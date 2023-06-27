package com.example.chatbot_presentation.chat_overview.model

data class Message(
    val text: String,
    val user: Boolean,
    val exerciseList: List<String> = emptyList()
)
