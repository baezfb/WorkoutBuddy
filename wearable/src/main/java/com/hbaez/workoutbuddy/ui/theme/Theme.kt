package com.hbaez.workoutbuddy.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.hbaez.core_ui.Cordovan
import com.hbaez.core_ui.DarkGray
import com.hbaez.core_ui.Dimensions
import com.hbaez.core_ui.LightGray
import com.hbaez.core_ui.LocalSpacing
import com.hbaez.core_ui.Mahogany
import com.hbaez.core_ui.MediumGray
import com.hbaez.core_ui.Orange
import com.hbaez.core_ui.TextWhite

private val DarkColorPalette = darkColors(
    primary = Cordovan,
    primaryVariant = Mahogany,
    secondary = Orange,
    background = MediumGray,
    onBackground = TextWhite,
    surface = LightGray,
    onSurface = TextWhite,
    onPrimary = Color.White,
    onSecondary = Color.White,
)

private val LightColorPalette = lightColors(
    primary = Cordovan,
    primaryVariant = Mahogany,
    secondary = Orange,
    background = Color.White,
    onBackground = DarkGray,
    surface = Color.White,
    onSurface = DarkGray,
    onPrimary = Color.White,
    onSecondary = Color.White,
)

@Composable
fun WorkoutBuddyWearableTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }
    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(
        color = Color.Black
    )
    CompositionLocalProvider(LocalSpacing provides Dimensions()) {
        MaterialTheme(
            colors = colors,
            typography = Typography,
            content = content
        )
    }
}