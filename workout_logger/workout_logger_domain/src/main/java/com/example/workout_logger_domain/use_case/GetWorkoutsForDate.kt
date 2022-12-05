package com.example.workout_logger_domain.use_case

import com.example.workout_logger_domain.model.CompletedWorkout
import com.example.workout_logger_domain.repository.ExerciseRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class GetWorkoutsForDate(
    private val repository: ExerciseRepository
) {

    operator fun invoke(date: LocalDate): Flow<List<CompletedWorkout>>{
        return repository.getWorkoutsForDate(date)
    }
}