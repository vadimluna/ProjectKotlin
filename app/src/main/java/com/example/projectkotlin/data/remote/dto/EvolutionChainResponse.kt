package com.example.projectkotlin.data.remote.dto

import com.google.gson.annotations.SerializedName

data class EvolutionChainResponse(
    val chain: ChainLink
)

data class ChainLink(
    val species: SpeciesShort,
    @SerializedName("evolves_to") val evolvesTo: List<ChainLink>
)

data class SpeciesShort(
    val name: String,
    val url: String
)