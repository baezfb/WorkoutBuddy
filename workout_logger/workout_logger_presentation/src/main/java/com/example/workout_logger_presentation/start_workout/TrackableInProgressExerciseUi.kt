package com.example.workout_logger_presentation.start_workout

import androidx.compose.material.MaterialTheme
import androidx.compose.ui.graphics.Color
import com.example.workout_logger_domain.model.TrackedExercise

data class TrackableInProgressExerciseUi(
    val name: String = "",
    val origSets: String = "",
    val sets: String = "0",
    val origReps: String = "",
    val reps: List<String> = List(sets.toInt()) { "" },
    val origRest: String = "",
    val rest: List<String> = List(sets.toInt()) { "" },
    val origWeight: String = "",
    val weight: List<String> = List(sets.toInt()) { "" },
    val id: Int = 0,
    val exerciseId: Int?,
    val exercise: TrackedExercise?,
    val isCompleted: List<Boolean> = List(sets.toInt()) { false },
    val timerStatus: TimerStatus = TimerStatus.START,
    val checkedColor: List<Color> = List(sets.toInt()) { Color.DarkGray }
)
