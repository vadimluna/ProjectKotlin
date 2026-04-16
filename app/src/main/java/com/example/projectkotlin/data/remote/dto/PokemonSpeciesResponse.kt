package com.example.projectkotlin.data.remote.dto

import com.google.gson.annotations.SerializedName

data class PokemonSpeciesResponse(
    @SerializedName("evolution_chain") val evolutionChain: EvolutionChainUrl
)

data class EvolutionChainUrl(
    val url: String
)