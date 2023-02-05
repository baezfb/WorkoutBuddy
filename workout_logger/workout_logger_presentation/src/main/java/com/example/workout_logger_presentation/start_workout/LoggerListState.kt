package com.example.workout_logger_presentation.start_workout

import androidx.compose.ui.graphics.Color
import com.example.workout_logger_domain.model.TrackedExercise

data class LoggerListState(
    val id: Int = 0,
    val exerciseName: String = "",
    val exerciseId: Int?,
    val timerStatus: TimerStatus = TimerStatus.START,
    val checkedColor: List<List<Color>> = List(1) { List(1) { Color.DarkGray } }
)
