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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.annotation.ExperimentalCoilApi
import com.example.chatbot_presentation.chat_overview.model.Message
import com.hbaez.core_ui.LocalSpacing

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
                MessageList(
                    modifier = Modifier.weight(1f),
                    messages = state.messages
                )
                UserInputField(
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }
    )
}

@Composable
fun AppBar(title: String) {
    TopAppBar(
        title = { Text(text = title) },
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = MaterialTheme.colors.onSecondary
    )
}

@Composable
fun MessageList(
    modifier: Modifier = Modifier,
    messages: List<Message>
) {
    LazyColumn(
        modifier = modifier,
        reverseLayout = true
    ) {
        messages.forEach{
            item {
                ChatBubble(message = it.text, user = it.user)
            }
        }
    }
}

@Composable
fun ChatBubble(message: String, user: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        horizontalArrangement = if(user) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .background(
                    if (user) MaterialTheme.colors.primaryVariant else MaterialTheme.colors.primarySurface,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(8.dp)
        ) {
            Text(text = message, color = MaterialTheme.colors.onSecondary)
        }
    }
}

@Composable
fun UserInputField(modifier: Modifier = Modifier) {
    var message by remember { mutableStateOf("") }

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.CenterEnd
    ) {
        TextField(
            value = message,
            onValueChange = { message = it/*TODO*/ },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
            textStyle = TextStyle(color = MaterialTheme.colors.onSurface),
            colors = TextFieldDefaults.textFieldColors(backgroundColor = MaterialTheme.colors.surface),
            trailingIcon = { Icon(
                imageVector = Icons.Default.Send,
                contentDescription = "Send",
                tint = Color.LightGray
            ) },
            placeholder = { Text(text = "Type your message...") },
        )
    }
}