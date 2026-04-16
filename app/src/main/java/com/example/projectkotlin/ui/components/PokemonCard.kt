package com.example.projectkotlin.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.projectkotlin.domain.model.Pokemon
import com.example.projectkotlin.util.getTypeColor

@Composable
fun PokemonCard(
    pokemon: Pokemon,
    onClick: () -> Unit
) {
    val typeColor = getTypeColor(pokemon.types.firstOrNull() ?: "")

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        border = BorderStroke(1.dp, typeColor),
        colors = CardDefaults.cardColors(
            containerColor = typeColor.copy(alpha = 0.2f)
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = pokemon.imageUrl,
                contentDescription = pokemon.name,
                modifier = Modifier.size(80.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = pokemon.name.replaceFirstChar { it.uppercaseChar() },
                    style = MaterialTheme.typography.titleLarge,
                    color = typeColor
                )
                Text(
                    text = "Tipos: ${pokemon.types.joinToString(", ")}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}