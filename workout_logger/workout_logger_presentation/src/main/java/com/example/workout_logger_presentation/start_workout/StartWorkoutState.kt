package com.example.workout_logger_presentation.start_workout

import com.example.workout_logger_presentation.search_exercise.TrackableExerciseState
import java.time.Duration
import java.util.Date

data class StartWorkoutState(
    val workoutName: String = "",
    val pagerIndex: Int = 0,
    val timerJump: Long = 5,
    val loggerListStates: MutableList<LoggerListState> = mutableListOf(),
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
