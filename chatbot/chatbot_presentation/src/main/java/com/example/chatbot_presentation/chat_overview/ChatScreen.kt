package com.example.chatbot_presentation.chat_overview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.hbaez.core_ui.LocalSpacing

@Composable
fun ChatScreen(){
    val spacing = LocalSpacing.current
    Scaffold(
        topBar = { AppBar(title = "Chatbot") },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = spacing.spaceExtraLarge + spacing.spaceSmall)
            ) {
                MessageList(
                    modifier = Modifier.weight(1f)
                )
                UserInputField(
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }
    )
}

// Sample list of messages
val messages = listOf(
    "Hello",
    "How are you?",
    "I'm good, thanks!",
    "What can I help you with?",
    "Can you provide some information?",
    "Sure, what do you need to know?",
    "..."
)

@Composable
fun AppBar(title: String) {
    TopAppBar(
        title = { Text(text = title) },
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = MaterialTheme.colors.onSecondary
    )
}

@Composable
fun MessageList(modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier,
        reverseLayout = true
    ) {
        messages.forEach{
            item {
                ChatBubble(message = it)
            }
        }
    }
}

@Composable
fun ChatBubble(message: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        horizontalArrangement = Arrangement.End
    ) {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colors.primaryVariant, shape = RoundedCornerShape(8.dp))
                .padding(8.dp)
        ) {
            Text(text = message, color = MaterialTheme.colors.onSecondary)
        }
    }
}

@Composable
fun UserInputField(modifier: Modifier = Modifier) {
    var message by remember { mutableStateOf("") }

    Row(modifier = modifier) {
        TextField(
            value = message,
            onValueChange = { message = it },
            modifier = Modifier.weight(1f),
            colors = TextFieldDefaults.textFieldColors(backgroundColor = MaterialTheme.colors.primarySurface),
            placeholder = { Text(text = "Type your message...") }
        )
        Button(
            onClick = {
                // Handle send button click here
                // You can add the user message to the message list or perform any other action
                // For simplicity, let's just clear the input field
                message = ""
            },
            modifier = Modifier.padding(start = 8.dp)
        ) {
            Text(text = "Send")
        }
    }
}