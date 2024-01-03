package com.example.chatbot_presentation.chat_overview.model

sealed class MessageType {
    data class User(val text: String, var isError: Boolean = false): MessageType()
    data class Bot(val text: String): MessageType()
    data class UserButton(val text: String): MessageType()
    object RoutineForm: MessageType()
    object Loading: MessageType()
}