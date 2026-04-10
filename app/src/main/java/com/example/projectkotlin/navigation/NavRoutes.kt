package com.example.projectkotlin.navigation

sealed class NavRoutes(val route: String) {
    object Splash : NavRoutes("splash")
    object Main : NavRoutes("main")
    object Detail : NavRoutes("detail/{pokemonId}") {
        fun createRoute(pokemonId: Int): String = "detail/$pokemonId"
    }
}
