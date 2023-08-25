package com.example.workout_logger_presentation.workout_logger_overview.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun ExerciseRow(
    set: Int,
    reps: Int?,
    weight: Double?,
    completed: Boolean,
    enabled: Boolean,
    onRepsChange: (String) -> Unit,
    onWeightChange: (String) -> Unit,
    onCompletedChange: (Boolean) -> Unit
){
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text= set.toString(),
            style = MaterialTheme.typography.bodyLarge
        )
        TextField(
            modifier = Modifier.weight(1f),
            value = weight?.toString() ?: "",
            enabled = enabled,
            textStyle = MaterialTheme.typography.bodyLarge,
            colors = TextFieldDefaults.colors(
                disabledContainerColor = MaterialTheme.colorScheme.inversePrimary,
                unfocusedContainerColor = MaterialTheme.colorScheme.inversePrimary,
                focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            ),
            onValueChange = {
                val filteredValue = it.filter { tmp -> tmp != '-' } // Filter out non-digit characters
                onWeightChange(filteredValue)
            },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onDone = {
                    defaultKeyboardAction(ImeAction.Next)
                }
            )

        )
        TextField(
            modifier = Modifier.weight(1f),
            value = reps?.toString() ?: "",
            enabled = enabled,
            textStyle = MaterialTheme.typography.bodyLarge,
            colors = TextFieldDefaults.colors(
                disabledContainerColor = MaterialTheme.colorScheme.inversePrimary,
                unfocusedContainerColor = MaterialTheme.colorScheme.inversePrimary,
                focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            ),
            onValueChange = {
                val filteredValue = it.filter { tmp -> !(tmp == '.' || tmp == '-') && tmp.isDigit() } // Filter out non-digit characters
                onRepsChange(filteredValue)
            },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onDone = {
                    defaultKeyboardAction(ImeAction.Next)
                }
            )
        )
        Checkbox(
            modifier = Modifier.weight(1f),
            checked = completed,
            onCheckedChange = {
                onCompletedChange(it)
            },
            enabled = enabled
        )
    }
}