package com.example.workout_logger_presentation.search_exercise

sealed class SearchExerciseEvent {

    data class OnExerciseNameChange(val name: String): SearchExerciseEvent()

    data class OnExerciseNameFocusChange(val isFocused: Boolean): SearchExerciseEvent()

    object OnSearch: SearchExerciseEvent()

    data class OnToggleTrackableExercise(val exercise: TrackableExerciseState): SearchExerciseEvent()

    data class OnToggleTrackableExerciseDescription(val exercise: TrackableExerciseState): SearchExerciseEvent()

    data class OnTrackExercise(val exercise: TrackableExerciseState, val rowId: Int): SearchExerciseEvent()
}