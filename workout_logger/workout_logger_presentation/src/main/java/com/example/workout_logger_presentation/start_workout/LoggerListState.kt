package com.example.workout_logger_presentation.start_workout

import androidx.compose.ui.graphics.Color

data class LoggerListState(
    val id: Int = 0,
    val exerciseName: String = "",
    val exerciseId: Int?,
    val timerStatus: TimerStatus = TimerStatus.START,
    val sets: String = "",
    val rest: List<String> = emptyList(),
    val reps: List<String> = emptyList(),
    val weight: List<String> = emptyList(),
    val isCompleted: List<Boolean> = List(0) { false },
    val checkedColor: List<Color> = List(1) { Color.DarkGray }
)
