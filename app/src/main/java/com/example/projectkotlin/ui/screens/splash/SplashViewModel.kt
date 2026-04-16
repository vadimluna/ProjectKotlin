package com.example.projectkotlin.ui.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow<SplashState>(SplashState.Initial)
    val state: StateFlow<SplashState> = _state.asStateFlow()

    init {
        handleIntent(SplashIntent.StartTimer)
    }

    fun handleIntent(intent: SplashIntent) {
        when (intent) {
            is SplashIntent.StartTimer -> startTimer()
        }
    }

    private fun startTimer() {
        viewModelScope.launch {
            delay(2000L)
            _state.value = SplashState.NavigateToMain
        }
    }
}