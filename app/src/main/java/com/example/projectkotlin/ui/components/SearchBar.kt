package com.example.projectkotlin.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
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
// IMPORTAMOS EL NUEVO ARCHIVO DE TIPOS
import com.example.projectkotlin.ui.types.getTypeUIInfo

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    availableTypes: List<String>,
    selectedTypes: Set<String>,
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
                    modifier = Modifier.weight(1f),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    singleLine = true
                )
                if (isFocused) {
                    IconButton(onClick = { focusManager.clearFocus() }) {
                        Icon(Icons.Default.Close, contentDescription = "Cerrar", tint = Color.Gray)
                    }
                }
            }

            AnimatedVisibility(visible = isFocused) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Selecciona tipos (se cierra al elegir 2):", style = MaterialTheme.typography.labelLarge, color = Color.Gray)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    val allOptions = listOf(null) + availableTypes
                    allOptions.chunked(3).forEach { row ->
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            row.forEach { type ->
                                val isSelected = if (type == null) selectedTypes.isEmpty() else selectedTypes.contains(type)
                                val typeInfo = getTypeUIInfo(type)
                                val baseColor = typeInfo.color
                                val symbol = typeInfo.symbol

                                Surface(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable {
                                            onTypeSelect(type)

                                            val willHaveTwo = !isSelected && selectedTypes.size == 1
                                            if (type == null || willHaveTwo) {
                                                focusManager.clearFocus()
                                            }
                                        },
                                    shape = RoundedCornerShape(12.dp),
                                    color = if (isSelected) baseColor else baseColor.copy(alpha = 0.2f),
                                    border = if (isSelected) null else androidx.compose.foundation.BorderStroke(1.dp, baseColor)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(vertical = 10.dp),
                                        horizontalArrangement = Arrangement.Center,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {

                                        Text(
                                            text = "$symbol ${type?.replaceFirstChar { it.uppercase() } ?: "Todos"}",
                                            textAlign = TextAlign.Center,
                                            color = if (isSelected) Color.White else baseColor,
                                            fontSize = 11.sp,
                                            style = MaterialTheme.typography.labelMedium
                                        )
                                        if (type != null && isSelected) {
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Icon(Icons.Default.Close, null, tint = Color.White, modifier = Modifier.size(12.dp))
                                        }
                                    }
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