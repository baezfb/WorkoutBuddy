package com.example.workout_logger_presentation.start_exercise

import androidx.compose.ui.graphics.Color
import com.example.workout_logger_presentation.search_exercise.TrackableExerciseState
import java.time.Duration
import java.util.Date

data class StartExerciseState(
    val exerciseName: String = "",
    val timerJump: Long = 5,
    val sets: String = "1",
    val rest: List<String> = listOf(""), /*TODO replace with preferences. Init in viewModel*/
    val reps: List<String> = listOf(""), /*TODO replace with preferences. Init in viewModel*/
    val weight: List<String> = listOf(""), /*TODO replace with preferences. Init in viewModel*/
    val isCompleted: List<Boolean> = List(1) { false },
    val checkedColor: List<Color> = List(1) { Color.DarkGray },
    val exerciseInfo: List<TrackableExerciseState> = emptyList(),
    val timerStatus: TimerStatus = TimerStatus.START,
    val timeDuration: Duration = Duration.ofSeconds(30),
    val remainingTime: Long = timeDuration.toMillis(),
    val currRunningIndex: Int = -1,
    val currRunningId: Int = -1,
    val startTime: Date = Date()
)

enum class TimerStatus {
    START, RUNNING, PAUSED, FINISHED
}
