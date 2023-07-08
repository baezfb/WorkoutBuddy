package com.example.workout_logger_presentation.search_exercise

import com.example.workout_logger_presentation.create_exercise.model.Muscle

data class SearchExerciseState(
    val query: String = "",
    val isHintVisible: Boolean = false,
    val isSearching: Boolean = false,
    val trackableExercise: List<TrackableExerciseState> = emptyList()
)
