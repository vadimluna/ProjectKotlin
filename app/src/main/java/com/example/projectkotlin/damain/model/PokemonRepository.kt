package com.example.projectkotlin.damain.model

import kotlinx.coroutines.flow.Flow

interface PokemonRepository {
    suspend fun getPokemonList(limit: Int, offset: Int): List<Pokemon>
    suspend fun getPokemonDetail(name: String): Pokemon
    suspend fun getPokemonDescription(id: Int): String
}
