package com.example.workout_logger_presentation.workout_logger_overview

import com.example.workout_logger_presentation.search_exercise.TrackableExerciseState
import com.hbaez.user_auth_presentation.model.CompletedWorkout


sealed class WorkoutLoggerOverviewEvent {
    object OnNextDayClick: WorkoutLoggerOverviewEvent()
    object OnPreviousDayClick: WorkoutLoggerOverviewEvent()
    object OnStartWorkoutClick: WorkoutLoggerOverviewEvent()
    data class OnCompletedWorkoutClick(val index: Int): WorkoutLoggerOverviewEvent()
    data class OnExerciseSearch(val filterText: String): WorkoutLoggerOverviewEvent()
    data class OnExerciseItemClick(val trackableExerciseState: TrackableExerciseState): WorkoutLoggerOverviewEvent()
    data class OnExerciseDescrClick(val trackableExerciseState: TrackableExerciseState): WorkoutLoggerOverviewEvent()
    data class OnChooseExercise(val trackableExerciseState: TrackableExerciseState): WorkoutLoggerOverviewEvent()
    data class OnDateClick(val year: Int, val dayOfMonth: Int, val month: Int): WorkoutLoggerOverviewEvent()
}
