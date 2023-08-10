package com.example.chatbot_presentation.chat_overview

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.hbaez.core.R
import com.hbaez.core_ui.LocalSpacing
import com.hexascribe.chatbotbuilder.ChatBot
import com.hexascribe.chatbotbuilder.base.RoleEnum

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ChatScreen(
    viewModel: ChatViewModel = hiltViewModel()
){
    val spacing = LocalSpacing.current
    val state = viewModel.state
    Scaffold(
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = spacing.spaceExtraLarge + spacing.spaceSmall)
            ) {
                val chatBot = ChatBot.Builder("PLACEHOLDER")
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
//@Composable
//fun AppBar(title: String) {
//    TopAppBar(
//        title = { Text(text = title) },
//        colors = TopAppBarColors(
//            MaterialTheme.colors.primary,
//        contentColor = MaterialTheme.colors.onSecondary
//    )
//}