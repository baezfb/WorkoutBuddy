package com.hbaez.wear_presentation.wear_overview.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import com.hbaez.core_ui.Dimensions
import com.hbaez.core_ui.LocalSpacing
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorPalette = darkColorScheme(
//    primary = Cordovan,
//    primaryVariant = Mahogany,
//    secondary = Orange,
//    background = MediumGray,
//    onBackground = TextWhite,
//    surface = LightGray,
//    onSurface = TextWhite,
//    onPrimary = Color.White,
//    onSecondary = Color.White,
)

private val LightColorPalette = lightColorScheme(
//    primary = Cordovan,
//    primaryVariant = Mahogany,
//    secondary = Orange,
//    background = Color.White,
//    onBackground = DarkGray,
//    surface = Color.White,
//    onSurface = DarkGray,
//    onPrimary = Color.White,
//    onSecondary = Color.White,
)

@Composable
fun WorkoutBuddyTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit) {
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
            colorScheme = colors,
            typography = Typography,
            shapes = Shapes,
            content = content
        )
    }
}