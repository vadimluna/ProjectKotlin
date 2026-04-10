package com.example.projectkotlin.ui.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectkotlin.domain.model.Pokemon
import com.example.projectkotlin.domain.model.PokemonRepository
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
    private var currentOffset = 0
    private var isLoading = false
    private var currentQuery = ""

    init {
        loadInitialPokemon()
    }

    private fun loadInitialPokemon() {
        viewModelScope.launch {
            _state.value = MainState.Loading
            try {
                val pokemons = repository.getPokemonList(limit = 20, offset = currentOffset)
                allPokemons = pokemons
                _state.value = MainState.Success(pokemons)
            } catch (e: Exception) {
                _state.value = MainState.Error(e.message ?: "Error desconocido")
            }
        }
    }

    private fun loadMorePokemon() {
        if (isLoading || currentQuery.isNotBlank()) return

        viewModelScope.launch {
            isLoading = true
            try {
                currentOffset += 20
                val newPokemons = repository.getPokemonList(limit = 20, offset = currentOffset)
                allPokemons = allPokemons + newPokemons
                _state.value = MainState.Success(allPokemons)
            } catch (e: Exception) {

            } finally {
                isLoading = false
            }
        }
    }

    fun handleIntent(intent: MainIntent) {
        when (intent) {
            is MainIntent.LoadMore -> {
                loadMorePokemon()
            }
            is MainIntent.Search -> {
                currentQuery = intent.query
                if (currentQuery.isBlank()) {
                    _state.value = MainState.Success(allPokemons)
                } else {
                    val filteredList = allPokemons.filter { pokemon ->
                        pokemon.name.contains(currentQuery, ignoreCase = true) ||
                                pokemon.types.any { type -> type.contains(currentQuery, ignoreCase = true) }
                    }
                    _state.value = MainState.Success(filteredList)
                }
            }
        }
    }
}