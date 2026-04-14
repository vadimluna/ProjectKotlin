package com.example.projectkotlin.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    availableTypes: List<String>,
    selectedType: String?,
    onTypeSelect: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    var isFocused by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .onFocusChanged { isFocused = it.isFocused },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF2F2F2)),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isFocused) 8.dp else 2.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray)
                TextField(
                    value = query,
                    onValueChange = onQueryChange,
                    placeholder = { Text("Buscar por nombre...") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    singleLine = true
                )
            }

            AnimatedVisibility(visible = isFocused) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Selecciona un tipo:", style = MaterialTheme.typography.labelLarge, color = Color.Gray)
                    Spacer(modifier = Modifier.height(12.dp))

                    val allOptions = listOf(null) + availableTypes
                    allOptions.chunked(3).forEach { row ->
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            row.forEach { type ->
                                Surface(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable {
                                            onTypeSelect(type)
                                            focusManager.clearFocus()
                                        },
                                    shape = RoundedCornerShape(12.dp),
                                    color = if (selectedType == type) getTypeColor(type ?: "") else getTypeColor(type ?: "").copy(alpha = 0.2f),
                                    border = if (selectedType == type) null else androidx.compose.foundation.BorderStroke(1.dp, getTypeColor(type ?: ""))
                                ) {
                                    Text(
                                        text = type?.replaceFirstChar { it.uppercase() } ?: "Todos",
                                        modifier = Modifier.padding(vertical = 10.dp),
                                        textAlign = TextAlign.Center,
                                        color = if (selectedType == type) Color.White else getTypeColor(type ?: ""),
                                        fontSize = 12.sp,
                                        style = MaterialTheme.typography.labelMedium
                                    )
                                }
                            }
                            if (row.size < 3) {
                                repeat(3 - row.size) { Spacer(modifier = Modifier.weight(1f)) }
                            }
                        }
                    }
                }
            }
        }
    }
}

fun getTypeColor(type: String): Color {
    return when (type.lowercase()) {
        "grass", "planta" -> Color(0xFF7AC74C)
        "fire", "fuego" -> Color(0xFFEE8130)
        "water", "agua" -> Color(0xFF6390F0)
        "bug", "bicho" -> Color(0xFFA6B91A)
        "normal" -> Color(0xFFA8A77A)
        "poison", "veneno" -> Color(0xFFA33EA1)
        "electric", "electrico" -> Color(0xFFF7D02C)
        "ground", "tierra" -> Color(0xFFE2BF65)
        "fairy", "hada" -> Color(0xFFD685AD)
        "fighting", "lucha" -> Color(0xFFC22E28)
        "psychic", "psiquico" -> Color(0xFFF95587)
        "rock", "roca" -> Color(0xFFB6A136)
        "ghost", "fantasma" -> Color(0xFF735797)
        "ice", "hielo" -> Color(0xFF96D9D6)
        "dragon", "dragon" -> Color(0xFF6F35FC)
        "dark", "siniestro" -> Color(0xFF705746)
        "steel", "acero" -> Color(0xFFB7B7CE)
        "flying", "volador" -> Color(0xFFA98FF0)
        else -> Color.DarkGray
    }
}