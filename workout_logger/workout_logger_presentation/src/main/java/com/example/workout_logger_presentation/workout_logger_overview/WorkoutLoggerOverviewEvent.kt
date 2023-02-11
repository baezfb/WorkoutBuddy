package com.example.workout_logger_presentation.workout_logger_overview

import com.hbaez.user_auth_presentation.model.CompletedWorkout


sealed class WorkoutLoggerOverviewEvent {
    object OnNextDayClick: WorkoutLoggerOverviewEvent()
    object OnPreviousDayClick: WorkoutLoggerOverviewEvent()
    object OnStartWorkoutClick: WorkoutLoggerOverviewEvent()
    data class OnCompletedWorkoutClick(val index: Int): WorkoutLoggerOverviewEvent()
}
