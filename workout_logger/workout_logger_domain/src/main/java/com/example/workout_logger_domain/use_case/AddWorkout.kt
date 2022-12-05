package com.example.workout_logger_domain.use_case

import com.example.workout_logger_domain.model.TrackedWorkout
import com.example.workout_logger_domain.repository.ExerciseRepository

class AddWorkout(
    private val repository: ExerciseRepository
) {

    suspend operator fun invoke(
        workoutName: String,
        exerciseName: String,
        exerciseId: Int?,
        sets: Int,
        rest: Int,
        reps: Int,
        weight: Int,
        rowId: Int,
        lastUsedId: Int
    ){
        repository.insertTrackedWorkout(
            TrackedWorkout(
                workoutId = null,
                name = workoutName,
                exerciseName = exerciseName ,
                exerciseId = exerciseId,
                sets = sets,
                rest = rest,
                reps = reps,
                weight = weight,
                rowId = rowId,
                lastUsedId = lastUsedId
            )
        )
    }
}