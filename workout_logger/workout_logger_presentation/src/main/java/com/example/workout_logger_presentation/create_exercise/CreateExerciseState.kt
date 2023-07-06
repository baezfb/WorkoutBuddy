package com.example.workout_logger_presentation.create_exercise

import com.example.workout_logger_presentation.create_exercise.model.Muscle

data class CreateExerciseState(
    val exerciseName: String = "",
    val description: String = "",
    val primaryMuscles: List<Muscle> = emptyList(),
    val secondaryMuscles: List<Muscle> = emptyList(),
    val muscles: List<Muscle> = listOf(
        Muscle("Anterior deltoid", "/static/images/muscles/main/muscle-2.svg", true),
        Muscle("Biceps brachii", "/static/images/muscles/main/muscle-1.svg", true),
        Muscle("Biceps femoris", "/static/images/muscles/main/muscle-11.svg", false),
        Muscle("Brachialis", "/static/images/muscles/main/muscle-13.svg", true),
        Muscle("Gastrocnemius", "/static/images/muscles/main/muscle-7.svg", false),
        Muscle("Gluteus maximus", "/static/images/muscles/main/muscle-8.svg", false),
        Muscle("Latissimus dorsi", "/static/images/muscles/main/muscle-12.svg", false),
        Muscle("Obliquus externus abdominis", "/static/images/muscles/main/muscle-14.svg", true),
        Muscle("Pectoralis major", "/static/images/muscles/main/muscle-4.svg", true),
        Muscle("Quadriceps femoris", "/static/images/muscles/main/muscle-10.svg", true),
        Muscle("Rectus abdominis", "/static/images/muscles/main/muscle-6.svg", true),
        Muscle("Serratus anterior", "/static/images/muscles/main/muscle-3.svg", true),
        Muscle("Soleus", "/static/images/muscles/main/muscle-15.svg", false),
        Muscle("Trapezius", "/static/images/muscles/main/muscle-9.svg", false),
        Muscle("Triceps brachii", "/static/images/muscles/main/muscle-5.svg", false)
    )
)
