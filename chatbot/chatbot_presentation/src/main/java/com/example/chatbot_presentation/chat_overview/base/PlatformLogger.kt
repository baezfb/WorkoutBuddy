package com.example.chatbot_presentation.chat_overview.base

import android.util.Log

object PlatformLogger {

    fun logError(exception: Throwable) {
        Log.e(TAG_ERROR, exception.message, exception)
    }

    fun logError(tag: String, exception: Throwable) {
        Log.e(tag, exception.message, exception)
    }

    fun logError(tag: String, message: String, exception: Throwable) {
        Log.e(tag, "$message: ${exception.message}", exception)
    }
}

internal const val TAG_ERROR = "ChatBot-Builder Error"