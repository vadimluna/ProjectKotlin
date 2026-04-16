package com.example.projectkotlin.ui.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectkotlin.domain.model.Pokemon
import com.example.projectkotlin.domain.model.PokemonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {

    private val _state = MutableStateFlow<MainState>(MainState.Loading)
    val state: StateFlow<MainState> = _state.asStateFlow()

    private var allPokemon = listOf<Pokemon>()
    private var currentPage = 0
    private val pageSize = 20

    init {
        loadPokemon()
    }

    fun handleIntent(intent: MainIntent) {
        when (intent) {
            is MainIntent.LoadMore -> loadPokemon()
            is MainIntent.Search -> filterPokemon(intent.query)
            is MainIntent.ToggleType -> toggleType(intent.type)
            is MainIntent.ToggleHideFire -> toggleHideFire(intent.hide)
            is MainIntent.ToggleSort -> toggleSort(intent.sort)
        }
    }

    private fun loadPokemon() {
        viewModelScope.launch {
            try {
                val newPokemon = repository.getPokemonList(pageSize, currentPage * pageSize)
                allPokemon = allPokemon + newPokemon
                currentPage++
                updateState()
            } catch (e: Exception) {
                _state.value = MainState.Error(e.message ?: "Unknown error")
            }
        }
    }

    private fun filterPokemon(query: String) {
        updateState(query = query)
    }

    private fun toggleType(type: String?) {
        val current = _state.value as? MainState.Success ?: return
        val newSelected = if (type == null) {
            emptySet()
        } else {
            if (current.selectedTypes.contains(type)) {
                current.selectedTypes - type
            } else {
                current.selectedTypes + type
            }
        }
        updateState(selectedTypes = newSelected)
    }

    private fun toggleHideFire(hide: Boolean) {
        updateState(hideFire = hide)
    }

    private fun toggleSort(sort: Boolean) {
        updateState(sort = sort)
    }

    private fun updateState(
        query: String? = null,
        selectedTypes: Set<String>? = null,
        hideFire: Boolean? = null,
        sort: Boolean? = null
    ) {
        val current = _state.value as? MainState.Success
        val q = query ?: ""
        val types = selectedTypes ?: current?.selectedTypes ?: emptySet()
        val hide = hideFire ?: current?.hideFireType ?: false
        val s = sort ?: current?.sortAlphabetically ?: false

        var filtered = allPokemon.filter { it.name.contains(q, ignoreCase = true) }
        
        if (types.isNotEmpty()) {
            filtered = filtered.filter { pokemon ->
                pokemon.types.any { it in types }
            }
        }

        if (hide) {
            filtered = filtered.filter { pokemon ->
                pokemon.types.none { it.equals("fire", ignoreCase = true) }
            }
        }

        if (s) {
            filtered = filtered.sortedBy { it.name }
        }

        val availableTypes = allPokemon.flatMap { it.types }.distinct().sorted()

        _state.value = MainState.Success(
            pokemonList = filtered,
            availableTypes = availableTypes,
            selectedTypes = types,
            hideFireType = hide,
            sortAlphabetically = s
        )
    }
}
