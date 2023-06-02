package com.example.workout_logger_presentation.start_workout

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.annotation.ExperimentalCoilApi
import com.example.workout_logger_presentation.components.IconButton
import com.example.workout_logger_presentation.start_workout.components.Timer
import com.example.workout_logger_presentation.start_workout.components.ExerciseCard
import com.example.workout_logger_presentation.start_workout.components.NotificationUtil
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.hbaez.core.util.UiEvent
import com.hbaez.core_ui.LocalSpacing
import com.hbaez.user_auth_presentation.model.WorkoutTemplate
import java.time.Duration

@RequiresApi(Build.VERSION_CODES.S)
@OptIn(ExperimentalPagerApi::class, ExperimentalLifecycleComposeApi::class
)
@ExperimentalCoilApi
@Composable
fun StartWorkoutScreen(
    workoutName: String,
    dayOfMonth: Int,
    month: Int,
    year: Int,
    onNavigateUp: () -> Unit,
    viewModel: StartWorkoutViewModel = hiltViewModel()
){
    val spacing = LocalSpacing.current
    val state = viewModel.state
    val workoutIds = viewModel.workoutIds
    val context = LocalContext.current
    val pagerState = rememberPagerState(initialPage = 0)
    val workoutTemplates = viewModel.workoutTemplates.collectAsStateWithLifecycle(emptyList())
    var count = 0
    workoutTemplates.value.forEach {
        if(it.name == workoutName){
            count += 1
        }
    }

    LaunchedEffect(Unit){
        viewModel.uiEvent.collect{ event ->
            when(event) {
//                is UiEvent.ShowSnackbar -> {
//                    scaffoldState.snackbarHostState.showSnackbar(
//                        message = event.message.asString(context)
//                    )
//                }
                is UiEvent.NavigateUp -> onNavigateUp()
                else -> Unit
            }
        }
    }
    Scaffold(
        topBar = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    modifier = Modifier
                        .padding(spacing.spaceMedium),
                    text = workoutName.uppercase(),
                    style = MaterialTheme.typography.h2
                )
                IconButton(
                    modifier = Modifier
                        .padding(top = spacing.spaceMedium, end = spacing.spaceMedium),
                    onClick = {
                                viewModel.onEvent(StartWorkoutEvent.OnSubmitWorkout(state.workoutName, state.loggerListStates, dayOfMonth, month, year))
                              },
                    icon = Icons.Default.Done
                )
            }
        },
        content = { padding ->
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .scrollEnabled(state.timerStatus != TimerStatus.RUNNING),
                verticalAlignment = Alignment.CenterVertically,
                count = count,
                contentPadding = PaddingValues(spacing.spaceSmall)
            ) {page ->
                var currExercise: WorkoutTemplate
                var loggerListState: LoggerListState
                workoutTemplates.value.forEach {
                    if(it.name == workoutName){
                        if (it.rowId == workoutIds[page].toInt()){
                            currExercise = it
                            if(state.loggerListStates.size > page && state.loggerListStates[page].id != it.rowId){
                                loggerListState = LoggerListState(
                                    id = it.rowId,
                                    exerciseName = it.exerciseName,
                                    exerciseId = it.exerciseId,
                                    timerStatus = TimerStatus.START,
                                    sets = it.sets.toString(),
                                    rest = it.rest.toString(),
                                    repsList = List(it.sets) { _ -> it.reps.toString() },
                                    weightList = List(it.sets) { _ -> it.weight.toString() },
                                    isCompleted = List(it.sets) { false },
                                    checkedColor = List(it.sets) { Color.DarkGray },
                                    origRest = it.rest.toString(),
                                    origReps = it.reps.toString(),
                                    origWeight = it.weight.toString()
                                )
                                viewModel.onEvent(StartWorkoutEvent.AddLoggerList(loggerListState))
                            }
                            else if (state.loggerListStates.size == page) {
                                loggerListState = LoggerListState(
                                    id = it.rowId,
                                    exerciseName = it.exerciseName,
                                    exerciseId = it.exerciseId,
                                    timerStatus = TimerStatus.START,
                                    sets = it.sets.toString(),
                                    rest = it.rest.toString(),
                                    repsList = List(it.sets) { "" },
                                    weightList = List(it.sets) { "" },
                                    isCompleted = List(it.sets) { false },
                                    checkedColor = List(it.sets) { Color.DarkGray },
                                    origRest = it.rest.toString(),
                                    origReps = it.reps.toString(),
                                    origWeight = it.weight.toString()
                                )
                                viewModel.onEvent(StartWorkoutEvent.AddLoggerList(loggerListState))
                            }
                            return@forEach
                        }
                    }
                }

                if(pagerState.targetPage != pagerState.currentPage){
                    viewModel.onEvent(StartWorkoutEvent.OnChangePage(pagerState.targetPage))
                }
                Column{
                    Row{
                        Text(
                            text = state.loggerListStates[page].exerciseName,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1,
                            style = MaterialTheme.typography.h3,
                            modifier = Modifier.fillMaxWidth(.75f)
                        )
                        Spacer(modifier = Modifier.width(spacing.spaceSmall))
                        IconButton(
                            onClick = { /*TODO*/ },
                            icon = Icons.Default.Info,
                            padding = 0.dp
                        )
                    }
                    Spacer(modifier = Modifier.height(spacing.spaceMedium))
                    ExerciseCard(
                        page = page,
                        loggerListState = state.loggerListStates[page],
                        workoutTemplates = workoutTemplates,
                        onRepsChange = { reps, index, id ->
                            viewModel.onEvent(StartWorkoutEvent.OnRepsChange(reps = reps, index = index, rowId = id))
                        },
                        onWeightChange = { weight, index, id ->
                            viewModel.onEvent(StartWorkoutEvent.OnWeightChange(weight = weight, index = index, rowId = id))
                        },
                        onCheckboxChange = { isChecked, index, id, page ->
                            if(isChecked && state.currRunningIndex != index && state.timerStatus == TimerStatus.RUNNING){ // non checked clicked while timer already running
                                viewModel.onEvent(StartWorkoutEvent.OnCheckboxChange(isChecked= false, timerStatus = TimerStatus.RUNNING, currRunningIndex = state.currRunningIndex, index = index, rowId = id, page = page))
                            }
                            if(isChecked && (state.timerStatus == TimerStatus.START || state.timerStatus == TimerStatus.FINISHED)){ // non checked clicked while timer not running
                                viewModel.onEvent(StartWorkoutEvent.OnCheckboxChange(isChecked= true, timerStatus = TimerStatus.RUNNING, currRunningIndex = index, index = index, rowId = id, page = page))
                                val wakeupTime = StartWorkoutViewModel.setAlarm(context = context, timeDuration = Duration.ofSeconds(workoutTemplates.value[page].rest.toLong()))
                                NotificationUtil.showTimerRunning(context, wakeupTime)
                                viewModel.onEvent(StartWorkoutEvent.ChangeCheckboxColor(color = Color(255,153,51), id = id, index = index))
                            }
                            else if(!isChecked && state.currRunningIndex == index && state.timerStatus == TimerStatus.RUNNING){ // checked clicked while that row has timer running
                                viewModel.onEvent(StartWorkoutEvent.OnCheckboxChange(isChecked= true, timerStatus = TimerStatus.FINISHED, currRunningIndex = -1, index = index, rowId = id, page = page))
                                StartWorkoutViewModel.removeAlarm(context)
                                NotificationUtil.hideTimerNotification(context)
                                viewModel.onEvent(StartWorkoutEvent.ChangeCheckboxColor(color = Color.DarkGray, id = id, index = index))
                            }
                            else if(!isChecked && state.currRunningIndex != index){ // checked clicked while that row does not have timer running
                                viewModel.onEvent(StartWorkoutEvent.OnCheckboxChange(isChecked= false, timerStatus = state.timerStatus, currRunningIndex = state.currRunningIndex, index = index, rowId = id, page = page))
                            }
                        }
                    )
                }
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
                    pagerState = pagerState,
                    handleColor = MaterialTheme.colors.secondary,
                    inactiveBarColor = MaterialTheme.colors.primaryVariant,
                    activeBarColor = MaterialTheme.colors.primary
                )
            }
        }
    )

}

fun Modifier.scrollEnabled(
    enabled: Boolean,
) = nestedScroll(
    connection = object : NestedScrollConnection {
        override fun onPreScroll(
            available: Offset,
            source: NestedScrollSource
        ): Offset = if(enabled) Offset.Zero else available
    }
)
