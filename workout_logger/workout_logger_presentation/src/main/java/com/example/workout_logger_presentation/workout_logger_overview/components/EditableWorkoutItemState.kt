package com.example.workout_logger_presentation.workout_logger_overview.components

data class EditableWorkoutItemState(
    val origReps: List<String> = emptyList(),
    val origWeights: List<String> = emptyList(),
    val origIsCompleted: List<String> = emptyList(),
    val reps: List<String> = emptyList(),
    val weight: List<String> = emptyList(),
    val isCompleted: List<String> = emptyList()
)
