package com.example.chatbot_presentation.chat_overview

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import co.yml.ychat.YChat
import co.yml.ychat.entrypoint.features.ChatCompletions
import com.example.chatbot_presentation.chat_overview.base.PlatformLogger.logError
import com.example.chatbot_presentation.chat_overview.base.RoleEnum
import com.example.chatbot_presentation.chat_overview.model.ChatDefaults
import com.example.chatbot_presentation.chat_overview.model.MessageType
import com.hbaez.core.domain.model.GoalType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

data class Muscle(
    val name: String,
    val imageURL: String,
    val isFront: Boolean
)

data class RoutineFormState(
    val primaryMuscles: List<Muscle> = emptyList(),
    val secondaryMuscles: List<Muscle> = emptyList(),
    val weightGoalType: GoalType = GoalType.KeepWeight,
    val timeLimit: Int = 60,
    val muscles: List<Muscle> = listOf(
        Muscle("Anterior deltoid", "/static/images/muscles/main/muscle-2.svg", true),
        Muscle("Biceps brachii", "/static/images/muscles/main/muscle-1.svg", true),
        Muscle("Biceps femoris", "/static/images/muscles/main/muscle-11.svg", false),
        Muscle("Brachialis", "/static/images/muscles/main/muscle-13.svg", true),
        Muscle("Gastrocnemius", "/static/images/muscles/main/muscle-7.svg", false),
        Muscle("Gluteus maximus", "/static/images/muscles/main/muscle-8.svg", false),
        Muscle("Latissimus dorsi", "/static/images/muscles/main/muscle-12.svg", false),
        Muscle("Obliquus externus abdominis", "/static/images/muscles/main/muscle-14.svg", true),
        Muscle("Pectoralis major", "/static/images/muscles/main/muscle-4.svg", true),
        Muscle("Quadriceps femoris", "/static/images/muscles/main/muscle-10.svg", true),
        Muscle("Rectus abdominis", "/static/images/muscles/main/muscle-6.svg", true),
        Muscle("Serratus anterior", "/static/images/muscles/main/muscle-3.svg", true),
        Muscle("Soleus", "/static/images/muscles/main/muscle-15.svg", false),
        Muscle("Trapezius", "/static/images/muscles/main/muscle-9.svg", false),
        Muscle("Triceps brachii", "/static/images/muscles/main/muscle-5.svg", false)
    ),

    )

class ChatState(
    private val chatDefaults: ChatDefaults,
    private val yChat: YChat,
    private val coroutineScope: CoroutineScope,
) {

    val message = mutableStateOf("")

    val messages = mutableStateListOf<MessageType>()

    val onButtonVisible = mutableStateOf(message.value.isNotEmpty())

    private val chatCompletions: ChatCompletions

    init {
        setupChatMessages()
        chatCompletions = createChatCompletions()
    }

    fun onMessage(message: String) {
        this.message.value = message
        verifyButtonVisible()
    }

    fun onTryAgain(message: String) {
        onError(false)
        this.message.value = message
        sendMessage()
    }

    fun sendMessage() = coroutineScope.launch {
        val messageToSend = message.value
        messages.add(MessageType.User(message.value))
        onLoading(true)
        onMessage("")
        runCatching { chatCompletions.execute(messageToSend) }
            .also { onLoading(false) }
            .onSuccess { messages.add(MessageType.Bot(it.first().content)) }
            .onFailure { handleFailure(it) }
    }

    private fun onLoading(isLoading: Boolean) {
        if (isLoading) {
            onError(false)
            messages.add(MessageType.Loading)
        } else {
            messages.remove(MessageType.Loading)
        }
        verifyButtonVisible()
    }

    private fun handleFailure(throwable: Throwable) {
        if (chatDefaults.isLogErrorEnabled) logError(throwable)
        onError(true)
    }

    private fun onError(isError: Boolean) {
        if (isError) {
            val error = messages.removeLast() as? MessageType.User ?: return
            error.isError = true
            messages.add(error)
        } else {
            messages.removeAll { it is MessageType.User && it.isError }
        }
    }

    private fun createChatCompletions(): ChatCompletions {
        val chatCompletions = yChat
            .chatCompletions()
            .setMaxTokens(chatDefaults.maxTokens)
        chatDefaults.preSeededMessages.forEach {
            chatCompletions.addMessage(it.role, it.content)
        }
        return chatCompletions
    }

    private fun setupChatMessages() {
        val chatMessages = chatDefaults.messages.mapNotNull {
            if (RoleEnum.isAssistant(it.role)) MessageType.Bot(it.content)
            else if (RoleEnum.isUser(it.role)) MessageType.User(it.content)
            else null
        }
        this.messages.addAll(chatMessages)
        this.messages.add(MessageType.Bot("Hello! How can I assist you today?"))
        this.messages.add(MessageType.UserButton("Recommend a workout routine"))
        this.messages.add(MessageType.RoutineForm)
    }

    private fun verifyButtonVisible() {
        onButtonVisible.value = this.message.value.isNotEmpty()
                && !messages.contains(MessageType.Loading)
    }
}