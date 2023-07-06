package com.example.workout_logger_data.repository

import com.example.workout_logger_data.local.ExerciseDao
import com.example.workout_logger_data.mapper.toCompletedWorkout
import com.example.workout_logger_data.mapper.toCompletedWorkoutEntity
import com.example.workout_logger_data.mapper.toExerciseEntity
import com.example.workout_logger_data.mapper.toTrackedExercise
import com.example.workout_logger_data.mapper.toTrackedWorkout
import com.example.workout_logger_data.mapper.toWorkoutEntity
import com.example.workout_logger_domain.model.CompletedWorkout
import com.example.workout_logger_domain.model.TrackedExercise
import com.example.workout_logger_domain.model.TrackedWorkout
import com.example.workout_logger_domain.repository.ExerciseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

class ExerciseRepositoryImpl(
    private val dao: ExerciseDao
): ExerciseRepository {

    override fun getExercisesForName(name: String): Flow<List<TrackedExercise>> {
        return dao.getExerciseByName(name).map { entities ->
            entities.map { it.toTrackedExercise() }

        }
    }

    override suspend fun insertTrackedWorkout(trackedWorkout: TrackedWorkout) {
        dao.insertWorkout(trackedWorkout.toWorkoutEntity())
    }

    override fun getWorkouts(): Flow<List<TrackedWorkout>> {
        return dao.getWorkouts().map { entities ->
            entities.map {
                it.toTrackedWorkout()
            }
        }
    }

    override fun getWorkoutsByName(workoutName: String): Flow<List<TrackedWorkout>> {
        return dao.getWorkoutsByName(workoutName).map { entities ->
            entities.map {
                it.toTrackedWorkout()
            }
        }
    }

    override suspend fun insertCompletedWorkout(completedWorkout: CompletedWorkout) {
        dao.insertCompletedWorkout(completedWorkout.toCompletedWorkoutEntity())
    }

    override fun getWorkoutsForDate(localDate: LocalDate): Flow<List<CompletedWorkout>> {
        return dao.getCompletedWorkoutsByDate(
            day = localDate.dayOfMonth,
            month = localDate.monthValue,
            year = localDate.year
        ).map { entities ->
            entities.map { it.toCompletedWorkout() }
        }
    }

    override suspend fun insertTrackedExercise(trackedExercise: TrackedExercise) {
        dao.insertExercise(trackedExercise.toExerciseEntity())
    }
}