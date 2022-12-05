package com.example.workout_logger_presentation.start_workout.components

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.VibrationEffect
import android.os.VibratorManager
import android.util.Log
import androidx.annotation.RequiresApi

class TimerExpiredReceiver: BroadcastReceiver() {
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onReceive(context: Context, intent: Intent) {
        Log.println(Log.DEBUG, "!!!! TimerReceiver", "reached TimerExpiredReceiver")
        val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        val vibrator = vibratorManager.defaultVibrator
        vibrator.vibrate(VibrationEffect.createOneShot(1250L, VibrationEffect.DEFAULT_AMPLITUDE))
        NotificationUtil.showTimerExpired(context)
    }
}