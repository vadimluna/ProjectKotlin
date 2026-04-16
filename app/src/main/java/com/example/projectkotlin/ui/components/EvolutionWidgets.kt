package com.example.projectkotlin.ui.screens.detail.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.projectkotlin.domain.model.EvolutionStep

@Composable
fun EvolutionWidget(step: EvolutionStep) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(8.dp)
    ) {
        AsyncImage(
            model = step.imageUrl,
            contentDescription = null,
            modifier = Modifier.size(90.dp)
        )
        Text(
            text = step.name.replaceFirstChar { it.uppercase() },
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}