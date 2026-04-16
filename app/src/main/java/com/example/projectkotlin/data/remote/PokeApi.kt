package com.example.projectkotlin.data.remote

import com.example.projectkotlin.data.remote.dto.EvolutionChainResponse
import com.example.projectkotlin.data.remote.dto.PokemonSpeciesResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface PokeApi {
    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): PokemonListResponse

    @GET("pokemon/{id}")
    suspend fun getPokemonDetail(
        @Path("id") id: String
    ): PokemonDetailResponse

    @GET("pokemon-species/{id}")
    suspend fun getPokemonSpecies(
        @Path("id") id: String
    ): PokemonSpeciesResponse

    @GET
    suspend fun getEvolutionChain(@Url url: String): EvolutionChainResponse

    @GET("type/{type}")
    suspend fun getPokemonsByType(@Path("type") type: String): TypeResponse
}