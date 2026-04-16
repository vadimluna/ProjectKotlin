package com.example.projectkotlin.ui.screens.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
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
            val currentState = state as? MainState.Success
            val availableTypes = currentState?.availableTypes ?: emptyList()
            val selectedTypes = currentState?.selectedTypes ?: emptySet()

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

            when (val s = state) {
                is MainState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    }
                }
                is MainState.Success -> {
                    if (s.pokemonList.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("No se encontraron Pokémon", color = Color.White)
                        }
                    } else {
                        LazyColumn(state = listState, modifier = Modifier.fillMaxSize()) {
                            items(s.pokemonList) { pokemon ->
                                PokemonCard(pokemon = pokemon, onClick = { onPokemonClick(pokemon.id) })
                            }
                            if (searchQuery.isBlank() && selectedTypes.isEmpty()) {
                                item {
                                    Button(
                                        onClick = { viewModel.handleIntent(MainIntent.LoadMore) },
                                        modifier = Modifier.fillMaxWidth().padding(16.dp)
                                    ) {
                                        Text("Cargar más")
                                    }
                                }
                            }
                        }
                    }
                }
                is MainState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(s.message, color = Color.Red)
                    }
                }
            }
        }

        Box(
            modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp).padding(bottom = 32.dp)
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
                val current = state as? MainState.Success

                DropdownMenuItem(
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Ocultar tipo fuego", modifier = Modifier.weight(1f))
                            Switch(
                                checked = current?.hideFireType == true,
                                onCheckedChange = { viewModel.handleIntent(MainIntent.ToggleHideFire(it)) }
                            )
                        }
                    },
                    onClick = { viewModel.handleIntent(MainIntent.ToggleHideFire(!(current?.hideFireType ?: false))) }
                )

                DropdownMenuItem(
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Ordenar A-Z", modifier = Modifier.weight(1f))
                            Switch(
                                checked = current?.sortAlphabetically == true,
                                onCheckedChange = { viewModel.handleIntent(MainIntent.ToggleSort(it)) }
                            )
                        }
                    },
                    onClick = { viewModel.handleIntent(MainIntent.ToggleSort(!(current?.sortAlphabetically ?: false))) }
                )
            }
        }
    }
}