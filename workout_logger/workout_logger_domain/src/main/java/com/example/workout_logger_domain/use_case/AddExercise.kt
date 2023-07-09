package com.example.workout_logger_domain.use_case

import com.example.workout_logger_domain.model.TrackedExercise
import com.example.workout_logger_domain.repository.ExerciseRepository

class AddExercise(
    private val repository: ExerciseRepository
) {

    suspend operator fun invoke(
        id: String,
        exerciseName: String,
        description: String,
        primaryMuscles: String,
        primaryURL: List<String>,
        secondaryMuscles: String,
        secondaryURL: List<String>,
        image_url: List<String>,
        image_1: ByteArray?,
        image_2: ByteArray?,
        image_3: ByteArray?,
        image_4: ByteArray?,
    ) {
        repository.insertTrackedExercise(
            TrackedExercise(
                id = id,
                name = exerciseName,
                exerciseBase = null,
                description = description,
                muscle_name_main = primaryMuscles,
                image_url_main = primaryURL,
                muscle_name_secondary = secondaryMuscles,
                image_url_secondary = secondaryURL,
                image_url = image_url
            )
        )
    }
}