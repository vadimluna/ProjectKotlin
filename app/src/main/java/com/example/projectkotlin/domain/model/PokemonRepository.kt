package com.example.projectkotlin.domain.model

interface PokemonRepository {
    suspend fun getPokemonList(limit: Int, offset: Int): List<Pokemon>
    suspend fun getPokemonDetail(id: Int): Pokemon
    suspend fun getPokemonDescription(id: Int): String
}