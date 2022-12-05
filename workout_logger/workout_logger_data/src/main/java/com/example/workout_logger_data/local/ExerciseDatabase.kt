package com.example.workout_logger_data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.workout_logger_data.local.entity.CompletedWorkoutEntity
import com.example.workout_logger_data.local.entity.ExerciseEntity
import com.example.workout_logger_data.local.entity.WorkoutEntity

@Database(
    entities = [ExerciseEntity::class, WorkoutEntity::class, CompletedWorkoutEntity::class],
    version = 1
)
abstract class ExerciseDatabase: RoomDatabase() {

    abstract val dao: ExerciseDao
}
