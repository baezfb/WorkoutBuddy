package com.example.workout_logger_presentation.start_exercise.components

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import com.example.workout_logger_presentation.start_exercise.TimerStatus
import com.hbaez.core.R
import com.hbaez.core_ui.LocalSpacing
import com.hbaez.user_auth_presentation.model.WorkoutTemplate

@ExperimentalCoilApi
@Composable
fun ExerciseCard(
    timerStatus: TimerStatus,
    sets: String,
    isCompleted: List<Boolean>,
    reps: List<String>,
    weight: List<String>,
    onRepsChange: (reps: String, index: Int) -> Unit,
    onWeightChange: (weight: String, index: Int) -> Unit,
    onCheckboxChange: (isCompleted: Boolean, index: Int) -> Unit,
    onRemoveSet: () -> Unit,
    onAddSet: () -> Unit
){
    val spacing = LocalSpacing.current
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        modifier = Modifier
            .clip(
                RoundedCornerShape(50.dp)
            )
            .border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(50.dp))
            .padding(spacing.spaceMedium)
            .width(275.dp)
            .height(325.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text= stringResource(id = R.string.sets),
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text= stringResource(id = R.string.weight),
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text= stringResource(id = R.string.reps),
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text= stringResource(id = R.string.completed_question),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            LazyColumn{
                List(sets.toInt()) { it }.forEach {
                    item {
                        ExerciseCardRow(
                            set = it + 1,
                            reps = reps,
                            weight = weight,
                            isCompleted = isCompleted,
                            index = it,
                            onRepsChange = onRepsChange,
                            onWeightChange = onWeightChange,
                            onCheckboxChange = onCheckboxChange
                        )
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(spacing.spaceMedium))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(100f))
                                .clickable(enabled = sets.toInt() > 1 && timerStatus != TimerStatus.RUNNING) { onRemoveSet() }
                                .border(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.background,
                                    shape = RoundedCornerShape(100f)
                                )
                                .padding(spacing.spaceMedium),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Del Set",
                                maxLines = 2,
                                style = MaterialTheme.typography.labelLarge,
                                color = if(sets.toInt() > 1 && timerStatus != TimerStatus.RUNNING) MaterialTheme.colorScheme.primary else Color.DarkGray
                            )
                        }
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(100f))
                                .clickable { onAddSet() }
                                .border(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.background,
                                    shape = RoundedCornerShape(100f)
                                )
                                .padding(spacing.spaceMedium),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Add Set",
                                maxLines = 2,
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ExerciseCardRow(
    set: Int,
    reps: List<String>,
    weight: List<String>,
    isCompleted: List<Boolean>,
    index: Int,
    onRepsChange: (reps: String, index: Int) -> Unit,
    onWeightChange: (weight: String, index: Int) -> Unit,
    onCheckboxChange: (isCompleted: Boolean, index: Int) -> Unit
){
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text= set.toString(),
            style = MaterialTheme.typography.bodyMedium
        )
        TextField(
            modifier = Modifier.weight(1f),
            placeholder = { Text(text = "100") }, /*TODO replace with preferences*/
            value = weight[index],
            textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
            singleLine = true,
            onValueChange = { onWeightChange(it, index) },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onDone = {
                    defaultKeyboardAction(ImeAction.Next)
                }
            ),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.background,
                focusedIndicatorColor = MaterialTheme.colorScheme.secondary,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
        TextField(
            modifier = Modifier.weight(1f),
            placeholder = { Text(text = "10") }, /*TODO replace with preferences*/
            value = reps[index],
            textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
            singleLine = true,
            onValueChange = { onRepsChange(it, index) },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onDone = {
                    defaultKeyboardAction(ImeAction.Next)
                }
            ),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.background,
                focusedIndicatorColor = MaterialTheme.colorScheme.secondary,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
        Checkbox(
            modifier = Modifier.weight(1f),
            checked = isCompleted[index],
            onCheckedChange = {
                onCheckboxChange(it, index)
            },
            colors = CheckboxDefaults.colors(
                checkedColor = Color.DarkGray /* TODO */
            )
        )

    }
}