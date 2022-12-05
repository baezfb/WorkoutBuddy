package com.example.workout_logger_presentation.workout_logger_overview

import com.example.workout_logger_domain.model.CompletedWorkout

sealed class WorkoutLoggerOverviewEvent {
    object OnNextDayClick: WorkoutLoggerOverviewEvent()
    object OnPreviousDayClick: WorkoutLoggerOverviewEvent()
    object OnStartWorkoutClick: WorkoutLoggerOverviewEvent()
    data class OnCompletedWorkoutClick(val completedWorkout: CompletedWorkout): WorkoutLoggerOverviewEvent()
}
