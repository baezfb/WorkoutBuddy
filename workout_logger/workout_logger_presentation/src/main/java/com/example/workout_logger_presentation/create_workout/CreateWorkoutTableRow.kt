package com.example.workout_logger_presentation.create_workout

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.hbaez.core_ui.LocalSpacing
import com.hbaez.core.R

@Composable
fun CreateWorkoutTableRow(
    onRepsChange: (String) -> Unit,
    onWeightChange: (String) -> Unit,
    reps: String,
    weight: String,
    hasExercise: Boolean
){
    val spacing = LocalSpacing.current
    Row(
        verticalAlignment = Alignment.CenterVertically
//        modifier = Modifier.border(2.dp, MaterialTheme.colorScheme.onPrimaryContainer, RectangleShape)
    ){
        EditTableCell(
            label = stringResource(id = R.string.weight),
            text = weight,
            weight = .25f,
            keyboardType = KeyboardType.Number,
            onValueChange = {
                val filteredValue = it.filter { tmp -> tmp != '-' } // Filter out non-digit characters
                onWeightChange(filteredValue)
            },
            borderColor = MaterialTheme.colorScheme.primaryContainer,
            backgroundColor = Color.Transparent
        )
        EditTableCell(
            label = stringResource(id = R.string.reps),
            text = reps,
            weight = .20f,
            keyboardType = KeyboardType.Number,
            onValueChange = {
                val filteredValue = it.filter { tmp -> !(tmp == '.' || tmp == '-') } // Filter out non-digit characters
                onRepsChange(filteredValue)
            },
            borderColor = MaterialTheme.colorScheme.primaryContainer,
            backgroundColor = Color.Transparent
        )

    }
}

@Composable
fun RowScope.EditTableCell(
    label: String,
    hasExercise: Boolean = false,
    text: String,
    weight: Float,
    keyboardType: KeyboardType,
    onValueChange: (String) -> Unit,
    borderColor: Color = MaterialTheme.colorScheme.onBackground,
    backgroundColor: Color = MaterialTheme.colorScheme.surface
){
    OutlinedTextField(
        label = { Text(text = label) },
        value = text,
        onValueChange = onValueChange,
        singleLine = true,
        modifier = Modifier
            .border(2.dp, borderColor)
            .background(backgroundColor)
            .weight(weight)
            .fillMaxWidth(),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next, keyboardType = keyboardType),
        keyboardActions = KeyboardActions(
            onNext = {
                defaultKeyboardAction(ImeAction.Next)
            }

        ),
//        enabled = !isRevealed && !hasExercise
    )
}