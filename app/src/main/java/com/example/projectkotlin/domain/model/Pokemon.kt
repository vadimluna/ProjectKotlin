package com.example.projectkotlin.domain.model

data class Pokemon(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val types: List<String>,
    val weight: Int,
    val height: Int,
    val stats: List<PokemonStat> = emptyList(),
    val evolutionChain: List<EvolutionStep> = emptyList()
)

data class PokemonStat(
    val name: String,
    val value: Int,
    val maxValue: Int = 255
)

data class EvolutionStep(
    val id: Int,
    val name: String,
    val imageUrl: String
)