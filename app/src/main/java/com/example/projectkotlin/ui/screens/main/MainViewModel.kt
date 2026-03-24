package com.example.projectkotlin.ui.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectkotlin.damain.model.Pokemon
import com.example.projectkotlin.damain.model.PokemonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {

    private val _state = MutableStateFlow<MainState>(MainState.Loading)
    val state: StateFlow<MainState> = _state.asStateFlow()

    private var allPokemons: List<Pokemon> = emptyList()

    init {
        loadInitialPokemon()
    }

    private fun loadInitialPokemon() {
        viewModelScope.launch {
            _state.value = MainState.Loading
            try {
                val pokemons = repository.getPokemonList(limit = 20, offset = 0)
                allPokemons = pokemons
                _state.value = MainState.Success(pokemons)
            } catch (e: Exception) {
                _state.value = MainState.Error(e.message ?: "Error desconocido")
            }
        }
    }

    fun handleIntent(intent: MainIntent) {
        when (intent) {
            is MainIntent.LoadMore -> {
            }
            is MainIntent.Search -> {
                val query = intent.query
                if (query.isBlank()) {
                    _state.value = MainState.Success(allPokemons)
                } else {
                    val filteredList = allPokemons.filter { pokemon ->
                        pokemon.name.contains(query, ignoreCase = true) ||
                                pokemon.types.any { type -> type.contains(query, ignoreCase = true) }
                    }
                    _state.value = MainState.Success(filteredList)
                }
            }
        }
    }
}