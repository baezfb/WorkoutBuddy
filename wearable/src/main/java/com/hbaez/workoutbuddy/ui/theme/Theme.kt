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
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.hbaez.core_ui.Dimensions
import com.hbaez.core_ui.LocalSpacing
import com.hbaez.core_ui.MediumGray

private val DarkColorScheme = darkColorScheme(
    primary = com.hbaez.core_ui.Red80,
    onPrimary = com.hbaez.core_ui.Red20,
    primaryContainer = com.hbaez.core_ui.Red30,
    onPrimaryContainer = com.hbaez.core_ui.Red90,
    inversePrimary = com.hbaez.core_ui.Red80,
    secondary = com.hbaez.core_ui.Orange80,
    onSecondary = com.hbaez.core_ui.Orange20,
    secondaryContainer = com.hbaez.core_ui.Orange30,
    onSecondaryContainer = com.hbaez.core_ui.Orange90,
    tertiary = com.hbaez.core_ui.DarkGreen80,
    onTertiary = com.hbaez.core_ui.DarkGreen20,
    tertiaryContainer = com.hbaez.core_ui.DarkGreen30,
    onTertiaryContainer = com.hbaez.core_ui.DarkGreen90,
    error = com.hbaez.core_ui.Red10,
    onError = com.hbaez.core_ui.Red20,
    errorContainer = com.hbaez.core_ui.Red10,
    onErrorContainer = com.hbaez.core_ui.Red20,
    background = MediumGray,
    onBackground = com.hbaez.core_ui.Grey90,
    surface = com.hbaez.core_ui.Grey10,
    onSurface = com.hbaez.core_ui.Grey80,
    inverseSurface = com.hbaez.core_ui.Grey90,
    inverseOnSurface = com.hbaez.core_ui.Grey20,
    surfaceVariant = com.hbaez.core_ui.RedGrey30,
    onSurfaceVariant = com.hbaez.core_ui.RedGrey80,
    outline = com.hbaez.core_ui.RedGrey60
)

private val LightColorScheme = lightColorScheme(
    primary = com.hbaez.core_ui.Red40,
    onPrimary = Color.White,
    primaryContainer = com.hbaez.core_ui.Red90,
    onPrimaryContainer = com.hbaez.core_ui.Red10,
    inversePrimary = com.hbaez.core_ui.Red80,
    secondary = com.hbaez.core_ui.Orange40,
    onSecondary = Color.White,
    secondaryContainer = com.hbaez.core_ui.Orange90,
    onSecondaryContainer = com.hbaez.core_ui.Orange10,
    tertiary = com.hbaez.core_ui.DarkGreen40,
    onTertiary = Color.Black,
    tertiaryContainer = com.hbaez.core_ui.DarkGreen90,
    onTertiaryContainer = com.hbaez.core_ui.DarkGreen10,
    error = com.hbaez.core_ui.Red10,
    onError = Color.White,
    errorContainer = com.hbaez.core_ui.Red90,
    onErrorContainer = com.hbaez.core_ui.Red10,
    background = com.hbaez.core_ui.Grey99,
    onBackground = com.hbaez.core_ui.Grey10,
    surface = com.hbaez.core_ui.Grey99,
    onSurface = com.hbaez.core_ui.Grey10,
    inverseSurface = com.hbaez.core_ui.Grey20,
    inverseOnSurface = com.hbaez.core_ui.Grey95,
    surfaceVariant = com.hbaez.core_ui.RedGrey90,
    onSurfaceVariant = com.hbaez.core_ui.RedGrey30,
    outline = com.hbaez.core_ui.RedGrey50
)

@Composable
fun WorkoutBuddyWearableTheme(
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