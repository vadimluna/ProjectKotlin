package com.example.projectkotlin.ui.screens.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
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

    Column(modifier = Modifier.fillMaxSize()) {
        val availableTypes = if (state is MainState.Success) (state as MainState.Success).availableTypes else emptyList()
        val selectedType = if (state is MainState.Success) (state as MainState.Success).selectedType else null

        SearchBar(
            query = searchQuery,
            onQueryChange = {
                searchQuery = it
                viewModel.handleIntent(MainIntent.Search(it))
            },
            availableTypes = availableTypes,
            selectedType = selectedType,
            onTypeSelect = { type ->
                viewModel.handleIntent(MainIntent.FilterType(type))
            }
        )

        when (val currentState = state) {
            is MainState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }
            is MainState.Success -> {
                LazyColumn(state = listState) {
                    items(currentState.pokemonList) { pokemon ->
                        PokemonCard(
                            pokemon = pokemon,
                            onClick = { onPokemonClick(pokemon.id) }
                        )
                    }
                }
                LaunchedEffect(listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index) {
                    val lastIndex = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
                    if (lastIndex >= currentState.pokemonList.size - 1 && currentState.pokemonList.isNotEmpty()) {
                        viewModel.handleIntent(MainIntent.LoadMore)
                    }
                }
            }
            is MainState.Error -> {
                Text(text = currentState.message, modifier = Modifier.align(Alignment.CenterHorizontally))
            }
        }
    }
}