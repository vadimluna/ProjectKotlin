package com.example.projectkotlin.ui.screens.detail

import com.example.projectkotlin.domain.model.Pokemon

sealed class DetailError {
    object NetworkError : DetailError()
    object NotFound : DetailError()
    data class Unknown(val message: String) : DetailError()
}

sealed class DetailState {
    object Idle : DetailState()
    object Loading : DetailState()
    data class Success(val pokemon: Pokemon) : DetailState()
    data class Error(val error: DetailError) : DetailState()
}

sealed class DetailIntent {
    data class LoadPokemonDetail(val pokemonId: Int) : DetailIntent()
}