package com.hbaez.workoutbuddy.workout.timer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.Log
import androidx.annotation.RequiresApi

class TimerExpiredReceiver: BroadcastReceiver() {

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onReceive(context: Context, intent: Intent) {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator // deprecated on API > 31. Use VibratorManager in later versions
//        val vibrator = vibratorManager.defaultVibrator
        vibrator.vibrate(VibrationEffect.createOneShot(1250L, VibrationEffect.DEFAULT_AMPLITUDE))
        NotificationUtil.showTimerExpired(context)
    }
}