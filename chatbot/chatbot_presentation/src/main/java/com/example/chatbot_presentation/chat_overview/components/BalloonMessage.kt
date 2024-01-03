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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.chatbot_presentation.chat_overview.ChatEvent
import com.example.chatbot_presentation.chat_overview.ChatState
import com.example.chatbot_presentation.chat_overview.ChatViewModel
import com.example.chatbot_presentation.chat_overview.RoutineFormState
import com.example.chatbot_presentation.chat_overview.model.ChatDefaults
import com.hbaez.core.R
import com.hbaez.core.domain.model.GoalType
import com.hbaez.core_ui.LocalSpacing

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
    viewModel: ChatViewModel = hiltViewModel(),
    chatDefaults: ChatDefaults = ChatDefaults()
) {
    val spacing = LocalSpacing.current
    val state = viewModel.routineFormState
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
                Spacer(modifier = Modifier.height(spacing.spaceMedium))
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
                        modifier = Modifier.clickable {
                            isUpdate = !isUpdate
                        },
                        text = stringResource(id = R.string.update_question),
                        color = chatDefaults.colors.botBalloonTextColor,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
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
                            disabledContainerColor = chatDefaults.colors.inputFieldBackgroundColor,
                            disabledTextColor = if (isUpdate) { chatDefaults.colors.botBalloonColor } else { chatDefaults.colors.botBalloonTextColor },
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
                Spacer(modifier = Modifier.height(spacing.spaceMedium))
                Text(
                    text = stringResource(id = R.string.target_muscle_group),
                    modifier = Modifier.padding(end = spacing.spaceMedium),
                    style = MaterialTheme.typography.bodyLarge,
                    color = chatDefaults.colors.botBalloonTextColor
                )
                Spacer(modifier = Modifier.height(spacing.spaceSmall))
                Row(
                    modifier = Modifier
                        .padding(spacing.spaceMedium)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(id = R.string.primary).uppercase(),
                        style = MaterialTheme.typography.titleSmall,
                        color = chatDefaults.colors.botBalloonTextColor
                    )
                    Text(
                        text = stringResource(id = R.string.secondary).uppercase(),
                        style = MaterialTheme.typography.titleSmall,
                        color = chatDefaults.colors.botBalloonTextColor
                    )
                }
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(spacing.spaceExtraExtraLarge + spacing.spaceExtraLarge)
                        .clip(
                            RoundedCornerShape(8.dp)
                        )
                        .background(color = chatDefaults.colors.botBalloonColor)
                        .border(4.dp, Color.Gray, RoundedCornerShape(4.dp))
                ) {
                    items(state.muscles.size) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = spacing.spaceSmall),
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = state.primaryMuscles.contains(state.muscles[it]),
                                onCheckedChange = { checked ->
                                    if(checked){
                                        viewModel.onEvent(ChatEvent.OnCheckboxFormAdd(state.muscles[it], true))
                                    } else {
                                        viewModel.onEvent(ChatEvent.OnCheckboxFormRemove(state.muscles[it], true))
                                    }
                                },
                                enabled = !state.secondaryMuscles.contains(state.muscles[it]),
                                modifier = Modifier.padding(end = spacing.spaceSmall),
                                colors = CheckboxDefaults.colors(
                                    checkedColor = chatDefaults.colors.botBalloonTextColor,
                                    checkmarkColor = chatDefaults.colors.botBalloonColor,
                                    uncheckedColor = chatDefaults.colors.backgroundColor
                                )
                            )
                            Text(
                                text = state.muscles[it].name,
                                modifier = Modifier.padding(end = spacing.spaceMedium),
                                style = MaterialTheme.typography.bodyMedium,
                                color = chatDefaults.colors.botBalloonTextColor
                            )
                            Checkbox(
                                checked = state.secondaryMuscles.contains(state.muscles[it]),
                                onCheckedChange = { checked ->
                                    if(checked){
                                        viewModel.onEvent(ChatEvent.OnCheckboxFormAdd(state.muscles[it], false))
                                    } else {
                                        viewModel.onEvent(ChatEvent.OnCheckboxFormRemove(state.muscles[it], false))
                                    }
                                },
                                enabled = !state.primaryMuscles.contains(state.muscles[it]),
                                modifier = Modifier.padding(end = spacing.spaceSmall),
                                colors = CheckboxDefaults.colors(
                                    checkedColor = chatDefaults.colors.botBalloonTextColor,
                                    checkmarkColor = chatDefaults.colors.botBalloonColor,
                                    uncheckedColor = chatDefaults.colors.backgroundColor
                                )
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(spacing.spaceMedium))
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(id = R.string.lose_keep_or_gain_weight),
                        color = chatDefaults.colors.botBalloonTextColor,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.height(spacing.spaceMedium))
                    Row {
                        SelectableButton(
                            text = stringResource(id = R.string.lose),
                            isSelected = state.weightGoalType is GoalType.LoseWeight,
                            color = chatDefaults.colors.botBalloonTextColor,
                            selectedTextColor = chatDefaults.colors.botBalloonColor,
                            onClick = {
                                viewModel.onEvent(ChatEvent.OnGoalTypeSelect(GoalType.LoseWeight))
                            },
                            textStyle = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Normal
                            )
                        )
                        Spacer(modifier = Modifier.width(spacing.spaceMedium))
                        SelectableButton(
                            text = stringResource(id = R.string.keep),
                            isSelected = state.weightGoalType is GoalType.KeepWeight,
                            color = chatDefaults.colors.botBalloonTextColor,
                            selectedTextColor = chatDefaults.colors.botBalloonColor,
                            onClick = {
                                viewModel.onEvent(ChatEvent.OnGoalTypeSelect(GoalType.KeepWeight))
                            },
                            textStyle = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Normal
                            )
                        )
                        Spacer(modifier = Modifier.width(spacing.spaceMedium))
                        SelectableButton(
                            text = stringResource(id = R.string.gain),
                            isSelected = state.weightGoalType is GoalType.GainWeight,
                            color = chatDefaults.colors.botBalloonTextColor,
                            selectedTextColor = chatDefaults.colors.botBalloonColor,
                            onClick = {
                                viewModel.onEvent(ChatEvent.OnGoalTypeSelect(GoalType.GainWeight))
                            },
                            textStyle = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Normal
                            )
                        )
                    }
                }
                Spacer(modifier = Modifier.height(spacing.spaceMedium))
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(id = R.string.time_limit),
                        color = chatDefaults.colors.botBalloonTextColor,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.height(spacing.spaceMedium))
                    Row {
                        SelectableButton(
                            text = stringResource(id = R.string.time_limit_low),
                            isSelected = state.timeLimit == 30,
                            color = chatDefaults.colors.botBalloonTextColor,
                            selectedTextColor = chatDefaults.colors.botBalloonColor,
                            onClick = {
                                viewModel.onEvent(ChatEvent.OnTimeLimitSelect(30))
                            },
                            textStyle = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Normal
                            )
                        )
                        Spacer(modifier = Modifier.width(spacing.spaceMedium))
                        SelectableButton(
                            text = stringResource(id = R.string.time_limit_med),
                            isSelected = state.timeLimit == 60,
                            color = chatDefaults.colors.botBalloonTextColor,
                            selectedTextColor = chatDefaults.colors.botBalloonColor,
                            onClick = {
                                viewModel.onEvent(ChatEvent.OnTimeLimitSelect(60))
                            },
                            textStyle = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Normal
                            )
                        )
                        Spacer(modifier = Modifier.width(spacing.spaceMedium))
                        SelectableButton(
                            text = stringResource(id = R.string.time_limit_hi),
                            isSelected = state.timeLimit == 90,
                            color = chatDefaults.colors.botBalloonTextColor,
                            selectedTextColor = chatDefaults.colors.botBalloonColor,
                            onClick = {
                                viewModel.onEvent(ChatEvent.OnTimeLimitSelect(90))
                            },
                            textStyle = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Normal
                            )
                        )
                    }
                }
            }
        }
    }
}