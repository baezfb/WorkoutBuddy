package com.hbaez.user_auth_presentation.components

import androidx.annotation.StringRes
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hbaez.core_ui.LocalSpacing

@Composable
fun BasicButton(@StringRes text: Int, modifier: Modifier, action: () -> Unit) {
    val spacing = LocalSpacing.current
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(100f))
            .clickable { action() }
            .border(
                width = 1.dp,
                color = MaterialTheme.colors.onPrimary,
                shape = RoundedCornerShape(100f)
            )
            .padding(spacing.spaceMedium),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
//        colors =
//        ButtonDefaults.run {
//            buttonColors(
//                backgroundColor = MaterialTheme.colors.primary,
//                contentColor = MaterialTheme.colors.onPrimary
//            )
//        }
    ) {
        Text(
            text = stringResource(text),
            style = MaterialTheme.typography.button,
            color = MaterialTheme.colors.onPrimary
        )
    }
}