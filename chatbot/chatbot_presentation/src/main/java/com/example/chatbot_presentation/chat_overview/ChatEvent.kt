package com.example.chatbot_presentation.chat_overview

import com.hbaez.core.domain.model.GoalType

sealed class ChatEvent {
    data class OnCheckboxFormAdd(val muscle: Muscle, val isPrimary: Boolean): ChatEvent()

    data class OnCheckboxFormRemove(val muscle: Muscle, val isPrimary: Boolean): ChatEvent()

    data class OnGoalTypeSelect(val goalType: GoalType): ChatEvent()

    data class OnTimeLimitSelect(val timeLimit: Int): ChatEvent()
}
