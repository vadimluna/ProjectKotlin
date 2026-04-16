package com.example.projectkotlin.data.repository

import com.example.projectkotlin.data.remote.PokeApi
import com.example.projectkotlin.data.remote.PokemonDetailResponse
import com.example.projectkotlin.data.remote.dto.ChainLink
import com.example.projectkotlin.domain.model.EvolutionStep
import com.example.projectkotlin.domain.model.Pokemon
import com.example.projectkotlin.domain.model.PokemonRepository
import com.example.projectkotlin.domain.model.PokemonStat
import javax.inject.Inject

class PokemonRepositoryImpl @Inject constructor(
    private val api: PokeApi
) : PokemonRepository {

    override suspend fun getPokemonDetail(id: Int): Pokemon {
        // 1. Obtener detalle básico (stats, tipos)
        val detail = api.getPokemonDetail(id.toString())


        val species = api.getPokemonSpecies(id.toString())

        val evolutionData = api.getEvolutionChain(species.evolutionChain.url)

        return mapToDomain(detail, evolutionData.chain)
    }

    private suspend fun mapToDomain(
        response: PokemonDetailResponse,
        evolutionChain: ChainLink? = null
    ): Pokemon {
        val evolutionSteps = mutableListOf<EvolutionStep>()


        var currentChain = evolutionChain
        while (currentChain != null) {
            val pokeId = currentChain.species.url.split("/").dropLast(1).last()
            evolutionSteps.add(
                EvolutionStep(
                    id = pokeId.toInt(),
                    name = currentChain.species.name,
                    imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$pokeId.png"
                )
            )
            currentChain = currentChain.evolvesTo.firstOrNull()
        }

        return Pokemon(
            id = response.id,
            name = response.name,
            imageUrl = response.sprites.other?.officialArtwork?.frontDefault ?: "",
            types = response.types.map { it.type.name },
            weight = response.weight,
            height = response.height,
            stats = response.stats.map {
                PokemonStat(name = it.stat.name, value = it.baseStat)
            },
            evolutionChain = evolutionSteps
        )
    }


    override suspend fun getPokemonList(limit: Int, offset: Int): List<Pokemon> = emptyList()
    override suspend fun getPokemonsByType(type: String): List<Pokemon> = emptyList()
    override suspend fun getPokemonDescription(id: Int): String = ""
}
