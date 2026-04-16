package com.example.projectkotlin.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import com.example.projectkotlin.domain.model.PokemonStat
import kotlin.math.cos
import kotlin.math.sin

@OptIn(ExperimentalTextApi::class)
@Composable
fun RadarChart(
    stats: List<PokemonStat>,
    modifier: Modifier = Modifier,
    statColor: Color = Color.Blue
) {
    val textMeasurer = rememberTextMeasurer()

    Canvas(modifier = modifier.size(250.dp)) {
        val radius = size.minDimension / 2f - 30.dp.toPx()
        val center = Offset(size.width / 2f, size.height / 2f)
        val anglePerStat = (2 * Math.PI) / stats.size

        for (i in 1..5) {
            val stepRadius = radius * (i / 5f)
            val path = Path()
            stats.indices.forEach { index ->
                val angle = index * anglePerStat - Math.PI / 2
                val x = center.x + stepRadius * cos(angle).toFloat()
                val y = center.y + stepRadius * sin(angle).toFloat()
                if (index == 0) path.moveTo(x, y) else path.lineTo(x, y)
            }
            path.close()
            drawPath(path, color = Color.LightGray, style = Stroke(width = 1f))
        }

        val statPath = Path()
        stats.forEachIndexed { index, stat ->
            val angle = index * anglePerStat - Math.PI / 2
            val normalizedValue = (stat.value / 150f).coerceIn(0f, 1f)
            val currentRadius = radius * normalizedValue

            val x = center.x + currentRadius * cos(angle).toFloat()
            val y = center.y + currentRadius * sin(angle).toFloat()

            if (index == 0) statPath.moveTo(x, y) else statPath.lineTo(x, y)

            val textX = center.x + (radius + 20.dp.toPx()) * cos(angle).toFloat()
            val textY = center.y + (radius + 20.dp.toPx()) * sin(angle).toFloat()

            drawText(
                textMeasurer = textMeasurer,
                text = stat.name.take(3).uppercase(),
                topLeft = Offset(textX - 15f, textY - 15f),
            )
        }
        statPath.close()

        drawPath(statPath, color = statColor.copy(alpha = 0.4f))
        drawPath(statPath, color = statColor, style = Stroke(width = 3f))
    }
}