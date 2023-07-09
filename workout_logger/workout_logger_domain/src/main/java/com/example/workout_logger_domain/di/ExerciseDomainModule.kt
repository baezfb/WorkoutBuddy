package com.example.workout_logger_domain.di

import com.example.workout_logger_domain.repository.ExerciseRepository
import com.example.workout_logger_domain.use_case.AddCompletedWorkout
import com.example.workout_logger_domain.use_case.AddExercise
import com.example.workout_logger_domain.use_case.AddWorkout
import com.example.workout_logger_domain.use_case.CreateWorkoutUseCases
import com.example.workout_logger_domain.use_case.ExerciseTrackerUseCases
import com.example.workout_logger_domain.use_case.GetExerciseForName
import com.example.workout_logger_domain.use_case.GetUniqueExerciseForName
import com.example.workout_logger_domain.use_case.GetWorkouts
import com.example.workout_logger_domain.use_case.GetWorkoutsByName
import com.example.workout_logger_domain.use_case.GetWorkoutsForDate
import com.example.workout_logger_domain.use_case.UpdateExercise
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import java.util.prefs.Preferences

@Module
@InstallIn(ViewModelComponent::class)
object ExerciseDomainModule {

    @ViewModelScoped
    @Provides
    fun provideTrackerUseCases(
        repository: ExerciseRepository
    ): ExerciseTrackerUseCases {
        return ExerciseTrackerUseCases(
            getExerciseForName = GetExerciseForName(repository),
            getUniqueExerciseForName = GetUniqueExerciseForName(repository),
            getWorkouts = GetWorkouts(repository),
            getWorkoutsByName = GetWorkoutsByName(repository),
            addCompletedWorkout = AddCompletedWorkout(repository),
            getWorkoutsForDate = GetWorkoutsForDate(repository),
            addExercise = AddExercise(repository),
            updateExercise = UpdateExercise(repository)
        )
    }

    @ViewModelScoped
    @Provides
    fun provideCreateWorkoutUseCases(
        repository: ExerciseRepository
    ): CreateWorkoutUseCases {
        return CreateWorkoutUseCases(
            addWorkout = AddWorkout(repository)
        )
    }
}