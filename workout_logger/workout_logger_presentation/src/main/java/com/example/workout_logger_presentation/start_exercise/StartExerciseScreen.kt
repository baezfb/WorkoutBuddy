package com.example.workout_logger_presentation.start_exercise

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.annotation.ExperimentalCoilApi
import com.example.workout_logger_presentation.components.IconButton
import com.example.workout_logger_presentation.start_exercise.TimerStatus
import com.example.workout_logger_presentation.start_exercise.components.ExerciseCard
import com.example.workout_logger_presentation.start_workout.StartWorkoutEvent
import com.example.workout_logger_presentation.start_workout.components.NotificationUtil
import com.example.workout_logger_presentation.start_workout.components.Timer
import com.hbaez.core.util.UiEvent
import com.hbaez.core_ui.LocalSpacing
import java.time.Duration

@RequiresApi(Build.VERSION_CODES.S)
@OptIn(ExperimentalCoilApi::class)
@Composable
fun StartExerciseScreen(
    exerciseName: String,
    dayOfMonth: Int,
    month: Int,
    year: Int,
    onNavigateUp: () -> Unit,
    viewModel: StartExerciseViewModel = hiltViewModel()
){
    val spacing = LocalSpacing.current
    val state = viewModel.state
    val showExerciseInfoDialog = remember { mutableStateOf(false) }

    LaunchedEffect(Unit){
        viewModel.uiEvent.collect{ event ->
            when(event) {
                is UiEvent.NavigateUp -> onNavigateUp()
                else -> Unit
            }
        }
    }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
//                Text(
//                    modifier = Modifier
//                        .padding(spacing.spaceMedium),
//                    text = workoutName.uppercase(),
//                    style = MaterialTheme.typography.h2
//                )
                IconButton(
                    modifier = Modifier
                        .padding(top = spacing.spaceMedium, end = spacing.spaceMedium, bottom = spacing.spaceLarge),
                    onClick = {
//                        viewModel.onEvent(StartWorkoutEvent.OnSubmitWorkout(state.workoutName, state.loggerListStates, workoutTemplates.value, dayOfMonth, month, year))
                    },
                    icon = Icons.Default.Done
                )
            }
        },
        content = { padding ->
            Spacer(modifier = Modifier.height(spacing.spaceSmall))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(padding),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Row{
                    Text(
                        text = state.exerciseName,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        style = MaterialTheme.typography.h3,
                        modifier = Modifier.fillMaxWidth(.75f)
                    )
                    Spacer(modifier = Modifier.width(spacing.spaceSmall))
                    IconButton(
                        onClick = {
                            showExerciseInfoDialog.value = true
                        },
                        icon = Icons.Default.Info,
                        padding = 0.dp
                    )
                }
                Spacer(modifier = Modifier.height(spacing.spaceMedium))
                ExerciseCard(
                    timerStatus = state.timerStatus,
                    sets = state.sets,
                    isCompleted = state.isCompleted,
                    reps = state.reps,
                    weight = state.weight,
                    onRepsChange = { reps, index ->
//                        viewModel.onEvent(StartWorkoutEvent.OnRepsChange(reps = reps, index = index, rowId = id, page = page))
                    },
                    onWeightChange = { weight, index ->
//                        viewModel.onEvent(StartWorkoutEvent.OnWeightChange(weight = weight, index = index, rowId = id, page = page))
                    },
                    onCheckboxChange = { isChecked, index ->
//                        if(isChecked && state.currRunningIndex != index && state.timerStatus == TimerStatus.RUNNING){ // non checked clicked while timer already running
//                            // DO NOTHING
////                                viewModel.onEvent(StartWorkoutEvent.OnCheckboxChange(isChecked= false, timerStatus = TimerStatus.RUNNING, currRunningIndex = state.currRunningIndex, index = index, rowId = id, page = page))
//                        }
//                        if(isChecked && (state.timerStatus == TimerStatus.START || state.timerStatus == TimerStatus.FINISHED)){ // non checked clicked while timer not running
//                            viewModel.onEvent(StartWorkoutEvent.OnCheckboxChange(isChecked= true, timerStatus = TimerStatus.RUNNING, currRunningIndex = index, index = index, rowId = id, page = page, shouldUpdateTime = true))
//                            val wakeupTime = StartWorkoutViewModel.setAlarm(context = context, timeDuration = Duration.ofSeconds((currentExercise.rest.getOrElse(index) { currentExercise.rest.last() }).toLong()))
//                            NotificationUtil.showTimerRunning(context, wakeupTime)
//                            viewModel.onEvent(StartWorkoutEvent.ChangeCheckboxColor(color = Color(255,153,51), id = id, index = index))
//                        }
//                        else if(!isChecked && state.currRunningIndex == index && state.timerStatus == TimerStatus.RUNNING){ // checked clicked while that row has timer running
//                            viewModel.onEvent(StartWorkoutEvent.OnCheckboxChange(isChecked= true, timerStatus = TimerStatus.FINISHED, currRunningIndex = -1, index = index, rowId = id, page = page, shouldUpdateTime = true))
//                            StartWorkoutViewModel.removeAlarm(context)
//                            NotificationUtil.hideTimerNotification(context)
//                            viewModel.onEvent(StartWorkoutEvent.ChangeCheckboxColor(color = Color.DarkGray, id = id, index = index))
//                        }
//                        else if(!isChecked && state.currRunningIndex != index){ // checked clicked while that row does not have timer running
//                            viewModel.onEvent(StartWorkoutEvent.OnCheckboxChange(isChecked= false, timerStatus = state.timerStatus, currRunningIndex = state.currRunningIndex, index = index, rowId = id, page = page, shouldUpdateTime = false))
//                        }
                    },
                    onRemoveSet = {
//                        viewModel.onEvent(StartWorkoutEvent.OnRemoveSet(page))
                    },
                    onAddSet = {
//                        viewModel.onEvent(StartWorkoutEvent.OnAddSet(page))
                    }
                )
            }
        },
        bottomBar = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ){
                Timer(
                    modifier = Modifier
                        .size(200.dp),
                    timerJump = state.timerJump,
                    handleColor = MaterialTheme.colors.secondary,
                    inactiveBarColor = MaterialTheme.colors.primaryVariant,
                    activeBarColor = MaterialTheme.colors.primary
                )
            }
        }
    )
}