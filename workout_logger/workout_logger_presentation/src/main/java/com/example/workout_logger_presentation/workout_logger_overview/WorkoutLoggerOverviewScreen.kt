package com.example.workout_logger_presentation.workout_logger_overview

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.runtime.Composable
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
import com.example.workout_logger_presentation.create_exercise.model.Muscle
import com.example.workout_logger_presentation.workout_logger_overview.components.CompletedWorkoutItem
import com.example.workout_logger_presentation.workout_logger_overview.components.ExerciseDialog
import com.example.workout_logger_presentation.workout_logger_overview.components.ExerciseRow
import com.example.workout_logger_presentation.workout_logger_overview.components.OptionsHeaderDialog
import com.example.workout_logger_presentation.workout_logger_overview.components.WorkoutDialog
import com.hbaez.core_ui.LocalSpacing
import com.hbaez.core.R

@OptIn(ExperimentalLifecycleComposeApi::class)
@ExperimentalCoilApi
@Composable
fun WorkoutLoggerOverviewScreen(
    onNavigateToCreateWorkout: () -> Unit,
    onNavigateToEditWorkout: (workoutName: String, workoutIds: String) -> Unit,
    onNavigateToStartWorkout: (workoutName: String, day: Int, month: Int, year: Int, workoutIds: String) -> Unit,
    onNavigateToCreateExercise: () -> Unit,
    onNavigateToEditExercise: (exerciseName: String, description: String, primaryMuscles: String?, secondaryMuscles: String?, imageURL: List<String>) -> Unit,
    onNavigateToStartExercise: (exerciseName: String, day: Int, month: Int, year: Int) -> Unit,
    viewModel: WorkoutLoggerOverviewModel = hiltViewModel()
) {
    val spacing = LocalSpacing.current
    val state = viewModel.state
    val workoutTemplates = viewModel.workoutTemplates.collectAsStateWithLifecycle(emptyList())
    val context = LocalContext.current
    val showDialog = remember { mutableStateOf(false) }
    val showDialogEdit = remember { mutableStateOf(false) }
    val showExercise = remember { mutableStateOf(false) }
    val showExerciseEdit = remember { mutableStateOf(false) }
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
                modifier = Modifier.fillMaxWidth()
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
                    onDismiss = {
                        showDialog.value = false
                        showDialogEdit.value = false
                                },
                    onChooseWorkout = { workoutName, workoutIds ->
                        if(showDialogEdit.value){
                            onNavigateToEditWorkout(
                                workoutName,
                                workoutIds
                            )
                        } else {
                            onNavigateToStartWorkout(
                                workoutName,
                                state.date.dayOfMonth,
                                state.date.monthValue,
                                state.date.year,
                                workoutIds
                            )
                        }
                                      },
                    workoutNames = state.workoutNames,
                    workoutTemplates = workoutTemplates
                )
            }
            if(showExercise.value){
                ExerciseDialog(
                    filterText = state.exerciseFilterText,
                    trackableExercises = state.trackableExercise,
                    onDismiss = {
                        showExercise.value = false
                        showExerciseEdit.value = false
                                },
                    onChooseExercise = {
                        if(showExerciseEdit.value){
                            onNavigateToEditExercise(
                                it.exercise.name!!,
                                it.exercise.description ?: "",
                                it.exercise.muscle_name_main,
                                it.exercise.muscle_name_secondary,
                                it.exercise.image_url.filterNotNull(),

                            )
                        } else {
                            onNavigateToStartExercise(
                                it.exercise.name!!,
                                state.date.dayOfMonth,
                                state.date.monthValue,
                                state.date.year,
                            )
                        }
                    },
                    onFilterTextChange = {
                        viewModel.onEvent(WorkoutLoggerOverviewEvent.OnExerciseSearch(it))
                    },
                    onItemClick = {
                        viewModel.onEvent(WorkoutLoggerOverviewEvent.OnExerciseItemClick(it))
                    },
                    onDescrClick = {
                        viewModel.onEvent(WorkoutLoggerOverviewEvent.OnExerciseDescrClick(it))
                    }
                )
            }
            if(showOptionsHeaderDialog.value){
                when(optionsHeaderType.value) {
                    "workout" -> {
                        OptionsHeaderDialog(
                            onDismiss = { showOptionsHeaderDialog.value = false },
                            onClickCreate = {
                                onNavigateToCreateWorkout()
                            },
                            onClickEdit = {
                                showDialog.value = true
                                showDialogEdit.value = true
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
                                onNavigateToCreateExercise()
                            },
                            onClickEdit = {
                                showExercise.value = true
                                showExerciseEdit.value = true
                            },
                            title = R.string.create_edit_exercise,
                            text1 = stringResource(id = R.string.create_exercise),
                            text2 = stringResource(id = R.string.edit_exercise)
                        )
                    }
                }
            }
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
                    onClick = {
                        showExercise.value = true
                        showExerciseEdit.value = false
                              },
                    modifier = Modifier.weight(1f),
                    color = MaterialTheme.colors.primary,
                    icon = Icons.Default.AddCircle
                )
            }
            Spacer(modifier = Modifier.height(spacing.spaceMedium))
        }
        if(viewModel.completedWorkouts.size > 0){

        }
        itemsIndexed(viewModel.completedWorkouts){ index, completedWorkout -> //items(state.completedWorkouts){ completedWorkout ->
            if(index < state.completedWorkoutIsExpanded.size){
                CompletedWorkoutItem(
                    workout = completedWorkout,
                    imageUrl = if(viewModel.imageUrls.containsKey(completedWorkout.exerciseName)) viewModel.imageUrls[completedWorkout.exerciseName]!! else "",
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
                            val weight = completedWorkout.weight
                            val reps = completedWorkout.reps
                            val isCompleted = completedWorkout.isCompleted
                            for(i in 1..completedWorkout.sets){
                                ExerciseRow(
                                    set = i,
                                    reps = reps[i-1].toInt(),
                                    weight = weight[i-1].toInt(),
                                    completed = isCompleted[i-1].toBoolean()
                                )
                                if(i != completedWorkout.sets) Divider(color = MaterialTheme.colors.primaryVariant, thickness = 1.dp)
                            }
                            Spacer(modifier = Modifier.height(spacing.spaceExtraSmall))
                        }
                    },
//                color = MaterialTheme.colors.primaryVariant
                )
                Spacer(modifier = Modifier.height(spacing.spaceMedium))
            }
        }
    }
}