package com.example.projectkotlin.ui.screens.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.projectkotlin.damain.model.Pokemon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    pokemonName: String,
    onBackClick: () -> Unit,
    viewModel: DetailViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(pokemonName) {
        viewModel.processIntent(DetailIntent.LoadPokemonDetail(pokemonName))
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Detalles") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Regresar")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (state) {
                is DetailState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is DetailState.Success -> {
                    val pokemon = (state as DetailState.Success).pokemon
                    PokemonDetailContent(pokemon)
                }
                is DetailState.Error -> {
                    Text(
                        text = "Error: ${(state as DetailState.Error).message}",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}

@Composable
fun PokemonDetailContent(pokemon: Pokemon) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = pokemon.imageUrl,
            contentDescription = pokemon.name,
            modifier = Modifier.size(250.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = pokemon.name.uppercase(), style = MaterialTheme.typography.headlineLarge)

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "Tipos: ${pokemon.types.joinToString(", ")}", style = MaterialTheme.typography.titleMedium)

        Spacer(modifier = Modifier.height(16.dp))

        Card(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = pokemon.description.ifEmpty { "Descripción no disponible." },
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Altura", style = MaterialTheme.typography.titleMedium)
                Text(text = "${pokemon.height / 10.0} m")
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Peso", style = MaterialTheme.typography.titleMedium)
                Text(text = "${pokemon.weight / 10.0} kg")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Habilidades", style = MaterialTheme.typography.titleLarge)
        Text(text = pokemon.abilities.joinToString(", "), style = MaterialTheme.typography.bodyLarge)

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Estadísticas Básicas", style = MaterialTheme.typography.titleLarge)
        pokemon.stats.forEach { stat ->
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = stat.name.uppercase())
                Text(text = stat.value.toString(), style = MaterialTheme.typography.labelLarge)
            }
            LinearProgressIndicator(
                progress = stat.value / 100f,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}