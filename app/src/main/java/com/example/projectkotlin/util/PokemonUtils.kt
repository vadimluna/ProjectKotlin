package com.example.projectkotlin.util

import androidx.compose.ui.graphics.Color

fun getStatSymbol(statName: String): String {
    return when (statName.lowercase()) {
        "hp" -> "❤️"
        "attack" -> "⚔️"
        "defense" -> "🛡️"
        "special-attack" -> "🔮"
        "special-defense" -> "🔰"
        "speed" -> "💨"
        else -> "🔸"
    }
}

fun getTypeColor(type: String): Color {
    return when (type.lowercase()) {
        "grass", "planta" -> Color(0xFF7AC74C)
        "fire", "fuego" -> Color(0xFFEE8130)
        "water", "agua" -> Color(0xFF6390F0)
        "bug", "bicho" -> Color(0xFFA6B91A)
        "normal" -> Color(0xFFA8A77A)
        "poison", "veneno" -> Color(0xFF980798)
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
fun formatHeight(height: Int): String = "${height / 10.0} m"
fun formatWeight(weight: Int): String = "${weight / 10.0} kg"

object AppColors {
    val SearchBarBackground = Color(0xFFF2F2F2)
}