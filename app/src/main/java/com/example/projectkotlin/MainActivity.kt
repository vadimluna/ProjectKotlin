package com.example.projectkotlin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.projectkotlin.ui.screens.detail.DetailScreen
import com.example.projectkotlin.ui.screens.main.MainScreen
import com.example.projectkotlin.ui.screens.main.MainViewModel
import com.example.projectkotlin.ui.screens.splash.SplashScreen
import com.example.projectkotlin.ui.theme.ProjectKotlinTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProjectKotlinTheme {
                PokeAppNavigation()
            }
        }
    }
}

@Composable
fun PokeAppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") {
            SplashScreen(onDataLoaded = {
                navController.navigate("main") {
                    popUpTo("splash") { inclusive = true }
                }
            })
        }

        composable("main") {
            MainScreen(
                onPokemonClick = { pokemon ->
                    navController.navigate("detail/${pokemon.name}")
                }
            )
        }

        composable(
            route = "detail/{pokemonName}",
            arguments = listOf(navArgument("pokemonName") { type = NavType.StringType })
        ) { backStackEntry ->
            val pokemonName = backStackEntry.arguments?.getString("pokemonName") ?: ""
            DetailScreen(
                pokemonName = pokemonName,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
