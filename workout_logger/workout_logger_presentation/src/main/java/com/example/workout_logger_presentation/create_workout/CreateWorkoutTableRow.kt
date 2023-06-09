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
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
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
    isRevealed: Boolean,
    hasExercise: Boolean
){
    val spacing = LocalSpacing.current

    Spacer(modifier = Modifier.height(spacing.spaceMedium))
    Row(
        modifier = Modifier.background(MaterialTheme.colors.background)
    ){
        Text(
            text = sets,
            style = MaterialTheme.typography.h3,
            modifier = Modifier.padding(spacing.spaceSmall)
        )
//        EditTableCell(
//            isRevealed = isRevealed,
//            hasExercise = hasExercise,
//            text = name,
//            weight = .32f,
//            keyboardType = KeyboardType.Text,
//            onValueChange = {
//                onNameChange(it)
//            }
//        )
//        EditTableCell(
//            isRevealed = isRevealed,
//            text = sets,
//            weight = .16f,
//            keyboardType = KeyboardType.Number,
//            onValueChange = {
//                onSetsChange(it)
//            }
//        )
        EditTableCell(
            label = stringResource(id = R.string.reps),
            isRevealed = isRevealed,
            text = reps,
            weight = .20f,
            keyboardType = KeyboardType.Number,
            onValueChange = {
                onRepsChange(it)
            },
            borderColor = MaterialTheme.colors.background,
            backgroundColor = MaterialTheme.colors.background
        )
        EditTableCell(
            label = stringResource(id = R.string.rest),
            isRevealed = isRevealed,
            text = rest,
            weight = .25f,
            keyboardType = KeyboardType.Number,
            onValueChange = {
                onRestChange(it)
            },
            borderColor = MaterialTheme.colors.background,
            backgroundColor = MaterialTheme.colors.background
        )
        EditTableCell(
            label = stringResource(id = R.string.weight),
            isRevealed = isRevealed,
            text = weight,
            weight = .25f,
            keyboardType = KeyboardType.Number,
            onValueChange = {
                onWeightChange(it)
            },
            borderColor = MaterialTheme.colors.background,
            backgroundColor = MaterialTheme.colors.background
        )

    }
    Spacer(modifier = Modifier.height(spacing.spaceMedium))
}

@Composable
fun RowScope.EditTableCell(
    label: String,
    isRevealed: Boolean,
    hasExercise: Boolean = false,
    text: String,
    weight: Float,
    keyboardType: KeyboardType,
    onValueChange: (String) -> Unit,
    borderColor: Color = MaterialTheme.colors.onBackground,
    backgroundColor: Color = MaterialTheme.colors.surface
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
        enabled = !isRevealed && !hasExercise
    )
}