package com.example.projectkotlin.data.remote

import com.google.gson.annotations.SerializedName

data class PokemonListResponse(
    val results: List<PokemonNamedResource>
)

data class PokemonNamedResource(
    val name: String,
    val url: String
)

data class PokemonDetailResponse(
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val types: List<TypeSlot>,
    val sprites: Sprites,
    val stats: List<StatSlot>,
    val abilities: List<AbilitySlot>
)

data class TypeSlot(
    val type: NamedResource
)

data class StatSlot(
    @SerializedName("base_stat") val baseStat: Int,
    val stat: NamedResource
)

data class AbilitySlot(
    val ability: NamedResource
)

data class NamedResource(
    val name: String
)

data class Sprites(
    @SerializedName("front_default") val frontDefault: String,
    val other: OtherSprites?
)

data class OtherSprites(
    @SerializedName("official-artwork") val officialArtwork: OfficialArtwork?
)

data class OfficialArtwork(
    @SerializedName("front_default") val frontDefault: String?
)

data class PokemonSpeciesResponse(
    @SerializedName("flavor_text_entries") val flavorTextEntries: List<FlavorTextEntry>
)

data class FlavorTextEntry(
    @SerializedName("flavor_text") val flavorText: String,
    val language: NamedResource
)
