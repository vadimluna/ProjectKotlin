package com.example.projectkotlin.ui.screens.main

import com.example.projectkotlin.domain.model.Pokemon

sealed class MainState {
    object Loading : MainState()
    data class Success(
        val pokemonList: List<Pokemon>,
        val availableTypes: List<String> = emptyList(),
        val selectedTypes: Set<String> = emptySet()
    ) : MainState()
    data class Error(val message: String) : MainState()
}

sealed class MainIntent {
    object LoadMore : MainIntent()
    data class Search(val query: String) : MainIntent()
    data class ToggleType(val type: String?) : MainIntent()
}