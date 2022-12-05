package com.example.workout_logger_domain.use_case

import com.example.workout_logger_domain.model.CompletedWorkout
import com.example.workout_logger_domain.repository.ExerciseRepository

class AddCompletedWorkout(
    private val repository: ExerciseRepository
) {
    suspend operator fun invoke(
        workoutName: String,
        workoutId: Int,
        exerciseName: String,
        exerciseId: Int?,
        sets: Int,
        rest: Int,
        reps: String,
        weight: String,
        dayOfMonth: Int,
        month: Int,
        year: Int
    ){
        repository.insertCompletedWorkout(
            CompletedWorkout(
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
        )
    }
}