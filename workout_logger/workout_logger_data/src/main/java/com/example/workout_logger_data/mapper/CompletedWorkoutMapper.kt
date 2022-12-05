package com.example.workout_logger_data.mapper

import com.example.workout_logger_data.local.entity.CompletedWorkoutEntity
import com.example.workout_logger_domain.model.CompletedWorkout

fun CompletedWorkout.toCompletedWorkoutEntity(): CompletedWorkoutEntity {
    return CompletedWorkoutEntity(
        workoutName = workoutName,
        workoutId = workoutId,
        exerciseName = exerciseName,
        exerciseId = exerciseId,
        sets = sets,
        rest = rest,
        reps = reps,
        weight = weight,
        dayOfMonth = dayOfMonth,
        month = month,
        year = year
    )
}

fun CompletedWorkoutEntity.toCompletedWorkout(): CompletedWorkout {
    return CompletedWorkout(
        id = id,
        workoutName = workoutName,
        workoutId = workoutId,
        exerciseName = exerciseName,
        exerciseId = exerciseId,
        sets = sets,
        rest = rest,
        reps = reps,
        weight = weight,
        dayOfMonth = dayOfMonth,
        month = month,
        year = year
    )
}