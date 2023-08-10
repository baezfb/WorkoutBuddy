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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.hbaez.core_ui.LocalSpacing
import com.hbaez.core.R

@Composable
fun CreateWorkoutTableRow(
    onRepsChange: (String) -> Unit,
    onRestChange: (String) -> Unit,
    onWeightChange: (String) -> Unit,
    sets: String,
    reps: String,
    rest: String,
    weight: String,
    hasExercise: Boolean
){
    val spacing = LocalSpacing.current

    Spacer(modifier = Modifier.height(spacing.spaceMedium))
    Row(
        modifier = Modifier.background(MaterialTheme.colorScheme.surface)
    ){
        Text(
            text = (sets.toInt() + 1).toString(),
            style = MaterialTheme.typography.displaySmall,
            modifier = Modifier.padding(spacing.spaceSmall)
        )
        EditTableCell(
            label = stringResource(id = R.string.reps),
            text = reps,
            weight = .20f,
            keyboardType = KeyboardType.Number,
            onValueChange = {
                onRepsChange(it)
            },
            borderColor = MaterialTheme.colorScheme.background,
            backgroundColor = MaterialTheme.colorScheme.background
        )
        EditTableCell(
            label = stringResource(id = R.string.rest),
            text = rest,
            weight = .25f,
            keyboardType = KeyboardType.Number,
            onValueChange = {
                onRestChange(it)
            },
            borderColor = MaterialTheme.colorScheme.background,
            backgroundColor = MaterialTheme.colorScheme.background
        )
        EditTableCell(
            label = stringResource(id = R.string.weight),
            text = weight,
            weight = .25f,
            keyboardType = KeyboardType.Number,
            onValueChange = {
                onWeightChange(it)
            },
            borderColor = MaterialTheme.colorScheme.background,
            backgroundColor = MaterialTheme.colorScheme.background
        )

    }
    Spacer(modifier = Modifier.height(spacing.spaceMedium))
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