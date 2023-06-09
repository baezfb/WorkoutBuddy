package com.example.workout_logger_presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hbaez.core_ui.LocalSpacing
import com.hbaez.core.R

@Composable
fun AddButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.onPrimary,
    borderColor: Color = color,
    icon: ImageVector? = null
) {
    val spacing = LocalSpacing.current
    if(icon != null){
        Row(
            modifier = modifier
                .clip(RoundedCornerShape(100f))
                .clickable { onClick() }
                .border(
                    width = 1.dp,
                    color = borderColor,
                    shape = RoundedCornerShape(100f)
                )
                .padding(spacing.spaceMedium),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = stringResource(id = R.string.add),
                tint = color
            )
            Spacer(modifier = Modifier.width(spacing.spaceMedium))
            Text(
                text = text,
                maxLines = 2,
                style = MaterialTheme.typography.button,
                color = color
            )
        }
    }
    else {
        Row(
            modifier = modifier
                .clip(RoundedCornerShape(100f))
                .clickable { onClick() }
                .border(
                    width = 1.dp,
                    color = borderColor,
                    shape = RoundedCornerShape(100f)
                )
                .padding(spacing.spaceMedium),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                maxLines = 2,
                style = MaterialTheme.typography.button,
                color = color
            )
        }
    }
}