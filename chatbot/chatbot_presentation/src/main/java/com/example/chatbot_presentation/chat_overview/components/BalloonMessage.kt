package com.example.chatbot_presentation.chat_overview.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.example.chatbot_presentation.chat_overview.model.ChatDefaults
import com.hbaez.core.R

@OptIn(ExperimentalAnimationApi::class)
@Composable
internal fun BalloonUserMessage(
    text: String,
    chatDefaults: ChatDefaults = ChatDefaults(),
    isError: Boolean = false,
    onTryAgain: (String) -> Unit = {}
) {
    var showError by remember { mutableStateOf(false) }
    showError = isError
    Column(
        Modifier
            .fillMaxWidth()
            .clickable(isError) { if (isError) onTryAgain(text) },
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(4.dp)

    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(
                            topStart = 16.dp,
                            topEnd = 16.dp,
                            bottomStart = 16.dp,
                        )
                    )
                    .background(chatDefaults.colors.userBalloonColor)
                    .padding(horizontal = 16.dp)
                    .padding(vertical = 8.dp),
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                color = chatDefaults.colors.userBalloonTextColor
            )
            AnimatedVisibility(visible = showError, enter = scaleIn(), exit = scaleOut()) {
                Icon(
                    Icons.Outlined.Warning,
                    null,
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
        AnimatedVisibility(visible = showError, enter = scaleIn(), exit = scaleOut()) {
            Text(
                chatDefaults.errorText,
                style = MaterialTheme.typography.labelSmall,
                color = chatDefaults.colors.errorTextColor,
            )
        }
    }
}

@Composable
internal fun BalloonBotMessage(
    text: String,
    chatDefaults: ChatDefaults = ChatDefaults()
) {
    Box(
        Modifier
            .fillMaxWidth()
            .padding(end = 64.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            chatDefaults.BotIcon()
            Text(
                text = text,
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(
                            bottomStart = 16.dp,
                            topEnd = 16.dp,
                            bottomEnd = 16.dp,
                        )
                    )
                    .background(chatDefaults.colors.botBalloonColor)
                    .padding(horizontal = 16.dp)
                    .padding(vertical = 8.dp),
                color = chatDefaults.colors.botBalloonTextColor,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@Composable
internal fun BalloonBotTyping(
    chatDefaults: ChatDefaults = ChatDefaults()
) {
    Box(
        Modifier
            .fillMaxWidth()
            .padding(end = 64.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            chatDefaults.BotIcon()
            TypingTextLoading(
                chatDefaults = chatDefaults,
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(
                            bottomStart = 16.dp,
                            topEnd = 16.dp,
                            bottomEnd = 16.dp,
                        )
                    )
                    .background(chatDefaults.colors.botBalloonColor)
                    .padding(horizontal = 16.dp)
                    .padding(vertical = 8.dp),
            )
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
internal fun BalloonUserButton(
    text: String,
    chatDefaults: ChatDefaults = ChatDefaults(),
    onClick: () -> Unit = {}
) {
    Column(
        Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(
                            topStart = 16.dp,
                            topEnd = 16.dp,
                            bottomStart = 16.dp,
                        )
                    )
                    .clickable { onClick() }
                    .background(chatDefaults.colors.backgroundColor)
                    .border(
                        2.dp,
                        chatDefaults.colors.userBalloonTextColor,
                        RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomStart = 16.dp)
                    )
                    .padding(horizontal = 16.dp)
                    .padding(vertical = 8.dp),
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                color = chatDefaults.colors.userBalloonTextColor
            )
        }
        AnimatedVisibility(visible = true, enter = scaleIn(), exit = scaleOut()) {
            Text(
                chatDefaults.suggestionText,
                style = MaterialTheme.typography.labelSmall,
                color = chatDefaults.colors.errorTextColor,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun BalloonRoutineForm(
    workoutNames: List<String>,
    chatDefaults: ChatDefaults = ChatDefaults()
) {
    var isUpdate by remember { mutableStateOf(false) }
    var isExpanded by remember { mutableStateOf(false) }
    var dropdownSelectedIndex by remember { mutableStateOf(0) }
    Box(
        Modifier
            .fillMaxWidth()
            .padding(end = 64.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            chatDefaults.BotIcon()
            Column(
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(
                            bottomStart = 16.dp,
                            topEnd = 16.dp,
                            bottomEnd = 16.dp,
                        )
                    )
                    .background(chatDefaults.colors.botBalloonColor)
                    .padding(horizontal = 8.dp)
                    .padding(vertical = 8.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.fill_out_form_bot_message),
                    color = chatDefaults.colors.botBalloonTextColor,
                    style = MaterialTheme.typography.bodyMedium,
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = isUpdate,
                        onCheckedChange = { isChecked ->
                            isUpdate = !isUpdate
                        },
                        colors = CheckboxDefaults.colors(
                            checkedColor = chatDefaults.colors.botBalloonTextColor,
                            checkmarkColor = chatDefaults.colors.botBalloonColor,
                            uncheckedColor = chatDefaults.colors.backgroundColor
                        )
                    )
                    Text(
                        text = stringResource(id = R.string.update),
                        color = chatDefaults.colors.botBalloonTextColor,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    ExposedDropdownMenuBox(
                        expanded = isExpanded,
                        onExpandedChange = { isExpanded = !isExpanded }
                    ) {
                        OutlinedTextField(
                            value = workoutNames[dropdownSelectedIndex],
                            enabled = false,
                            onValueChange = { },
                            singleLine = true,
                            textStyle = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier
                                .clip(RoundedCornerShape(5.dp))
                                .shadow(
                                    elevation = 2.dp,
                                    shape = RoundedCornerShape(5.dp)
                                )
                                .background(MaterialTheme.colorScheme.primaryContainer)
                                .menuAnchor(),
                            colors = TextFieldDefaults.colors(
                                disabledContainerColor = chatDefaults.colors.botBalloonColor,
                                disabledTextColor = if (isUpdate) { chatDefaults.colors.botBalloonTextColor } else { chatDefaults.colors.botBalloonColor },
                                disabledIndicatorColor = chatDefaults.colors.botBalloonColor
                            )
                        )
                        // Dropdown menu
                        DropdownMenu(
                            expanded = isExpanded,
                            onDismissRequest = { isExpanded = false }
                        ) {
                            if (isUpdate) {
                                workoutNames.forEachIndexed { index, s ->
                                    DropdownMenuItem(
                                        text = { Text(text = s) },
                                        onClick = {
                                            dropdownSelectedIndex = index
                                            isExpanded = !isExpanded
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}