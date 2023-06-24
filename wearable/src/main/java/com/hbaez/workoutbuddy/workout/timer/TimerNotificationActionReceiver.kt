package com.hbaez.workoutbuddy.workout.timer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import java.time.Duration

class TimerNotificationActionReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action){
            TimerStatus.FINISHED.toString() -> { //action_stop
                TimerViewModel.removeAlarm(context)
                NotificationUtil.hideTimerNotification(context)
            }
            TimerStatus.RUNNING.toString() -> { //action_start
                val remainingTime = intent.getLongExtra("remainingTime", 0)
                val wakeUpTime = TimerViewModel.setAlarm(context, Duration.ofMillis(remainingTime))
                NotificationUtil.showTimerRunning(context, wakeUpTime)
            }
        }
    }
}