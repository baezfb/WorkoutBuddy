package com.example.workout_logger_presentation.workout_logger_overview.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun ExerciseRow(
    set: Int,
    reps: Int,
    weight: Double,
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
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text= weight.toString(),
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text= reps.toString(),
            style = MaterialTheme.typography.bodyMedium
        )
        Checkbox(
//            modifier = Modifier.weight(1f),
            checked = completed,
            onCheckedChange = { },
            enabled = false
        )
    }
}