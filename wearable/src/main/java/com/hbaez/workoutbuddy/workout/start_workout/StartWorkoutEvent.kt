package com.hbaez.workoutbuddy.workout.start_workout

import com.hbaez.user_auth_presentation.model.WorkoutTemplate

sealed class StartWorkoutEvent {
    data class AddLoggerList(val loggerListState: LoggerListState): StartWorkoutEvent()

    data class OnRepIncrease(val page: Int, val index: Int): StartWorkoutEvent()

    data class OnRepDecrease(val page: Int, val index: Int): StartWorkoutEvent()

    data class OnWeightIncrease(val page: Int, val index: Int): StartWorkoutEvent()

    data class OnWeightDecrease(val page: Int, val index: Int): StartWorkoutEvent()

    data class OnSetIncrease(val page: Int): StartWorkoutEvent()

    data class OnSubmitWorkout(val workoutName: String, val trackableExercises: List<LoggerListState>, val workoutTemplates: List<WorkoutTemplate>, val dayOfMonth: Int, val month: Int, val year: Int): StartWorkoutEvent()
}