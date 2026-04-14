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
    private var currentTypeFilter: String? = null

    init {
        loadInitialPokemon()
    }

    private fun loadInitialPokemon() {
        viewModelScope.launch {
            _state.value = MainState.Loading
            try {
                val pokemons = repository.getPokemonList(limit = 20, offset = currentOffset)
                allPokemons = pokemons
                applyFiltersAndEmit()
            } catch (e: Exception) {
                _state.value = MainState.Error(e.message ?: "Error desconocido")
            }
        }
    }

    private fun loadMorePokemon() {
        if (isLoading || currentQuery.isNotBlank() || currentTypeFilter != null) return

        viewModelScope.launch {
            isLoading = true
            try {
                currentOffset += 20
                val newPokemons = repository.getPokemonList(limit = 20, offset = currentOffset)
                allPokemons = allPokemons + newPokemons
                applyFiltersAndEmit()
            } catch (e: Exception) {
            } finally {
                isLoading = false
            }
        }
    }

    fun handleIntent(intent: MainIntent) {
        when (intent) {
            is MainIntent.LoadMore -> loadMorePokemon()
            is MainIntent.Search -> {
                currentQuery = intent.query
                if (currentTypeFilter != null) {
                    val currentState = _state.value
                    if (currentState is MainState.Success) {
                        val filteredList = currentState.pokemonList.filter {
                            it.name.contains(currentQuery, ignoreCase = true)
                        }
                        _state.value = currentState.copy(pokemonList = filteredList)
                    }
                } else {
                    applyFiltersAndEmit()
                }
            }
            is MainIntent.FilterType -> {
                currentTypeFilter = intent.type
                if (intent.type == null) {
                    applyFiltersAndEmit()
                } else {
                    fetchPokemonsByType(intent.type)
                }
            }
        }
    }

    private fun fetchPokemonsByType(type: String) {
        viewModelScope.launch {
            _state.value = MainState.Loading
            try {
                val pokemonsByType = repository.getPokemonsByType(type)

                var result = pokemonsByType
                if (currentQuery.isNotBlank()) {
                    result = result.filter {
                        it.name.contains(currentQuery, ignoreCase = true)
                    }
                }

                val availableTypes = allPokemons.flatMap { it.types }.distinct()

                _state.value = MainState.Success(
                    pokemonList = result,
                    availableTypes = availableTypes,
                    selectedType = currentTypeFilter
                )
            } catch (e: Exception) {
                _state.value = MainState.Error(e.message ?: "Error al cargar tipos")
            }
        }
    }

    private fun applyFiltersAndEmit() {
        val availableTypes = allPokemons.flatMap { it.types }.distinct()
        var result = allPokemons

        if (currentQuery.isNotBlank()) {
            result = result.filter {
                it.name.contains(currentQuery, ignoreCase = true)
            }
        }

        _state.value = MainState.Success(
            pokemonList = result,
            availableTypes = availableTypes,
            selectedType = null
        )
    }
}