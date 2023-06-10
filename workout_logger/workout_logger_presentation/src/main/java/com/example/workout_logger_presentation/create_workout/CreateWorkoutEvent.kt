package com.example.workout_logger_presentation.create_workout

sealed class CreateWorkoutEvent {

    object OnAddExercise: CreateWorkoutEvent()

    data class OnWorkoutNameChange(val name: String): CreateWorkoutEvent()

    data class OnWorkoutNameFocusChange(val isFocused: Boolean): CreateWorkoutEvent()

    data class OnTrackableExerciseUiNameChange(val name: String, val trackableExerciseUiState: TrackableExerciseUiState): CreateWorkoutEvent()

    data class OnTrackableExerciseUiRepsChange(val reps: String, val trackableExerciseUiStateId: Int, val index: Int): CreateWorkoutEvent()

    data class OnTrackableExerciseUiRestChange(val rest: String, val trackableExerciseUiStateId: Int, val index: Int): CreateWorkoutEvent()

    data class OnTrackableExerciseUiWeightChange(val weight: String, val trackableExerciseUiStateId: Int, val index: Int): CreateWorkoutEvent()

    data class OnRemoveSetRow(val id: Int, val exerciseId: Int): CreateWorkoutEvent()

    object CheckTrackedExercise: CreateWorkoutEvent()

    data class OnCreateWorkout(val trackableExercise: List<TrackableExerciseUiState>, val workoutName: String, val lastUsedId: Int): CreateWorkoutEvent()

    object AddPageCount: CreateWorkoutEvent()

    object SubtractPageCount: CreateWorkoutEvent()

    data class AddSet(val page: Int): CreateWorkoutEvent()

    data class OnRemovePage(val page: Int): CreateWorkoutEvent()

}
