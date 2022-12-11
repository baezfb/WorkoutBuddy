package com.hbaez.user_auth_presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.hbaez.core.R
import com.hbaez.core_ui.LocalSpacing

@Composable
fun EmailField(value: String, onNewValue: (String) -> Unit, modifier: Modifier = Modifier) {
    val spacing = LocalSpacing.current
    Column(
        modifier = modifier
    ) {
        Text(text = stringResource(id = R.string.email), style = MaterialTheme.typography.body1)
        Spacer(modifier = Modifier.height(spacing.spaceSmall))
        OutlinedTextField(
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            value = value,
            onValueChange = { onNewValue(it) },
            placeholder = { Text(stringResource(R.string.email)) },
            leadingIcon = { Icon(imageVector = Icons.Default.Email, contentDescription = "Email") }
        )
    }
}