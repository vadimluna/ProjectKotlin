package com.example.projectkotlin.ui.screens.splash

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectkotlin.damain.model.PokemonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {

    private val _state = MutableStateFlow<SplashState>(SplashState.Idle)
    val state: StateFlow<SplashState> = _state.asStateFlow()

    init {
        handleIntent(SplashIntent.LoadData)
    }

    fun handleIntent(intent: SplashIntent) {
        when (intent) {
            is SplashIntent.LoadData -> loadInitialData()
        }
    }

    private  fun loadInitialData() {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("PokeDebug", "A. Iniciando la carga en el ViewModel")
            _state.value = SplashState.Loading
            try {
                val pokemon = repository.getPokemonList(limit = 20, offset = 0)
                Log.d("PokeDebug", "B. Datos recibidos en ViewModel, actualizando pantalla")
                _state.value = SplashState.Success(pokemon)
            } catch (e: Exception) {
                Log.e("PokeDebug", "C. Error capturado: ${e.message}", e)
                _state.value = SplashState.Error(e.message ?: "Unknown error")
            }
        }
    }
}
