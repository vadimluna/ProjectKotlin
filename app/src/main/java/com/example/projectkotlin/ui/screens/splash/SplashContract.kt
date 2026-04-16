package com.example.projectkotlin.ui.screens.splash

import com.example.projectkotlin.domain.model.Pokemon

sealed class SplashState {
    object Idle : SplashState()

    data class Loading(
        val progress: Float = 0f,
        val downloadedMb: Float = 0f,
        val totalMb: Float = 60.7f,
        val estimatedSeconds: Int = 8
    ) : SplashState()

    data class Success(val pokemon: List<Pokemon>) : SplashState()
    data class Error(val message: String) : SplashState()
}

sealed class SplashIntent {
    object LoadData : SplashIntent()
}