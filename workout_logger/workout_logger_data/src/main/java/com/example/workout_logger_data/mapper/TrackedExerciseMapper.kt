package com.example.workout_logger_data.mapper

import com.example.workout_logger_data.local.entity.ExerciseEntity
import com.example.workout_logger_domain.model.TrackedExercise

fun ExerciseEntity.toTrackedExercise(): TrackedExercise {
    return TrackedExercise(
        id = id,
        name = name,
        exerciseBase = exerciseBase,
        description = description,
        muscles = muscles,
        muscles_secondary = muscles_secondary,
        equipment = equipment,
        image_url = image_url?.split(",") ?: emptyList(),
        is_main = is_main,
        is_front = is_front,
        muscle_name_main = muscle_name_main,
        image_url_main = image_url_main?.split(",") ?: emptyList(),
        image_url_secondary = image_url_secondary?.split(",") ?: emptyList(),
        muscle_name_secondary = muscle_name_secondary
    )
}

fun TrackedExercise.toExerciseEntity(): ExerciseEntity {
    return ExerciseEntity(
        id = id ?: "",
        name = name ?: "",
        exerciseBase = exerciseBase ?: -1,
        description = description ?: "N/A",
        muscle_name_main = muscle_name_main,
        muscle_name_secondary = muscle_name_secondary,
        image_url_main = image_url_main.joinToString(","),
        image_url_secondary = image_url_secondary.joinToString(","),
        image_url = image_url.joinToString(","),
        equipment = null,
        is_front = is_front,
        is_main = is_main,
        muscles = null,
        muscles_secondary = null
    )
}