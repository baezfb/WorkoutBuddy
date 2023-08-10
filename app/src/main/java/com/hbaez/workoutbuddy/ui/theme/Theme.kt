package com.hbaez.workoutbuddy.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.hbaez.core_ui.*
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorScheme = darkColorScheme(
    primary = Red80,
    onPrimary = Red20,
    primaryContainer = Red30,
    onPrimaryContainer = Red90,
    inversePrimary = Red80,
    secondary = Orange80,
    onSecondary = Orange20,
    secondaryContainer = Orange30,
    onSecondaryContainer = Orange90,
    tertiary = DarkGreen80,
    onTertiary = DarkGreen20,
    tertiaryContainer = DarkGreen30,
    onTertiaryContainer = DarkGreen90,
    error = Red10,
    onError = Red20,
    errorContainer = Red10,
    onErrorContainer = Red20,
    background = MediumGray,
    onBackground = Grey90,
    surface = Grey10,
    onSurface = Grey80,
    inverseSurface = Grey90,
    inverseOnSurface = Grey20,
    surfaceVariant = RedGrey30,
    onSurfaceVariant = RedGrey80,
    outline = RedGrey60
)

private val LightColorScheme = lightColorScheme(
    primary = Red40,
    onPrimary = Color.White,
    primaryContainer = Red90,
    onPrimaryContainer = Red10,
    inversePrimary = Red80,
    secondary = Orange40,
    onSecondary = Color.White,
    secondaryContainer = Orange90,
    onSecondaryContainer = Orange10,
    tertiary = DarkGreen40,
    onTertiary = Color.Black,
    tertiaryContainer = DarkGreen90,
    onTertiaryContainer = DarkGreen10,
    error = Red10,
    onError = Color.White,
    errorContainer = Red90,
    onErrorContainer = Red10,
    background = Grey99,
    onBackground = Grey10,
    surface = Grey99,
    onSurface = Grey10,
    inverseSurface = Grey20,
    inverseOnSurface = Grey95,
    surfaceVariant = RedGrey90,
    onSurfaceVariant = RedGrey30,
    outline = RedGrey50
)

@Composable
fun WorkoutBuddyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable() () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }
//    val systemUiController = rememberSystemUiController()
//    systemUiController.setSystemBarsColor(
//        color = Color.Black
//    )
    CompositionLocalProvider(LocalSpacing provides Dimensions()) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            shapes = Shapes,
            content = content
        )
    }
}