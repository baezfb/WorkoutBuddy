package com.hbaez.user_auth_presentation.components

import androidx.annotation.StringRes
import androidx.compose.compiler.plugins.kotlin.ComposeFqNames.remember
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.hbaez.core.R
import com.hbaez.core_ui.LocalSpacing

@Composable
fun PasswordField(
    value: String,
    onNewValue: (String) -> Unit,
    modifier: Modifier = Modifier,
    @StringRes placeholder: Int
) {
    PasswordField(value, placeholder, onNewValue)
}

@Composable
private fun PasswordField(
    value: String,
    @StringRes placeholder: Int,
    onNewValue: (String) -> Unit
) {
    var isVisible by remember { mutableStateOf(false) }

    val visualTransformation =
        if (isVisible) VisualTransformation.None else PasswordVisualTransformation()

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = value,
        singleLine = true,
        onValueChange = { onNewValue(it) },
        label = { Text(
            text = stringResource(placeholder),
            fontSize = MaterialTheme.typography.titleLarge.fontSize,
            color = MaterialTheme.colorScheme.primary,
            ) },
        leadingIcon = { Icon(
            imageVector = Icons.Default.Lock,
            contentDescription = "Lock",
            tint = MaterialTheme.colorScheme.primary
        ) },
        trailingIcon = {
            IconButton(onClick = { isVisible = !isVisible }) {
                if (isVisible) Icon(imageVector = Icons.Default.Warning, contentDescription = "Visibility")
                    else Icon(imageVector = Icons.Default.List, contentDescription = "Visibility")
            }
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedTextColor = MaterialTheme.colorScheme.primary,
            unfocusedTextColor = MaterialTheme.colorScheme.primary,
        ),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation = visualTransformation
    )
}