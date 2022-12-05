package com.example.workout_logger_presentation.start_workout.components

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.workout_logger_presentation.start_workout.StartWorkoutViewModel
import com.example.workout_logger_presentation.start_workout.TimerStatus
import java.time.Duration

class TimerNotificationActionReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action){
            TimerStatus.FINISHED.toString() -> { //action_stop
                StartWorkoutViewModel.removeAlarm(context)
                NotificationUtil.hideTimerNotification(context)
            }
            TimerStatus.RUNNING.toString() -> { //action_start
                val remainingTime = intent.getLongExtra("remainingTime", 0)
                val wakeUpTime = StartWorkoutViewModel.setAlarm(context, Duration.ofMillis(remainingTime))
                NotificationUtil.showTimerRunning(context, wakeUpTime)
            }
        }
    }

}