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
import com.example.projectkotlin.domain.model.Pokemon
import com.example.projectkotlin.util.getStatSymbol
import com.example.projectkotlin.util.getStatSymbol
import com.example.projectkotlin.util.formatHeight
import com.example.projectkotlin.util.formatWeight
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    pokemonId: Int,
    onBackClick: () -> Unit,
    viewModel: DetailViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(pokemonId) {
        viewModel.processIntent(DetailIntent.LoadPokemonDetail(pokemonId))
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
            when (val currentState = state) {
                is DetailState.Idle -> {
                }
                is DetailState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is DetailState.Success -> {
                    PokemonDetailContent(currentState.pokemon)
                }
                is DetailState.Error -> {
                    val message = when (val error = currentState.error) {
                        is DetailError.NetworkError -> "Error de red"
                        is DetailError.NotFound -> "Pokémon no encontrado"
                        is DetailError.Unknown -> error.message
                    }
                    Text(
                        text = "Error: $message",
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
                Text(text = formatHeight(pokemon.height))
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Peso", style = MaterialTheme.typography.titleMedium)
                Text(text = formatWeight(pokemon.weight))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Habilidades", style = MaterialTheme.typography.titleLarge)
        Text(text = pokemon.abilities.joinToString(", "), style = MaterialTheme.typography.bodyLarge)

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Estadísticas Básicas",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        pokemon.stats.forEach { stat ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${getStatSymbol(stat.name)} ${stat.name.uppercase()}",
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = stat.value.toString(),
                    style = MaterialTheme.typography.labelLarge
                )
            }

            LinearProgressIndicator(
                progress = stat.value / 100f,
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )
        }
    }
}