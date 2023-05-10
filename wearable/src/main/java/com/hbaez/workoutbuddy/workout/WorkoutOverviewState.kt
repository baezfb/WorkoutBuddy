package com.hbaez.workoutbuddy.workout

import java.time.LocalDate

data class WorkoutOverviewState(
    val date: LocalDate = LocalDate.now(),
    val workoutNames: MutableList<String> = mutableListOf(),
    val workoutId: MutableList<Int> = mutableListOf()
)
