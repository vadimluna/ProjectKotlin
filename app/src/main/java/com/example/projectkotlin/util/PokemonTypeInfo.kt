package com.example.projectkotlin.ui.types

import androidx.compose.ui.graphics.Color

data class TypeUIInfo(val color: Color, val symbol: String)

fun getTypeUIInfo(type: String?): TypeUIInfo {
    return when (type?.lowercase()) {
        "grass", "planta" -> TypeUIInfo(Color(0xFF7AC74C), "🌿")
        "fire", "fuego" -> TypeUIInfo(Color(0xFFEE8130), "🔥")
        "water", "agua" -> TypeUIInfo(Color(0xFF6390F0), "💧")
        "bug", "bicho" -> TypeUIInfo(Color(0xFFA6B91A), "🐛")
        "normal" -> TypeUIInfo(Color(0xFFA8A77A), "⏺️")
        "poison", "veneno" -> TypeUIInfo(Color(0xFFA33EA1), "☠️")
        "electric", "electrico" -> TypeUIInfo(Color(0xFFF7D02C), "⚡")
        "ground", "tierra" -> TypeUIInfo(Color(0xFFE2BF65), "🏜️")
        "fairy", "hada" -> TypeUIInfo(Color(0xFFD685AD), "✨")
        "fighting", "lucha" -> TypeUIInfo(Color(0xFFC22E28), "🥊")
        "psychic", "psiquico" -> TypeUIInfo(Color(0xFFF95587), "🔮")
        "rock", "roca" -> TypeUIInfo(Color(0xFFB6A136), "🪨")
        "ghost", "fantasma" -> TypeUIInfo(Color(0xFF735797), "👻")
        "ice", "hielo" -> TypeUIInfo(Color(0xFF96D9D6), "❄️")
        "dragon", "dragon" -> TypeUIInfo(Color(0xFF6F35FC), "🐉")
        "dark", "siniestro" -> TypeUIInfo(Color(0xFF705746), "🌙")
        "steel", "acero" -> TypeUIInfo(Color(0xFFB7B7CE), "⚙️")
        "flying", "volador" -> TypeUIInfo(Color(0xFFA98FF0), "🦅")
        null -> TypeUIInfo(Color.DarkGray, "🔍")
        else -> TypeUIInfo(Color.DarkGray, "❓")
    }
}