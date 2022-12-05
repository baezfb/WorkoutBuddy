package com.example.workout_logger_presentation.workout_logger_overview.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.hbaez.workout_logger_presentation.R

@Composable
fun ExerciseRow(
    set: Int,
    reps: Int,
    weight: Int,
    completed: Boolean
){
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text= set.toString(),
            style = MaterialTheme.typography.body2
        )
        Text(
            text= weight.toString(),
            style = MaterialTheme.typography.body2
        )
        Text(
            text= reps.toString(),
            style = MaterialTheme.typography.body2
        )
        Checkbox(
//            modifier = Modifier.weight(1f),
            checked = completed,
            onCheckedChange = { },
            enabled = false
        )
    }
}