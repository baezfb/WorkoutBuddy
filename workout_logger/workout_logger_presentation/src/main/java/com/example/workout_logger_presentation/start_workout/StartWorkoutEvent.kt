package com.example.workout_logger_presentation.start_workout

import androidx.compose.ui.graphics.Color
import com.example.workout_logger_presentation.search_exercise.TrackableExerciseState
import com.hbaez.user_auth_presentation.model.WorkoutTemplate

sealed class StartWorkoutEvent {

    data class OnUpdateWorkoutName(val workoutName: String): StartWorkoutEvent()

    data class OnRepsChange(val reps: String, val index: Int, val rowId: Int, val page: Int, val exerciseName: String): StartWorkoutEvent()

    data class OnWeightChange(val weight: String, val index: Int, val rowId: Int, val page: Int, val exerciseName: String): StartWorkoutEvent()

    data class OnCheckboxChange(val isChecked: Boolean, val timerStatus: TimerStatus, val currRunningIndex: Int, val index: Int, val rowId: Int, val page: Int, val shouldUpdateTime: Boolean): StartWorkoutEvent()

    object ChangeRemainingTime: StartWorkoutEvent()

    object UpdateRemainingTime: StartWorkoutEvent()

    data class OnChangePage(val currentPage: Int): StartWorkoutEvent()

    object TimerFinished: StartWorkoutEvent()

    data class ChangeCheckboxColor(val color: Color, val id: Int, val index: Int): StartWorkoutEvent()

    data class OnSubmitWorkout(val workoutName: String, val trackableExercises: List<LoggerListState>, val workoutTemplates: List<WorkoutTemplate>, val dayOfMonth: Int, val month: Int, val year: Int): StartWorkoutEvent()

    data class AddLoggerList(val loggerListState: LoggerListState): StartWorkoutEvent()

    data class GetExerciseInfo(val page: Int): StartWorkoutEvent()

    data class OnToggleExerciseDescription(val trackableExerciseState: TrackableExerciseState): StartWorkoutEvent()

    data class OnTimeJump(val increase: Boolean, val timeJump: Long): StartWorkoutEvent()

    data class OnAddSet(val page: Int): StartWorkoutEvent()

    data class OnRemoveSet(val page: Int): StartWorkoutEvent()
}
