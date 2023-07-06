package com.example.workout_logger_domain.repository

import com.example.workout_logger_domain.model.CompletedWorkout
import com.example.workout_logger_domain.model.TrackedExercise
import com.example.workout_logger_domain.model.TrackedWorkout
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface ExerciseRepository {
    fun getExercisesForName(name: String): Flow<List<TrackedExercise>>

    suspend fun insertTrackedWorkout(trackedWorkout: TrackedWorkout)

    fun getWorkouts(): Flow<List<TrackedWorkout>>

    fun getWorkoutsByName(workoutName: String): Flow<List<TrackedWorkout>>

    suspend fun insertCompletedWorkout(completedWorkout: CompletedWorkout)

    fun getWorkoutsForDate(localDate: LocalDate): Flow<List<CompletedWorkout>>

    suspend fun insertTrackedExercise(trackedExercise: TrackedExercise)
}