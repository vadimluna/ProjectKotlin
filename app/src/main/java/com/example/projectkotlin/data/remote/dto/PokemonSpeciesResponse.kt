package com.example.projectkotlin.data.remote.dto

import com.google.gson.annotations.SerializedName

data class PokemonSpeciesResponse(
    @SerializedName("evolution_chain") val evolutionChain: EvolutionChainUrl,
    @SerializedName("flavor_text_entries") val flavorTextEntries: List<FlavorTextEntry>
)

data class EvolutionChainUrl(
    val url: String
)

data class FlavorTextEntry(
    @SerializedName("flavor_text") val flavorText: String,
    val language: SpeciesLanguage
)

data class SpeciesLanguage(
    val name: String
)
