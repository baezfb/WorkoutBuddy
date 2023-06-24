package com.hbaez.workoutbuddy.workout.start_workout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.hbaez.core_ui.LocalSpacing

@Composable
fun TimerScreen(
    seconds: Int,
    exerciseName: String,
    currentSet: Int,
    totalSet: Int
){
    val spacing = LocalSpacing.current

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = exerciseName,
            style = MaterialTheme.typography.caption2,
            modifier = Modifier.padding(bottom = spacing.spaceSmall)
        )
        Text(
            text = "$currentSet of $totalSet finished",
            style = MaterialTheme.typography.body1,
            modifier = Modifier.padding(bottom = spacing.spaceMedium)
        )
        Text(
            text = formatSeconds(seconds),
            style = MaterialTheme.typography.display1
        )
    }
}

private fun formatSeconds(seconds: Int): String {
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return "%02d:%02d".format(minutes, remainingSeconds)
}