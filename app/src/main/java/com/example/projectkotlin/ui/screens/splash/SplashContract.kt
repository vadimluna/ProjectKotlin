package com.example.projectkotlin.ui.screens.splash

import com.example.projectkotlin.damain.model.Pokemon

sealed class SplashState {
    object Idle : SplashState()
    object Loading : SplashState()
    data class Success(val pokemon: List<Pokemon>) : SplashState()
    data class Error(val message: String) : SplashState()
}

sealed class SplashIntent {
    object LoadData : SplashIntent()
}
