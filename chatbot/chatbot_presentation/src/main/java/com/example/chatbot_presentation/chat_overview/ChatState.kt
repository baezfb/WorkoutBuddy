package com.example.chatbot_presentation.chat_overview

import com.example.chatbot_presentation.chat_overview.model.Message

data class ChatState(
    val suggestWorkoutRoutine: Boolean? = null,
    val workoutType: WorkoutType? = null,
    val daysAWeek: Int? = null,
    val weeks: Int? = null,
    val userPrompt: String = "",
    val messages: List<Message> = listOf(Message(text="placeholder text", user = false))
)

enum class WorkoutType {
    STRENGTH, CARDIO, HIIT
}