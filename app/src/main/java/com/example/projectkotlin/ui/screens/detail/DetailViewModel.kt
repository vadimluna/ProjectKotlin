package com.example.projectkotlin.ui.screens.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectkotlin.domain.model.PokemonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {

    private val _state = MutableStateFlow<DetailState>(DetailState.Idle)
    val state: StateFlow<DetailState> = _state.asStateFlow()

    fun processIntent(intent: DetailIntent) {
        when (intent) {
            is DetailIntent.LoadPokemonDetail -> loadPokemonDetail(intent.pokemonId)
        }
    }

    private fun loadPokemonDetail(id: Int) {
        viewModelScope.launch {
            _state.value = DetailState.Loading
            try {
                val pokemon = repository.getPokemonDetail(id)
                val description = repository.getPokemonDescription(id)
                _state.value = DetailState.Success(pokemon.copy(description = description))
            } catch (e: IOException) {
                _state.value = DetailState.Error(DetailError.NetworkError)
            } catch (e: Exception) {
                val message = e.message ?: "Error desconocido"
                val error = if (message.contains("404")) DetailError.NotFound
                else DetailError.Unknown(message)
                _state.value = DetailState.Error(error)
            }
        }
    }
}