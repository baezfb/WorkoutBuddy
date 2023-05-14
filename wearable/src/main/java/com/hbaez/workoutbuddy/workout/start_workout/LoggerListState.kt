package com.hbaez.workoutbuddy.workout.start_workout

import androidx.compose.ui.graphics.Color

data class LoggerListState(
    val id: Int = 0,
    val exerciseName: String = "",
    val exerciseId: Int?,
    val timerStatus: TimerStatus = TimerStatus.START,
    val sets: String = "",
    val rest: String = "",
    val repsList: List<String> = List(0) { "" },
    val weightList: List<String> = List(0) { "" },
    val isCompleted: List<Boolean> = List(0) { false },
    val checkedColor: List<Color> = List(1) { Color.DarkGray },
    val origRest: String = "",
    val origReps: String = "",
    val origWeight: String = ""
)
