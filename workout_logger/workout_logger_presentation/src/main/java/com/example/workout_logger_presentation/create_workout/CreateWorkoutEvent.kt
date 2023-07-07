package com.example.workout_logger_presentation.create_workout

import com.example.workout_logger_presentation.search_exercise.SearchExerciseEvent
import com.example.workout_logger_presentation.search_exercise.TrackableExerciseState

sealed class CreateWorkoutEvent {

    data class OnWorkoutNameChange(val name: String): CreateWorkoutEvent()

    data class OnWorkoutNameFocusChange(val isFocused: Boolean): CreateWorkoutEvent()

    data class OnTrackableExerciseUiRepsChange(val reps: String, val page: Int, val index: Int): CreateWorkoutEvent()

    data class OnTrackableExerciseUiRestChange(val rest: String, val page: Int, val index: Int): CreateWorkoutEvent()

    data class OnTrackableExerciseUiWeightChange(val weight: String, val page: Int, val index: Int): CreateWorkoutEvent()

    data class OnRemoveSetRow(val id: Int, val exerciseId: Int): CreateWorkoutEvent()

    object CheckTrackedExercise: CreateWorkoutEvent()

    data class OnCreateWorkout(val trackableExercise: List<TrackableExerciseUiState>, val workoutName: String, val lastUsedId: Int): CreateWorkoutEvent()

    data class OnUpdateWorkout(val trackableExercise: List<TrackableExerciseUiState>, val workoutName: String, val lastUsedId: Int): CreateWorkoutEvent()

    object AddPageCount: CreateWorkoutEvent()

    object SubtractPageCount: CreateWorkoutEvent()

    data class AddSet(val page: Int): CreateWorkoutEvent()

    data class OnRemovePage(val page: Int): CreateWorkoutEvent()

    data class GetExerciseInfo(val exerciseName: String): CreateWorkoutEvent()

    object GetAllExerciseInfo: CreateWorkoutEvent()

    data class OnToggleExerciseDescription(val exercise: TrackableExerciseState): CreateWorkoutEvent()

}
