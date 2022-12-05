package com.example.workout_logger_presentation.search_exercise

data class SearchExerciseState(
    val query: String = "",
    val isHintVisible: Boolean = false,
    val isSearching: Boolean = false,
    val trackableExercise: List<TrackableExerciseState> = emptyList()
)
