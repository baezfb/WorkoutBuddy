package com.hbaez.tracker_presentation.tracker_overview.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hbaez.core.R
import com.hbaez.core_ui.LocalSpacing

@Composable
fun IconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onBackground,
    borderColor: Color = Color.Transparent,
    icon: ImageVector,
    padding: Dp = LocalSpacing.current.spaceMedium
) {
    Icon(
        imageVector = icon,
        contentDescription = stringResource(id = R.string.add),
        tint = color,
        modifier = modifier
            .clip(RoundedCornerShape(100f))
            .clickable { onClick() }
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(100f)
            )
            .padding(padding)
    )
}