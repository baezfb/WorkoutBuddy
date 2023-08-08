package com.hbaez.user_auth_presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.hbaez.core.R
import com.hbaez.core_ui.LocalSpacing

@Composable
fun EmailField(value: String, onNewValue: (String) -> Unit, modifier: Modifier = Modifier) {
    OutlinedTextField(
        singleLine = true,
        value = value,
        label = { Text(
            stringResource(R.string.email),
            maxLines = 1,
            fontSize = MaterialTheme.typography.h6.fontSize,
            color = MaterialTheme.colors.primary,
        ) },
        onValueChange = { onNewValue(it) },
        leadingIcon = { Icon(
            imageVector = Icons.Default.Email,
            contentDescription = "Email",
            tint = MaterialTheme.colors.primary) },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MaterialTheme.colors.primary,
            unfocusedBorderColor = Color.Transparent,
            textColor = MaterialTheme.colors.primary,
        ),
        modifier = modifier,
    )
}