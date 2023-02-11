package com.example.workout_logger_presentation.start_workout

import androidx.compose.ui.graphics.Color

sealed class StartWorkoutEvent {

    data class OnRepsChange(val reps: String, val index: Int, val rowId: Int): StartWorkoutEvent()

    data class OnWeightChange(val weight: String, val index: Int, val rowId: Int): StartWorkoutEvent()

    data class OnCheckboxChange(val isChecked: Boolean, val timerStatus: TimerStatus, val currRunningIndex: Int, val index: Int, val rowId: Int, val page: Int): StartWorkoutEvent()

    object ChangeRemainingTime: StartWorkoutEvent()

    object UpdateRemainingTime: StartWorkoutEvent()

    data class OnChangePage(val currentPage: Int): StartWorkoutEvent()

    object TimerFinished: StartWorkoutEvent()

    data class ChangeCheckboxColor(val color: Color, val id: Int, val index: Int): StartWorkoutEvent()

    data class OnSubmitWorkout(val workoutName: String, val trackableExercises: List<LoggerListState>, val dayOfMonth: Int, val month: Int, val year: Int): StartWorkoutEvent()

    data class AddLoggerList(val loggerListState: LoggerListState): StartWorkoutEvent()
}
