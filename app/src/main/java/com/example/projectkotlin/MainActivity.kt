package com.example.projectkotlin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.projectkotlin.ui.screens.main.MainScreen
import com.example.projectkotlin.ui.screens.main.MainViewModel
import com.example.projectkotlin.ui.theme.ProjectKotlinTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProjectKotlinTheme {
                val mainViewModel: MainViewModel = hiltViewModel()
                MainScreen(viewModel = mainViewModel)
            }
        }
    }
}