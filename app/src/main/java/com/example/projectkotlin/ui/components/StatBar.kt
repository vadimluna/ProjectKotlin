package com.example.projectkotlin.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projectkotlin.domain.model.PokemonStat

@Composable
fun PokemonStatItem(stat: PokemonStat, color: Color) {
    var animatedProgress by remember { mutableStateOf(0f) }
    val progress by animateFloatAsState(
        targetValue = animatedProgress,
        animationSpec = tween(durationMillis = 1000)
    )

    LaunchedEffect(Unit) {
        animatedProgress = stat.value.toFloat() / 255f
    }

    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stat.name.uppercase().take(3),
            modifier = Modifier.width(40.dp),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .height(10.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color.LightGray.copy(alpha = 0.2f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(progress)
                    .background(color)
                    .clip(RoundedCornerShape(10.dp))
            )
        }

        Text(
            text = stat.value.toString(),
            modifier = Modifier.width(35.dp).padding(start = 8.dp),
            fontSize = 12.sp
        )
    }
}