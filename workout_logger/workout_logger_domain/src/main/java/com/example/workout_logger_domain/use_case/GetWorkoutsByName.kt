package com.example.workout_logger_domain.use_case

import com.example.workout_logger_domain.model.TrackedWorkout
import com.example.workout_logger_domain.repository.ExerciseRepository
import kotlinx.coroutines.flow.Flow

class GetWorkoutsByName(
    private val repository: ExerciseRepository
) {
    operator fun invoke(workoutName: String): Flow<List<TrackedWorkout>> {
        return repository.getWorkoutsByName(workoutName)
    }
}