package com.example.workout_logger_presentation.workout_logger_overview

import com.example.workout_logger_domain.model.CompletedWorkout
import com.example.workout_logger_presentation.search_exercise.TrackableExerciseState
import com.himanshoe.kalendar.KalendarEvents
import java.time.LocalDate

data class WorkoutLoggerOverviewState(
    val date: LocalDate = LocalDate.now(),
    val workoutNames: MutableList<String> = mutableListOf(),
    val workoutId: MutableList<Int> = mutableListOf(),
    val showWorkoutDialog: Boolean = false,
    val completedWorkouts: List<CompletedWorkout> = listOf(),
    val completedWorkoutIsExpanded: List<Boolean> = mutableListOf(),
    val exerciseFilterText: String = "",
    val trackableExercise: List<TrackableExerciseState> = emptyList()
)
