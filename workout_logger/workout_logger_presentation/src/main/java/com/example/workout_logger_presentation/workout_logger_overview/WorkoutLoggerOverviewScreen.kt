package com.example.workout_logger_presentation.workout_logger_overview

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.annotation.ExperimentalCoilApi
import com.example.workout_logger_presentation.components.AddButton
import com.example.workout_logger_presentation.components.DaySelector
import com.example.workout_logger_presentation.components.OptionsHeader
import com.example.workout_logger_presentation.workout_logger_overview.components.CompletedWorkoutItem
import com.example.workout_logger_presentation.workout_logger_overview.components.ExerciseRow
import com.example.workout_logger_presentation.workout_logger_overview.components.OptionsHeaderDialog
import com.example.workout_logger_presentation.workout_logger_overview.components.WorkoutDialog
import com.hbaez.core_ui.LocalSpacing
import com.hbaez.core.R
import com.hbaez.user_auth_presentation.model.WorkoutTemplate

@OptIn(ExperimentalLifecycleComposeApi::class)
@ExperimentalCoilApi
@Composable
fun WorkoutLoggerOverviewScreen(
    onNavigateToCreate: () -> Unit,
    onNavigateToWorkout: (workoutName: String, day: Int, month: Int, year: Int, workoutIds: String) -> Unit,
    viewModel: WorkoutLoggerOverviewModel = hiltViewModel()
) {
    val spacing = LocalSpacing.current
    val state = viewModel.state
    val workoutTemplates = viewModel.workoutTemplates.collectAsStateWithLifecycle(emptyList())
    val context = LocalContext.current
    val showDialog = remember { mutableStateOf(false) }
    val showOptionsHeaderDialog = remember { mutableStateOf(false) }
    val optionsHeaderType = remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = spacing.spaceMedium)
    ) {
        item {
            Log.println(Log.DEBUG, "showWorkoutDialog", state.showWorkoutDialog.toString())
            OptionsHeader(
                optionsHeaderDialog = { type ->
                    if(type == "workout"){
                        showOptionsHeaderDialog.value = true
                        optionsHeaderType.value = type
                    } else {
                        showOptionsHeaderDialog.value = true
                        optionsHeaderType.value = type
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                displayWorkouts = {

                }
            )
            Spacer(modifier = Modifier.height(spacing.spaceMedium))
            DaySelector(
                date = state.date,
                onPreviousDayClick = {
                    viewModel.onEvent(WorkoutLoggerOverviewEvent.OnPreviousDayClick)
                },
                onNextDayClick = {
                    viewModel.onEvent(WorkoutLoggerOverviewEvent.OnNextDayClick)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = spacing.spaceMedium)
            )
            Spacer(modifier = Modifier.height(spacing.spaceMedium))

            if(showDialog.value){
                WorkoutDialog(
                    onDismiss = { showDialog.value = false },
                    onChooseWorkout = { workoutName, workoutIds ->
                                        onNavigateToWorkout(
                                            workoutName,
                                            state.date.dayOfMonth,
                                            state.date.monthValue,
                                            state.date.year,
                                            workoutIds
                                        )
                                      },
                    workoutNames = state.workoutNames,
                    workoutTemplates = workoutTemplates
                )
            }
            if(showOptionsHeaderDialog.value){
                when(optionsHeaderType.value) {
                    "workout" -> {
                        OptionsHeaderDialog(
                            onDismiss = { showOptionsHeaderDialog.value = false },
                            onClickCreate = {
                                onNavigateToCreate()
                            },
                            onClickEdit = {
                                /*TODO*/
                            },
                            title = R.string.create_edit_workout,
                            text1 = stringResource(id = R.string.create_workout),
                            text2 = stringResource(id = R.string.edit_workout)
                        )
                    }
                    "exercise" -> {
                        OptionsHeaderDialog(
                            onDismiss = { showOptionsHeaderDialog.value = false },
                            onClickCreate = {
                                /*TODO*/
                            },
                            onClickEdit = {
                                /*TODO*/
                            },
                            title = R.string.create_edit_exercise,
                            text1 = stringResource(id = R.string.create_exercise),
                            text2 = stringResource(id = R.string.edit_exercise)
                        )
                    }
                }
            }
        }
        itemsIndexed(viewModel.completedWorkouts){ index, completedWorkout -> //items(state.completedWorkouts){ completedWorkout ->
            CompletedWorkoutItem(
                workout = completedWorkout,
                isExpanded = state.completedWorkoutIsExpanded[index],
                modifier = Modifier,
                onClick = {
                    viewModel.onEvent(WorkoutLoggerOverviewEvent.OnCompletedWorkoutClick(index))
                },
                content = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = spacing.spaceSmall)
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
                        val weight = completedWorkout.weight.removeSurrounding("[","]").split(",").toList().map{ it.trim().toInt() }
                        val reps = completedWorkout.reps.removeSurrounding("[","]").split(",").toList().map{ it.trim().toInt() }
                        for(i in 1..completedWorkout.sets){
                            if(i <= reps.size){
                                ExerciseRow(
                                    set = i,
                                    reps = reps[i-1],
                                    weight = weight[i-1],
                                    completed = true
                                )
                            }
                            else {
                                ExerciseRow(
                                    set = i,
                                    reps = 0,
                                    weight = 0,
//                                    rest = 0,
                                    completed = false
                                )
                            }
                            if(i != completedWorkout.sets) Divider(color = MaterialTheme.colors.primaryVariant, thickness = 1.dp)
                        }
                        Spacer(modifier = Modifier.height(spacing.spaceExtraSmall))
                    }
                },
//                color = MaterialTheme.colors.primaryVariant
            )
            Spacer(modifier = Modifier.height(spacing.spaceMedium))
        }
        item{
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                AddButton(
                    text = stringResource(id = R.string.start_workout),
                    onClick = {
                        viewModel.onEvent(WorkoutLoggerOverviewEvent.OnStartWorkoutClick)
                        showDialog.value = true
                    },
                    modifier = Modifier.weight(1f),
                    color = MaterialTheme.colors.primary,
                    icon = Icons.Default.AddCircle
                )
                Spacer(modifier = Modifier.width(spacing.spaceExtraSmall))
                AddButton(
                    text = stringResource(id = R.string.start_exercise),
                    onClick = { /*TODO*/ },
                    modifier = Modifier.weight(1f),
                    color = MaterialTheme.colors.primary,
                    icon = Icons.Default.AddCircle
                )
            }
        }
    }
}