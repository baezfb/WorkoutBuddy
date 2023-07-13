package com.example.workout_logger_presentation.start_workout.components

import android.content.Context
import android.os.Build
import android.os.VibratorManager
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.annotation.ExperimentalCoilApi
import com.example.workout_logger_presentation.start_workout.StartWorkoutEvent
import com.example.workout_logger_presentation.start_workout.StartWorkoutViewModel
import com.example.workout_logger_presentation.start_workout.TimerStatus
import com.hbaez.core.R
import com.hbaez.core_ui.LocalSpacing
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Math.PI
import java.time.Duration
import java.util.Date
import java.util.concurrent.TimeUnit
import kotlin.math.cos
import kotlin.math.sin

@RequiresApi(Build.VERSION_CODES.S)
@ExperimentalCoilApi
@Composable
fun Timer(
    handleColor: Color,
    inactiveBarColor: Color,
    activeBarColor: Color,
    timerJump: Long,
    modifier: Modifier = Modifier,
    initialValue: Float = 1f,
    strokeWidth: Dp = 5.dp,
    viewModel: StartWorkoutViewModel = hiltViewModel()
    ){

    val haptic = LocalHapticFeedback.current
    val context = LocalContext.current
    val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
    val vibrator = vibratorManager.defaultVibrator
    val spacing = LocalSpacing.current

    val state = viewModel.state
    val currentTime by derivedStateOf { viewModel.currentTime }

    var size by remember {
        mutableStateOf(IntSize.Zero)
    }
    val angleRatio = remember {
        Animatable(1f)
    }
    LaunchedEffect(key1 = currentTime, key2 = state.timerStatus) {
        angleRatio.animateTo(
            targetValue = if (state.timeDuration.seconds > 0 && state.timerStatus == TimerStatus.RUNNING) {
                (currentTime / 1000L) / (state.timeDuration.seconds).toFloat()
            } else 1f,
            animationSpec = tween(
                durationMillis = 300
            )
        )
    }
    LaunchedEffect(key1 = currentTime, key2 = state.timerStatus, block = {
        CoroutineScope(Dispatchers.IO).launch {
            if(state.timerStatus == TimerStatus.RUNNING && currentTime > 0){
                delay(100)
                viewModel.onEvent(StartWorkoutEvent.ChangeRemainingTime)
            }
            if(currentTime <= 0L){
                viewModel.onEvent(StartWorkoutEvent.ChangeCheckboxColor(Color.DarkGray, state.currRunningId, state.currRunningIndex))
                viewModel.onEvent(StartWorkoutEvent.TimerFinished)
                NotificationUtil.hideTimerNotification(context)
            }
        }
    })
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .onSizeChanged {
                size = it
            },
    ) {
        // draw the timer
        Canvas(modifier = modifier) {
            // draw the inactive arc with following parameters
            drawArc(
                color = inactiveBarColor, // assign the color
                startAngle = -215f, // assign the start angle
                sweepAngle = 250f, // arc angles
                useCenter = false, // prevents our arc to connect at te ends
                size = Size(size.width.toFloat(), size.height.toFloat()),

                // to make ends of arc round
                style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round)
            )
            // draw the active arc with following parameters
            drawArc(
                color = activeBarColor, // assign the color
                startAngle = -215f,  // assign the start angle
                sweepAngle = 250f * angleRatio.value, // reduce the sweep angle
                // with the current value
                useCenter = false, // prevents our arc to connect at te ends
                size = Size(size.width.toFloat(), size.height.toFloat()),

                // to make ends of arc round
                style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round)
            )
            // calculate the value from arc pointer position
            val center = Offset(size.width / 2f, size.height / 2f)
            val beta = (250f * angleRatio.value + 145f) * (PI / 180f).toFloat()
            val r = size.width / 2f
            val a = cos(beta) * r
            val b = sin(beta) * r
            // draw the circular pointer/ cap
            drawPoints(
                listOf(Offset(center.x + a, center.y + b)),
                pointMode = PointMode.Points,
                color = handleColor,
                strokeWidth = (strokeWidth * 3f).toPx(),
                cap = StrokeCap.Round  // make the pointer round
            )
        }
        // add value of the timer
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = if (state.timerStatus == TimerStatus.RUNNING){ formatTime(currentTime / 1000L) } else { stringResource(R.string.null_time) },
                fontSize = 44.sp,
                fontWeight = FontWeight.Bold,
                color = if (state.timerStatus == TimerStatus.RUNNING){ Color.White } else { Color.Gray }
            )
            Spacer(modifier = Modifier.width(spacing.spaceMedium))
            Row {
                Text(
                    text = "- ${timerJump}s",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Black,
                    color = if(state.timerStatus == TimerStatus.RUNNING && Duration.ofMillis(currentTime) > Duration.ofSeconds(timerJump)) MaterialTheme.colors.primaryVariant else Color.Gray,
                    modifier = Modifier.clickable(enabled = (state.timerStatus == TimerStatus.RUNNING && Duration.ofMillis(currentTime) > Duration.ofSeconds(timerJump))) {
                        StartWorkoutViewModel.removeAlarm(context)
                        NotificationUtil.hideTimerNotification(context)
                        val remainingTime = state.timeDuration.toMillis() - (Date().time - state.startTime.time)
                        val wakeupTime = StartWorkoutViewModel.setAlarm(context = context, timeDuration = Duration.ofMillis(remainingTime) - Duration.ofSeconds(timerJump))
                        NotificationUtil.showTimerRunning(context, wakeupTime)
                        viewModel.onEvent(StartWorkoutEvent.OnTimeJump(false, timerJump))
                    }
                        .padding(spacing.spaceSmall)
                )
                Spacer(modifier = Modifier.width(spacing.spaceLarge))
                Text(
                    text = "+ ${timerJump}s",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Black,
                    color = if(state.timerStatus == TimerStatus.RUNNING) MaterialTheme.colors.primaryVariant else Color.Gray,
                    modifier = Modifier.clickable(enabled = state.timerStatus == TimerStatus.RUNNING) {
                        StartWorkoutViewModel.removeAlarm(context)
                        NotificationUtil.hideTimerNotification(context)
                        val remainingTime = state.timeDuration.toMillis() - (Date().time - state.startTime.time)
                        val wakeupTime = StartWorkoutViewModel.setAlarm(context = context, timeDuration = Duration.ofMillis(remainingTime) + Duration.ofSeconds(timerJump))
                        NotificationUtil.showTimerRunning(context, wakeupTime)
                        viewModel.onEvent(StartWorkoutEvent.OnTimeJump(true, timerJump))
                    }
                        .padding(spacing.spaceSmall)
                )
            }
        }
    }
}

fun formatTime(currentTime: Long): String{
    val minutes = TimeUnit.MILLISECONDS.toMinutes(currentTime * 1000).toString().padStart(2,'0')
    val seconds = (TimeUnit.MILLISECONDS.toSeconds(currentTime * 1000)%60).toString().padStart(2,'0')
    return "${minutes}:${seconds}"
}