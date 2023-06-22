package com.hbaez.workoutbuddy.workout.start_workout

sealed class StartWorkoutEvent {
    data class AddLoggerList(val loggerListState: LoggerListState): StartWorkoutEvent()

    data class OnRepIncrease(val page: Int, val index: Int): StartWorkoutEvent()

    data class OnRepDecrease(val page: Int, val index: Int): StartWorkoutEvent()

    data class OnWeightIncrease(val page: Int, val index: Int): StartWorkoutEvent()

    data class OnWeightDecrease(val page: Int, val index: Int): StartWorkoutEvent()
}