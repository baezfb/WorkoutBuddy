package com.hbaez.user_auth_presentation.components

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hbaez.core_ui.LocalSpacing
import com.hbaez.core.R

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

@Composable
fun Button(@StringRes text: Int, modifier: Modifier, action: () -> Unit) {
    /**
     * TODO: Add the ability to change the color of the outline
     * TODO: Add the ability to add an icon
     */
    val spacing = LocalSpacing.current
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(15.dp))
            .background(Color.White)
            .clickable { action() }
            .padding(spacing.spaceSmall)
            .fillMaxWidth(1f),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(text),
            style = MaterialTheme.typography.button,
            color = MaterialTheme.colors.primary
        )
    }
}


@Composable
fun FlatButton(@StringRes text: Int, modifier: Modifier, action: () -> Unit) {
    /**
     * TODO: Add the ability to change the color of the outline
     * TODO: Add the ability to add an icon
     */
    val spacing = LocalSpacing.current
    Row(
        modifier = modifier
            .background(Color.Transparent)
            .clickable { action() }
            .border(1.dp, MaterialTheme.colors.onPrimary, RoundedCornerShape(15.dp))
            .padding(spacing.spaceSmall)
            .fillMaxWidth(1f),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(text),
            style = MaterialTheme.typography.button,
            color = Color.White
        )
    }
}