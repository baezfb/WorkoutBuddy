package com.hbaez.user_auth_presentation.components

import androidx.annotation.StringRes
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource

@Composable
fun BasicTextButton(@StringRes text: Int, modifier: Modifier, action: () -> Unit) {
    TextButton(onClick = action, modifier = modifier) {
        Text(
            text = stringResource(text),
            style = MaterialTheme.typography.body2,
            color = MaterialTheme.colors.primary,
            fontWeight = MaterialTheme.typography.h1.fontWeight,
            fontSize = MaterialTheme.typography.h4.fontSize,
            letterSpacing = MaterialTheme.typography.body2.letterSpacing,
        )
    }
}