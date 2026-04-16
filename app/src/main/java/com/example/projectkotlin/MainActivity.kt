package com.example.projectkotlin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.projectkotlin.navigation.NavRoutes
import com.example.projectkotlin.ui.screens.detail.DetailScreen
import com.example.projectkotlin.ui.screens.main.MainScreen
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

    NavHost(navController = navController, startDestination = NavRoutes.Splash.route) {
        splashGraph(navController)
        mainGraph(navController)
        detailGraph(navController)
    }
}

fun NavGraphBuilder.splashGraph(navController: NavHostController) {
    composable(NavRoutes.Splash.route) {
        SplashScreen(onNavigateToMain = {
            navController.navigate(NavRoutes.Main.route) {
                popUpTo(NavRoutes.Splash.route) { inclusive = true }
            }
        })
    }
}

fun NavGraphBuilder.mainGraph(navController: NavHostController) {
    composable(NavRoutes.Main.route) {
        MainScreen(
            onPokemonClick = { pokemonId ->
                navController.navigate(NavRoutes.Detail.createRoute(pokemonId))
            }
        )
    }
}

fun NavGraphBuilder.detailGraph(navController: NavHostController) {
    composable(
        route = NavRoutes.Detail.route,
        arguments = listOf(navArgument("pokemonId") { type = NavType.IntType })
    ) { backStackEntry ->
        val pokemonId = backStackEntry.arguments?.getInt("pokemonId") ?: 0
        DetailScreen(
            pokemonId = pokemonId,
            onBackClick = { navController.popBackStack() }
        )
    }
}
