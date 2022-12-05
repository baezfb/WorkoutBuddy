package com.example.workout_logger_data.mapper

import com.example.workout_logger_data.local.entity.WorkoutEntity
import com.example.workout_logger_domain.model.TrackedWorkout

fun TrackedWorkout.toWorkoutEntity(): WorkoutEntity {
    return WorkoutEntity(
        name = name,
        exerciseName = exerciseName,
        exerciseId = exerciseId,
        sets = sets,
        rest = rest,
        reps = reps,
        weight = weight,
        rowId = rowId,
        lastUsedId = lastUsedId
    )
}

fun WorkoutEntity.toTrackedWorkout(): TrackedWorkout {
    return TrackedWorkout(
        workoutId = id!!,
        name = name,
        exerciseName = exerciseName,
        exerciseId = exerciseId,
        sets = sets,
        rest = rest,
        reps = reps,
        weight = weight,
        rowId = rowId,
        lastUsedId = lastUsedId
    )
}