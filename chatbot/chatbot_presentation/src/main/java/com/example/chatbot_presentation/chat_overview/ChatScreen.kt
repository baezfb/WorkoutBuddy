package com.example.chatbot_presentation.chat_overview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.chatbot_presentation.chat_overview.components.BalloonBotMessage
import com.example.chatbot_presentation.chat_overview.components.BalloonBotTyping
import com.example.chatbot_presentation.chat_overview.components.BalloonUserMessage
import com.example.chatbot_presentation.chat_overview.components.SendInputField
import com.example.chatbot_presentation.chat_overview.model.ChatDefaults
import com.example.chatbot_presentation.chat_overview.model.MessageType

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Divider
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.unit.dp
import co.yml.ychat.YChat
import com.example.chatbot_presentation.chat_overview.components.BalloonUserButton
import com.hbaez.core_ui.LocalSpacing

@Composable
fun ChatScreen(
    apiKey: String,
    chatDefaults: ChatDefaults = ChatDefaults(),
    chatState: ChatState = rememberChatState(apiKey, chatDefaults),
) {
    val spacing = LocalSpacing.current
    Column(
        Modifier
            .background(chatDefaults.colors.backgroundColor)
            .fillMaxHeight()
            .padding(bottom = spacing.spaceExtraExtraLarge)
    ) {
        val scrollState = rememberLazyListState()
        val messages = chatState.messages
        LazyColumn(
            state = scrollState,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .weight(1F)
                .padding(horizontal = 8.dp),
        ) {
            item { Spacer(modifier = Modifier.padding(top = 16.dp)) }
            items(messages) { item ->
                when (item) {
                    is MessageType.User ->
                        BalloonUserMessage(
                            item.text,
                            chatDefaults,
                            item.isError,
                            onTryAgain = { chatState.onTryAgain(it) }
                        )

                    is MessageType.Bot ->
                        BalloonBotMessage(item.text, chatDefaults)

                    is MessageType.Loading ->
                        BalloonBotTyping(chatDefaults)

                    is MessageType.UserButton -> {
                        BalloonUserButton(
                            text = item.text,
                            chatDefaults = chatDefaults,
                            onClick = {

                            })
                    }
                }
            }
        }
        SenderMessageSection(chatState, chatDefaults)
    }
}

@Composable
private fun SenderMessageSection(
    chatState: ChatState,
    chatDefaults: ChatDefaults
) {
    Box {
        Divider(color = chatDefaults.colors.dividerColor)
        SendInputField(
            value = chatState.message.value,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            chatDefaults = chatDefaults,
            isButtonVisible = chatState.onButtonVisible.value,
            hint = chatDefaults.inputFieldHint,
            onTextChanged = { chatState.onMessage(it) },
            onButtonClick = { chatState.sendMessage() }
        )
    }
}

@Composable
private fun rememberChatState(
    apiKey: String,
    chatDefaults: ChatDefaults,
): ChatState {
    val yChat = YChat.create(apiKey)
    val coroutine = rememberCoroutineScope()
    val chatState =
        ChatState(chatDefaults, yChat, coroutine)
    return remember { chatState }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(title: String) {
    TopAppBar(
        title = { Text(text = title) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onSecondary
        )
    )
}