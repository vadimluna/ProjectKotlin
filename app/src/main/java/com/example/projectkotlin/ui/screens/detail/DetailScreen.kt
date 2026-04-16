package com.example.projectkotlin.ui.screens.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.projectkotlin.ui.components.PokemonStatItem
import com.example.projectkotlin.ui.screens.detail.components.EvolutionWidget
import com.example.projectkotlin.util.getTypeColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    pokemonId: Int,
    onBackClick: () -> Unit,
    viewModel: DetailViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(pokemonId) {
        viewModel.loadPokemon(pokemonId)
    }

    when (val s = state) {
        is DetailState.Idle -> {
            // Nothing to show
        }
        is DetailState.Loading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is DetailState.Success -> {
            val pokemon = s.pokemon
            val themeColor = getTypeColor(pokemon.types.firstOrNull() ?: "")

            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text(pokemon.name.uppercase(), fontWeight = FontWeight.Bold) },
                        navigationIcon = {
                            IconButton(onClick = onBackClick) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "Back"
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = themeColor,
                            titleContentColor = Color.White,
                            navigationIconContentColor = Color.White
                        )
                    )
                }
            ) { padding ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .background(Color.White)
                ) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(themeColor)
                                .padding(bottom = 20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            AsyncImage(
                                model = pokemon.imageUrl,
                                contentDescription = null,
                                modifier = Modifier.size(200.dp)
                            )
                        }
                    }

                    item {
                        Text(
                            "Estadísticas Base",
                            modifier = Modifier.padding(16.dp),
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                            pokemon.stats.forEach { stat ->
                                PokemonStatItem(stat = stat, color = themeColor)
                            }
                        }
                    }

                    if (pokemon.evolutionChain.isNotEmpty()) {
                        item {
                            Text(
                                "Cadena Evolutiva",
                                modifier = Modifier.padding(16.dp),
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                pokemon.evolutionChain.forEach { step ->
                                    EvolutionWidget(step)
                                }
                            }
                        }
                    }
                }
            }
        }
        is DetailState.Error -> {
            val errorMessage = when (val err = s.error) {
                is DetailError.NetworkError -> "Error de red"
                is DetailError.NotFound -> "Pokémon no encontrado"
                is DetailError.Unknown -> err.message
            }
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(errorMessage, color = Color.Red)
                    Spacer(Modifier.height(16.dp))
                    Button(onClick = onBackClick) {
                        Text("Volver")
                    }
                }
            }
        }
    }
}
