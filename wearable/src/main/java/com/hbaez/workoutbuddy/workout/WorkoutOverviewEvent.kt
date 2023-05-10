package com.hbaez.workoutbuddy.workout

sealed class WorkoutOverviewEvent {
    data class OnWorkoutClick(val index: Int): WorkoutOverviewEvent()
}
