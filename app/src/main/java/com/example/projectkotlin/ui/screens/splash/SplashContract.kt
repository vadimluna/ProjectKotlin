package com.example.projectkotlin.ui.screens.splash

sealed class SplashState {
    object Initial : SplashState()
    object NavigateToMain : SplashState()
}

sealed class SplashIntent {
    object StartTimer : SplashIntent()
}