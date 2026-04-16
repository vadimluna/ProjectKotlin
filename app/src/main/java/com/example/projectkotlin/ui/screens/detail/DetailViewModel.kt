package com.example.projectkotlin.ui.screens.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectkotlin.domain.model.PokemonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {

    private val _state = MutableStateFlow<DetailState>(DetailState.Loading)
    val state: StateFlow<DetailState> = _state.asStateFlow()

    fun loadPokemon(id: Int) {
        viewModelScope.launch {
            _state.value = DetailState.Loading
            try {
                val pokemon = repository.getPokemonDetail(id)
                _state.value = DetailState.Success(pokemon)
            } catch (e: Exception) {
                _state.value = DetailState.Error(DetailError.Unknown(e.message ?: "Error al cargar el Pokémon"))
            }
        }
    }
}