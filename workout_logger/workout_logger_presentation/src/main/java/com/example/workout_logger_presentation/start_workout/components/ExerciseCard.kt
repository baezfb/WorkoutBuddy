package com.example.workout_logger_presentation.start_workout.components

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
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
import com.example.workout_logger_presentation.start_workout.LoggerListState
import com.hbaez.core_ui.LocalSpacing
import com.hbaez.core.R
import com.hbaez.user_auth_presentation.model.WorkoutTemplate

@ExperimentalCoilApi
@Composable
fun ExerciseCard(
    page: Int,
    loggerListState: LoggerListState,
    workoutTemplates: State<List<WorkoutTemplate>>,
    onRepsChange: (reps: String, index: Int, id: Int) -> Unit,
    onWeightChange: (weight: String, index: Int, id: Int) -> Unit,
    onCheckboxChange: (isCompleted: Boolean, index: Int, id: Int, page: Int) -> Unit
){
    val spacing = LocalSpacing.current
    Card(
        shape = RoundedCornerShape(8.dp),
        backgroundColor = MaterialTheme.colors.background,
        modifier = Modifier
            .clip(
                RoundedCornerShape(50.dp)
            )
            .border(2.dp, MaterialTheme.colors.primary, RoundedCornerShape(50.dp))
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
                    style = MaterialTheme.typography.body2
                )
                Text(
                    text= stringResource(id = R.string.weight),
                    style = MaterialTheme.typography.body2
                )
                Text(
                    text= stringResource(id = R.string.reps),
                    style = MaterialTheme.typography.body2
                )
                Text(
                    text= stringResource(id = R.string.completed_question),
                    style = MaterialTheme.typography.body2
                )
            }
            LazyColumn{
                List(loggerListState.sets.toInt()) { it + 1 }.forEach {
                    Log.println(Log.DEBUG, "exercisecard", it.toString())
                    item {
                        ExerciseCardRow(
                            it,
                            loggerListState,
                            workoutTemplates.value[page],
                            it - 1,
                            onRepsChange,
                            onWeightChange,
                            onCheckboxChange,
                            page
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ExerciseCardRow(
    set: Int,
    loggerListState: LoggerListState,
    workoutTemplate: WorkoutTemplate,
    index: Int,
    onRepsChange: (reps: String, index: Int, id: Int) -> Unit,
    onWeightChange: (weight: String, index: Int, id: Int) -> Unit,
    onCheckboxChange: (isCompleted: Boolean, index: Int, id: Int, page: Int) -> Unit,
    page: Int
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
            style = MaterialTheme.typography.body2
        )
        TextField(
            modifier = Modifier.weight(1f),
            placeholder = { Text(text = loggerListState.origWeight) },
            value = loggerListState.weightList[index],
            textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
            singleLine = true,
            onValueChange = { onWeightChange(it, index, loggerListState.id) },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onDone = {
                    defaultKeyboardAction(ImeAction.Next)
                }
            ),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = MaterialTheme.colors.background,
                focusedIndicatorColor = MaterialTheme.colors.primaryVariant,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
        TextField(
            modifier = Modifier.weight(1f),
            placeholder = { Text(text = loggerListState.origReps.toString()) },
            value = loggerListState.repsList[index],
            textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
            singleLine = true,
            onValueChange = { onRepsChange(it, index, loggerListState.id) },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onDone = {
                    defaultKeyboardAction(ImeAction.Next)
                }
            ),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = MaterialTheme.colors.background,
                focusedIndicatorColor = MaterialTheme.colors.primaryVariant,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
        Checkbox(
            modifier = Modifier.weight(1f),
            checked = loggerListState.isCompleted[index],
            onCheckedChange = {
                onCheckboxChange(it, index, loggerListState.id, page)
            },
            colors = CheckboxDefaults.colors(
                checkedColor = Color.DarkGray /* TODO */
            )
        )

    }
}