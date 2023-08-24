package com.hbaez.workoutbuddy.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun WearText(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onPrimary,
    text: String,
    style: TextStyle = MaterialTheme.typography.displaySmall,
    maxLines: Int = 1,
    overflow: TextOverflow = TextOverflow.Clip
) {
    Text(
        modifier = modifier,
        textAlign = TextAlign.Center,
        maxLines = maxLines,
        color = color,
        text = text,
        style = style,
        overflow = overflow
    )
}