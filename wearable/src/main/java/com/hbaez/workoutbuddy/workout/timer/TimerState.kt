package com.hbaez.workoutbuddy.workout.timer

import java.util.Date

data class TimerState(
    val startTime: Date = Date(),
    val endTime: Date = Date()
)

enum class TimerStatus {
    START, RUNNING, PAUSED, FINISHED
}