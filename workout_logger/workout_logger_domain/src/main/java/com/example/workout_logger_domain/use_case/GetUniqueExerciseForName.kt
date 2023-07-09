package com.example.workout_logger_domain.use_case

import com.example.workout_logger_domain.model.TrackedExercise
import com.example.workout_logger_domain.repository.ExerciseRepository
import kotlinx.coroutines.flow.Flow

class GetUniqueExerciseForName(
    private val repository: ExerciseRepository
) {

    operator fun invoke(query: String): Flow<TrackedExercise> {
        return repository.getUniqueExerciseForName(query)
    }
}