package com.example.workout_logger_presentation.create_workout

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.hbaez.core_ui.LocalSpacing

@Composable
fun CreateWorkoutTableRow(
    onNameChange: (String) -> Unit,
    onSetsChange: (String) -> Unit,
    onRepsChange: (String) -> Unit,
    onRestChange: (String) -> Unit,
    onWeightChange: (String) -> Unit,
    name: String,
    sets: String,
    reps: String,
    rest: String,
    weight: String,
    isRevealed: Boolean,
    hasExercise: Boolean
){
    val spacing = LocalSpacing.current

    Spacer(modifier = Modifier.height(spacing.spaceMedium))
    Row(
        modifier = Modifier.background(MaterialTheme.colors.background)
    ){
        EditTableCell(
            isRevealed = isRevealed,
            hasExercise = hasExercise,
            text = name,
            weight = .32f,
            keyboardType = KeyboardType.Text,
            onValueChange = {
                onNameChange(it)
            }
        )
        EditTableCell(
            isRevealed = isRevealed,
            text = sets,
            weight = .16f,
            keyboardType = KeyboardType.Number,
            onValueChange = {
                onSetsChange(it)
            }
        )
        EditTableCell(
            isRevealed = isRevealed,
            text = reps,
            weight = .16f,
            keyboardType = KeyboardType.Number,
            onValueChange = {
                onRepsChange(it)
            }
        )
        EditTableCell(
            isRevealed = isRevealed,
            text = rest,
            weight = .18f,
            keyboardType = KeyboardType.Number,
            onValueChange = {
                onRestChange(it)
            }
        )
        EditTableCell(
            isRevealed = isRevealed,
            text = weight,
            weight = .18f,
            keyboardType = KeyboardType.Number,
            onValueChange = {
                onWeightChange(it)
            }
        )

    }
    Spacer(modifier = Modifier.height(spacing.spaceMedium))
}

@Composable
fun RowScope.EditTableCell(
    isRevealed: Boolean,
    hasExercise: Boolean = false,
    text: String,
    weight: Float,
    keyboardType: KeyboardType,
    onValueChange: (String) -> Unit
){
    OutlinedTextField(
        value = text,
        onValueChange = onValueChange,
        singleLine = true,
        modifier = Modifier
            .border(1.dp, MaterialTheme.colors.onBackground)
            .background(MaterialTheme.colors.surface)
            .weight(weight)
            .fillMaxWidth(),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next, keyboardType = keyboardType),
        keyboardActions = KeyboardActions(
            onNext = {
                defaultKeyboardAction(ImeAction.Next)
            }

        ),
        enabled = !isRevealed && !hasExercise
    )
}