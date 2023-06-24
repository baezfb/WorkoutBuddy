package com.hbaez.workoutbuddy.workout.timer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.material.Vignette
import androidx.wear.compose.material.VignettePosition
import androidx.wear.compose.material.scrollAway
import com.hbaez.core_ui.LocalSpacing
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.Duration
import java.util.Date
import kotlin.time.Duration.Companion.seconds

@Composable
fun TimerScreen(
    seconds: Int,
    exerciseName: String,
    currentSet: Int,
    totalSet: Int,
    viewModel: TimerViewModel = hiltViewModel()
){
    val spacing = LocalSpacing.current
    val state = viewModel.state
    val context = LocalContext.current
    val timerRunning = remember { mutableStateOf(false) }
    val remainingSeconds = remember { mutableStateOf(seconds) }

    LaunchedEffect(key1 = Unit){
        val wakeupTime = TimerViewModel.setAlarm(context, Duration.ofSeconds(seconds.toLong()))
        NotificationUtil.showTimerRunning(context, wakeupTime)
        timerRunning.value = true
    }

    DisposableEffect(key1 = Unit) {
        // Cleanup logic when the composable is disposed (when user swipes back)
        onDispose {
            NotificationUtil.hideTimerNotification(context)
        }
    }

    LaunchedEffect(true) {
        if(timerRunning.value){
            while (remainingSeconds.value > 0) {
                delay(1000) // Wait for 1 second
                remainingSeconds.value = ((state.endTime.time / 1000).toInt() - (System.currentTimeMillis() / 1000).toInt()).coerceAtLeast(0)
            }
            if(remainingSeconds.value == 0){
                timerRunning.value = false
                NotificationUtil.hideTimerNotification(context)
            }
        }
    }


    Scaffold(
        timeText = {
            TimeText()
        },
        modifier = Modifier
            .background(color = MaterialTheme.colors.background),
    ) {
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
                text = formatSeconds(remainingSeconds.value),
                style = MaterialTheme.typography.display1
            )
        }
    }
}

private fun formatSeconds(seconds: Int): String {
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return "%02d:%02d".format(minutes, remainingSeconds)
}