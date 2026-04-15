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
    private var currentTypeFilters: MutableSet<String> = linkedSetOf()

    private val allPossibleTypes = listOf(
        "grass", "fire", "water", "bug", "normal", "poison",
        "electric", "ground", "fairy", "fighting", "psychic",
        "rock", "ghost", "ice", "dragon", "dark", "steel", "flying"
    )

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
        if (isLoading || currentQuery.isNotBlank() || currentTypeFilters.isNotEmpty()) return

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
                applyFiltersAndEmit()
            }

            is MainIntent.ToggleType -> {
                if (intent.type == null) {
                    currentTypeFilters.clear()
                } else {
                    if (currentTypeFilters.contains(intent.type)) { currentTypeFilters.remove(intent.type)
                    } else {

                        if (currentTypeFilters.size >= 2) {

                            val oldestType = currentTypeFilters.first()
                            currentTypeFilters.remove(oldestType)
                        }

                        currentTypeFilters.add(intent.type)
                    }
                }
                applyFiltersAndEmit()
            }
        }
    }

    private fun applyFiltersAndEmit() {
        var result = allPokemons

        if (currentTypeFilters.isNotEmpty()) {
            result = result.filter { pokemon ->
                pokemon.types.containsAll(currentTypeFilters)
            }
        }

        if (currentQuery.isNotBlank()) {
            result = result.filter {
                it.name.contains(currentQuery, ignoreCase = true)
            }
        }

        _state.value = MainState.Success(
            pokemonList = result,
            availableTypes = allPossibleTypes,
            selectedTypes = currentTypeFilters.toSet()
        )
    }
}