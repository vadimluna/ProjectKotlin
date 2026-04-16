package com.example.projectkotlin.data.repository

import com.example.projectkotlin.domain.model.Pokemon
import com.example.projectkotlin.domain.model.PokemonRepository
import com.example.projectkotlin.domain.model.PokemonStat
import com.example.projectkotlin.data.remote.PokeApi
import com.example.projectkotlin.data.remote.PokemonDetailResponse
import javax.inject.Inject

class PokemonRepositoryImpl @Inject constructor(
    private val api: PokeApi
) : PokemonRepository {

    override suspend fun getPokemonList(limit: Int, offset: Int): List<Pokemon> {
        return try {
            val response = api.getPokemonList(limit, offset)
            val pokemons = mutableListOf<Pokemon>()

            for (resource in response.results) {
                try {
                    val detail = api.getPokemonDetail(resource.name)
                    pokemons.add(mapToDomain(detail))
                } catch (e: Exception) {
                }
            }
            pokemons
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getPokemonsByType(type: String): List<Pokemon> {
        return try {
            val response = api.getPokemonsByType(type.lowercase())
            val pokemonListToFetch = response.pokemon.take(50)
            val pokemons = mutableListOf<Pokemon>()

            for (typePokemon in pokemonListToFetch) {
                try {
                    val detail = api.getPokemonDetail(typePokemon.pokemon.name)
                    pokemons.add(mapToDomain(detail))
                } catch (e: Exception) {
                }
            }
            pokemons
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getPokemonDetail(id: Int): Pokemon {
        val response = api.getPokemonDetail(id.toString())
        return mapToDomain(response)
    }

    override suspend fun getPokemonDescription(id: Int): String {
        return try {
            val response = api.getPokemonSpecies(id.toString())
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
                ?: response.sprites.frontDefault ?: "",
            types = response.types.map { it.type.name },
            height = response.height,
            weight = response.weight,
            abilities = response.abilities.map { it.ability.name },
            stats = response.stats.map { PokemonStat(name = it.stat.name, value = it.baseStat) }
        )
    }
}