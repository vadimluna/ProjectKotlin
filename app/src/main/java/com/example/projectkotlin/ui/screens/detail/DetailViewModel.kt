package com.example.projectkotlin.ui.screens.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectkotlin.damain.model.PokemonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {

    private val _state = MutableStateFlow<DetailState>(DetailState.Loading)
    val state: StateFlow<DetailState> = _state

    fun processIntent(intent: DetailIntent) {
        when (intent) {
            is DetailIntent.LoadPokemonDetail -> loadPokemonDetail(intent.pokemonName)
        }
    }

    private fun loadPokemonDetail(name: String) {
        viewModelScope.launch {
            _state.value = DetailState.Loading
            try {
                val pokemon = repository.getPokemonDetail(name)
                _state.value = DetailState.Success(pokemon)
            } catch (e: Exception) {
                _state.value = DetailState.Error(e.message ?: "Error desconocido")
            }
        }
    }
}