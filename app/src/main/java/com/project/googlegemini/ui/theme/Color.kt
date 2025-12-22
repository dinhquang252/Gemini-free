package com.project.googlegemini.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Gemini AI Brand Colors
val GeminiBlue = Color(0xFF4285F4)
val GeminiRed = Color(0xFFEA4335)
val GeminiYellow = Color(0xFFFBBC04)
val GeminiGreen = Color(0xFF34A853)

// Light Mode Colors (Transparent & Gradient theme)
val LightPrimary = Color(0xFF4285F4) // Gemini Blue
val LightSecondary = Color(0xFF34A853) // Green
val LightTertiary = Color(0xFFEA4335) // Red
val LightBackground = Color(0xFFFAFAFA) // Very Light Gray
val LightSurface = Color(0xFFFFFFFF) // Pure White
val LightOnPrimary = Color(0xFFFFFFFF) // White
val LightOnSecondary = Color(0xFFFFFFFF) // White
val LightOnBackground = Color(0xFF1F1F1F) // Almost Black
val LightOnSurface = Color(0xFF1F1F1F) // Almost Black
val LightOutline = Color(0xFFE0E0E0) // Light Border

// Dark Mode Colors (Transparent & Gradient theme)
val DarkPrimary = Color(0xFF8AB4F8) // Light Blue
val DarkSecondary = Color(0xFF81C995) // Light Green
val DarkTertiary = Color(0xFFF28B82) // Light Red
val DarkBackground = Color(0xFF0A0A0A) // Almost Black
val DarkSurface = Color(0xFF1A1A1A) // Dark Surface
val DarkOnPrimary = Color(0xFF000000) // Black
val DarkOnSecondary = Color(0xFF000000) // Black
val DarkOnBackground = Color(0xFFE8E8E8) // Light Gray
val DarkOnSurface = Color(0xFFE8E8E8) // Light Gray
val DarkOutline = Color(0xFF3C3C3C) // Dark Border

// Gradient Brushes for Light Mode
val LightGradientPrimary = Brush.linearGradient(
    colors = listOf(
        Color(0xFF4285F4), // Blue
        Color(0xFF34A853)  // Green
    )
)

val LightGradientBackground = Brush.verticalGradient(
    colors = listOf(
        Color(0xFFFAFAFA),
        Color(0xFFFFFFFF)
    )
)

// Gradient Brushes for Dark Mode
val DarkGradientPrimary = Brush.linearGradient(
    colors = listOf(
        Color(0xFF8AB4F8), // Light Blue
        Color(0xFF81C995)  // Light Green
    )
)

val DarkGradientBackground = Brush.verticalGradient(
    colors = listOf(
        Color(0xFF0A0A0A),
        Color(0xFF1A1A1A)
    )
)

// Message bubble colors with transparency
val LightUserMessageStart = Color(0xFF4285F4) // Blue
val LightUserMessageEnd = Color(0xFF34A853) // Green
val LightGeminiMessageBackground = Color(0xFFF5F5F5).copy(alpha = 0.6f) // Transparent Light Gray

val DarkUserMessageStart = Color(0xFF8AB4F8) // Light Blue
val DarkUserMessageEnd = Color(0xFF81C995) // Light Green
val DarkGeminiMessageBackground = Color(0xFF2A2A2A).copy(alpha = 0.6f) // Transparent Dark Gray

// Gemini AI Icon Gradient (4 colors)
val GeminiIconGradient = Brush.sweepGradient(
    colors = listOf(
        GeminiBlue,   // Blue
        GeminiGreen,  // Green
        GeminiYellow, // Yellow
        GeminiRed,    // Red
        GeminiBlue    // Back to Blue for smooth loop
    )
)
