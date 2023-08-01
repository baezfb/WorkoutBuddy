package com.example.chatbot_presentation.chat_overview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.annotation.ExperimentalCoilApi
import com.example.chatbot_presentation.chat_overview.model.Message
import com.hbaez.core.R
import com.hbaez.core_ui.LocalSpacing
import com.hexascribe.chatbotbuilder.ChatBot
import com.hexascribe.chatbotbuilder.base.RoleEnum

@Composable
fun ChatScreen(
    viewModel: ChatViewModel = hiltViewModel()
){
    val spacing = LocalSpacing.current
    val state = viewModel.state
    Scaffold(
        topBar = { AppBar(title = "Chatbot") },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = spacing.spaceExtraLarge + spacing.spaceSmall)
            ) {
                val chatBot = ChatBot.Builder("PLACEHOLDER")
                    .setDarkMode(true)
                    .addMessage(RoleEnum.ASSISTANT, "Hi, how can I help you today?")
                    .addPreSeededMessage(RoleEnum.SYSTEM, "You are a helpful personal trainer")
                    .addPreSeededMessage(RoleEnum.SYSTEM, String.format(stringResource(id = R.string.chatgpt_preseed_msg), 23, "male", "beginner", 69, 190, "low", "lose", "upper body"))
                    .setInputFieldBorderWidth(1)
                    .build()
                chatBot.ComposeScreen()
            }
        }
    )
}
//
@Composable
fun AppBar(title: String) {
    TopAppBar(
        title = { Text(text = title) },
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = MaterialTheme.colors.onSecondary
    )
}