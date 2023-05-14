package com.hbaez.workoutbuddy.workout.start_workout

sealed class StartWorkoutEvent {
    data class AddLoggerList(val loggerListState: LoggerListState): StartWorkoutEvent()
}