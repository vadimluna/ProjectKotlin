package com.example.projectkotlin.data.repository

import android.util.Log
import com.example.projectkotlin.damain.model.Pokemon
import com.example.projectkotlin.damain.model.PokemonRepository
import com.example.projectkotlin.damain.model.PokemonStat
import com.example.projectkotlin.data.remote.PokeApi
import com.example.projectkotlin.data.remote.PokemonDetailResponse
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class PokemonRepositoryImpl @Inject constructor(
    private val api: PokeApi
) : PokemonRepository {

    override suspend fun getPokemonList(limit: Int, offset: Int): List<Pokemon> {
        Log.d("PokeDebug", "1. Pidiendo lista base a la API...")
        val response = api.getPokemonList(limit, offset)
        Log.d("PokeDebug", "2. Lista base obtenida con ${response.results.size} pokemons. Pidiendo detalles...")

        return coroutineScope {
            val pokemons = response.results.map { resource ->
                async {
                    Log.d("PokeDebug", "-> Pidiendo detalle de: ${resource.name}")
                    getPokemonDetail(resource.name)
                }
            }.awaitAll()

            Log.d("PokeDebug", "3. ¡Todos los detalles descargados!")
            pokemons
        }
    }

    override suspend fun getPokemonDetail(name: String): Pokemon {
        val response = api.getPokemonDetail(name)
        return mapToDomain(response)
    }

    override suspend fun getPokemonDescription(id: Int): String {
        return try {
            val response = api.getPokemonSpecies(id)
            response.flavorTextEntries
                .firstOrNull { it.language.name == "en" || it.language.name == "es" }
                ?.flavorText?.replace("\n", " ") ?: ""
        } catch (e: Exception) {
            ""
        }
    }

    private fun mapToDomain(response: PokemonDetailResponse): Pokemon {
        return Pokemon(
            id = response.id,
            name = response.name.replaceFirstChar { it.uppercase() },
            imageUrl = response.sprites.other?.officialArtwork?.frontDefault
                ?: response.sprites.frontDefault
                ?: "",
            types = response.types.map { it.type.name },
            height = response.height,
            weight = response.weight,
            abilities = response.abilities.map { it.ability.name },
            stats = response.stats.map {
                PokemonStat(name = it.stat.name, value = it.baseStat)
            }
        )
    }
}
