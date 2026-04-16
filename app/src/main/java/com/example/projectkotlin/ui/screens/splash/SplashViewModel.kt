package com.example.projectkotlin.ui.screens.splash

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectkotlin.domain.model.PokemonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
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

    private fun loadInitialData() {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("PokeDebug", "A. Iniciando la carga interactiva en el ViewModel")

            val totalMb = 60.7f

            try {
                val pokemonDeferred = async {
                    repository.getPokemonList(limit = 20, offset = 0)
                }

                for (i in 1..100) {
                    delay(35)
                    val currentMb = (i / 100f) * totalMb
                    val timeRemaining = 8 - (i * 8 / 100)

                    _state.value = SplashState.Loading(
                        progress = i / 100f,
                        downloadedMb = currentMb,
                        totalMb = totalMb,
                        estimatedSeconds = if (timeRemaining < 0) 0 else timeRemaining
                    )
                }
                val pokemon = pokemonDeferred.await()
                Log.d("PokeDebug", "B. Datos recibidos, avanzando a la app")
                _state.value = SplashState.Success(pokemon)

            } catch (e: Exception) {
                Log.e("PokeDebug", "C. Error capturado: ${e.message}", e)
                _state.value = SplashState.Error(e.message ?: "Unknown error")
            }
        }
    }
}