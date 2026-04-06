package com.example.projectkotlin.ui.screens.detail

import com.example.projectkotlin.damain.model.Pokemon

sealed class DetailState {
    object Loading : DetailState()
    data class Success(val pokemon: Pokemon) : DetailState()
    data class Error(val message: String) : DetailState()
}

sealed class DetailIntent {
    data class LoadPokemonDetail(val pokemonName: String) : DetailIntent()
}