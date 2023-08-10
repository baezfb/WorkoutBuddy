package com.hbaez.user_auth_presentation.components

import androidx.annotation.StringRes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource

@Composable
fun BasicTextButton(@StringRes text: Int, modifier: Modifier, action: () -> Unit) {
    TextButton(onClick = action, modifier = modifier) {
        Text(
            text = stringResource(text),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = MaterialTheme.typography.displayLarge.fontWeight,
            fontSize = MaterialTheme.typography.headlineMedium.fontSize,
            letterSpacing = MaterialTheme.typography.bodyMedium.letterSpacing,
        )
    }
}