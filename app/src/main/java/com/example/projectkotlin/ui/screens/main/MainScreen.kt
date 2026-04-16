package com.example.projectkotlin.ui.screens.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.projectkotlin.R
import com.example.projectkotlin.ui.components.PokemonCard
import com.example.projectkotlin.ui.components.SearchBar

@Composable
fun MainScreen(
    onPokemonClick: (Int) -> Unit,
    viewModel: MainViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    var isMenuExpanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {

        Image(
            painter = painterResource(id = R.drawable.main_background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(modifier = Modifier.fillMaxSize()) {
            val availableTypes = if (state is MainState.Success) (state as MainState.Success).availableTypes else emptyList()
            val selectedTypes = if (state is MainState.Success) (state as MainState.Success).selectedTypes else emptySet()

            SearchBar(
                query = searchQuery,
                onQueryChange = {
                    searchQuery = it
                    viewModel.handleIntent(MainIntent.Search(it))
                },
                availableTypes = availableTypes,
                selectedTypes = selectedTypes,
                onTypeSelect = { type ->
                    viewModel.handleIntent(MainIntent.ToggleType(type))
                }
            )

            when (val currentState = state) {
                is MainState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    }
                }
                is MainState.Success -> {
                    if (currentState.pokemonList.isEmpty()) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "(X_X)",
                                fontSize = 48.sp,
                                color = Color.White,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )
                            Text(
                                text = "No se ha encontrado ningún Pokémon.",
                                color = Color.White,
                                style = MaterialTheme.typography.titleMedium,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Prueba a buscar con otro nombre, quita algunos filtros, o carga más Pokémon en la lista principal.",
                                color = Color.LightGray,
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center
                            )
                        }
                    } else {
                        LazyColumn(state = listState, modifier = Modifier.fillMaxSize()) {
                            items(currentState.pokemonList) { pokemon ->
                                PokemonCard(
                                    pokemon = pokemon,
                                    onClick = { onPokemonClick(pokemon.id) }
                                )
                            }

                            if (searchQuery.isBlank() && selectedTypes.isEmpty()) {
                                item {
                                    Button(
                                        onClick = { viewModel.handleIntent(MainIntent.LoadMore) },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp)
                                    ) {
                                        Text(text = "Cargar 20 Pokémon más")
                                    }
                                }
                            }
                        }
                    }
                }
                is MainState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = currentState.message, color = Color.Red)
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .padding(bottom = 32.dp)
        ) {
            FloatingActionButton(
                onClick = { isMenuExpanded = !isMenuExpanded },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
            }

            DropdownMenu(
                expanded = isMenuExpanded,
                onDismissRequest = { isMenuExpanded = false }
            ) {
                val currentState = state as? MainState.Success
                val hideFire = currentState?.hideFireType == true
                val sortAlpha = currentState?.sortAlphabetically == true

                DropdownMenuItem(
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Ocultar tipo fuego", modifier = Modifier.weight(1f))
                            Spacer(modifier = Modifier.width(8.dp))
                            Switch(
                                checked = hideFire,
                                onCheckedChange = {
                                    viewModel.handleIntent(MainIntent.ToggleHideFire(it))
                                }
                            )
                        }
                    },
                    onClick = {
                        viewModel.handleIntent(MainIntent.ToggleHideFire(!hideFire))
                    }
                )

                DropdownMenuItem(
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Ordenar A-Z", modifier = Modifier.weight(1f))
                            Spacer(modifier = Modifier.width(8.dp))
                            Switch(
                                checked = sortAlpha,
                                onCheckedChange = {
                                    viewModel.handleIntent(MainIntent.ToggleSort(it))
                                }
                            )
                        }
                    },
                    onClick = {
                        viewModel.handleIntent(MainIntent.ToggleSort(!sortAlpha))
                    }
                )
            }
        }
    }
}