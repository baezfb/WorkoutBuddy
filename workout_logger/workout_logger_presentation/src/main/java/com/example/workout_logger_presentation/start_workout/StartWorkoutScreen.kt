package com.example.workout_logger_presentation.start_workout

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.annotation.ExperimentalCoilApi
import com.example.workout_logger_presentation.components.ExerciseInfoDialog
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
import kotlinx.coroutines.launch
import java.time.Duration

@RequiresApi(Build.VERSION_CODES.S)
@OptIn(ExperimentalPagerApi::class)
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
    val coroutineScope = rememberCoroutineScope()
    val workoutTemplates = viewModel.workoutTemplates.collectAsStateWithLifecycle(emptyList())
    val workoutExerciseNames: MutableList<String?> = (List(viewModel.workoutIds.size) { null }).toMutableList()
    val count = state.routineWorkoutTemplate.distinctBy { it.position }.size
    val showExerciseInfoDialog = remember { mutableStateOf(false) }
    val showExerciseInfoDialogSuperset = remember { mutableStateOf(false) }
    workoutTemplates.value.forEach {
        if(it.name == workoutName){
            workoutExerciseNames[it.position] = it.exerciseName
        }
        viewModel.onEvent(StartWorkoutEvent.OnUpdateWorkoutName(workoutName))
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

    LaunchedEffect(key1 = pagerState.currentPage, key2 = state.loggerListStates.size){
        if(state.loggerListStates.getOrNull(pagerState.currentPage) != null) {
            viewModel.onEvent(
                StartWorkoutEvent.GetExerciseInfo(pagerState.currentPage)
            )
        }
    }

    if(showExerciseInfoDialog.value && state.exerciseInfo.isNotEmpty()){
        ExerciseInfoDialog(
            trackableExerciseState = state.exerciseInfo.first(),
            onDescrClick = {
                           viewModel.onEvent(StartWorkoutEvent.OnToggleExerciseDescription(state.exerciseInfo.first()))
                           },
            onDismiss = { showExerciseInfoDialog.value = false })
    }
    else if(showExerciseInfoDialogSuperset.value && state.exerciseInfo.isNotEmpty()){
        ExerciseInfoDialog(
            trackableExerciseState = state.exerciseInfo.last(),
            onDescrClick = {
                viewModel.onEvent(StartWorkoutEvent.OnToggleExerciseDescription(state.exerciseInfo.last()))
            },
            onDismiss = { showExerciseInfoDialogSuperset.value = false })
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
                    style = MaterialTheme.typography.displayMedium
                )
                IconButton(
                    borderColor = MaterialTheme.colorScheme.outline,
                    modifier = Modifier
                        .padding(top = spacing.spaceMedium, end = spacing.spaceMedium),
                    onClick = {
                                viewModel.onEvent(StartWorkoutEvent.OnSubmitWorkout(state.workoutName, state.loggerListStates, workoutTemplates.value, dayOfMonth, month, year))
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
                if(pagerState.targetPage != pagerState.currentPage){
                    viewModel.onEvent(StartWorkoutEvent.OnChangePage(pagerState.targetPage))
                }
                // get current exercise from workoutTemplates
                // using workoutName and exerciseName
                val currentExercise = state.routineWorkoutTemplate.filter { it.position == page }
                val currentLoggerState = state.loggerListStates.filter { it.position == page }

                Column{
                    Row{
                        Text(
                            text = "${page + 1}/${count}",
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1,
                            style = MaterialTheme.typography.displaySmall
                        )
                        Spacer(modifier = Modifier.width(spacing.spaceSmall))
                        if(currentExercise.size == 1){
                            Text(
                                text = currentExercise.first().exerciseName,
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1,
                                style = MaterialTheme.typography.displaySmall,
                                modifier = Modifier.fillMaxWidth(.75f)
                            )
                            Spacer(modifier = Modifier.width(spacing.spaceSmall))
                            IconButton(
                                onClick = {
                                    showExerciseInfoDialog.value = true
                                },
                                icon = Icons.Outlined.Info,
                                padding = 0.dp
                            )
                        }
                        else if(currentExercise.size > 1){
                            Row(Modifier.fillMaxWidth(.9f)) {
                                Text(
                                    text = currentExercise.first().exerciseName,
                                    overflow = TextOverflow.Ellipsis,
                                    maxLines = 1,
                                    style = MaterialTheme.typography.displaySmall,
                                    modifier = Modifier.weight(.8f)
                                )
                                Spacer(modifier = Modifier.width(spacing.spaceSmall))
                                IconButton(
                                    onClick = {
                                        showExerciseInfoDialog.value = true
                                    },
                                    icon = Icons.Outlined.Info,
                                    padding = 0.dp
                                )
                                Spacer(modifier = Modifier.width(spacing.spaceSmall))
                                Text(
                                    text = currentExercise.last().exerciseName,
                                    overflow = TextOverflow.Ellipsis,
                                    maxLines = 1,
                                    style = MaterialTheme.typography.displaySmall,
                                    modifier = Modifier.weight(.8f)
                                )
                                Spacer(modifier = Modifier.width(spacing.spaceSmall))
                                IconButton(
                                    onClick = {
                                        showExerciseInfoDialogSuperset.value = true
                                    },
                                    icon = Icons.Outlined.Info,
                                    padding = 0.dp
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(spacing.spaceMedium))
                    ExerciseCard(
                        page = page,
                        timerStatus = state.timerStatus,
                        trackableExercises = state.exerciseInfo,
                        loggerListState = currentLoggerState,
                        workoutTemplate = currentExercise,
                        onRepsChange = { reps, index, id, exerciseName ->
                            viewModel.onEvent(StartWorkoutEvent.OnRepsChange(reps = reps, index = index, rowId = id, page = page, exerciseName = exerciseName))
                        },
                        onWeightChange = { weight, index, id, exerciseName ->
                            viewModel.onEvent(StartWorkoutEvent.OnWeightChange(weight = weight, index = index, rowId = id, page = page, exerciseName = exerciseName))
                        },
                        onCheckboxChange = { isChecked, index, id, page ->
                            if(isChecked && state.currRunningIndex != index && state.timerStatus == TimerStatus.RUNNING){ // non checked clicked while timer already running
                                // DO NOTHING
//                                viewModel.onEvent(StartWorkoutEvent.OnCheckboxChange(isChecked= false, timerStatus = TimerStatus.RUNNING, currRunningIndex = state.currRunningIndex, index = index, rowId = id, page = page))
                            }
                            if(isChecked && (state.timerStatus == TimerStatus.START || state.timerStatus == TimerStatus.FINISHED)){ // non checked clicked while timer not running
                                viewModel.onEvent(StartWorkoutEvent.OnCheckboxChange(isChecked= true, timerStatus = TimerStatus.RUNNING, currRunningIndex = index, index = index, rowId = id, page = page, shouldUpdateTime = true))
                                val wakeupTime = StartWorkoutViewModel.setAlarm(context = context, timeDuration = Duration.ofSeconds((currentExercise.first().rest.getOrElse(index) { currentExercise.first().rest.last() }).toLong()))
                                NotificationUtil.showTimerRunning(context, wakeupTime)
                                viewModel.onEvent(StartWorkoutEvent.ChangeCheckboxColor(color = Color(255,153,51), id = id, index = index))
                            }
                            else if(!isChecked && state.currRunningIndex == index && state.timerStatus == TimerStatus.RUNNING){ // checked clicked while that row has timer running
                                viewModel.onEvent(StartWorkoutEvent.OnCheckboxChange(isChecked= true, timerStatus = TimerStatus.FINISHED, currRunningIndex = -1, index = index, rowId = id, page = page, shouldUpdateTime = true))
                                StartWorkoutViewModel.removeAlarm(context)
                                NotificationUtil.hideTimerNotification(context)
                                viewModel.onEvent(StartWorkoutEvent.ChangeCheckboxColor(color = Color.DarkGray, id = id, index = index))
                            }
                            else if(!isChecked && state.currRunningIndex != index){ // checked clicked while that row does not have timer running
                                viewModel.onEvent(StartWorkoutEvent.OnCheckboxChange(isChecked= false, timerStatus = state.timerStatus, currRunningIndex = state.currRunningIndex, index = index, rowId = id, page = page, shouldUpdateTime = false))
                            }
                        },
                        onRemoveSet = {
                            viewModel.onEvent(StartWorkoutEvent.OnRemoveSet(page))
                        },
                        onAddSet = {
                            viewModel.onEvent(StartWorkoutEvent.OnAddSet(page))
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
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.TopCenter
                ) {
                    Timer(
                        modifier = Modifier
                            .size(200.dp),
                        timerJump = state.timerJump,
                        handleColor = MaterialTheme.colorScheme.primary,
                        inactiveBarColor = MaterialTheme.colorScheme.secondary,
                        activeBarColor = MaterialTheme.colorScheme.inversePrimary
                    )
                    Row(
                        Modifier
                            .padding(horizontal = spacing.spaceMedium)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowLeft,
                            contentDescription = null,
                            tint = if(pagerState.currentPage == 0 || state.timerStatus == TimerStatus.RUNNING) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.onBackground,
                            modifier = if(pagerState.currentPage == 0 || state.timerStatus == TimerStatus.RUNNING) { Modifier.size(spacing.spaceLarge)
                            } else {
                                Modifier
                                    .clickable {
                                        if (pagerState.currentPage > 0) {
                                            coroutineScope.launch {
                                                pagerState.animateScrollToPage(
                                                    pagerState.currentPage - 1,
                                                    0f
                                                )
                                            }
                                        }
                                    }
                                    .size(spacing.spaceLarge)
                            }
                        )
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowRight,
                            contentDescription = null,
                            tint = if(((pagerState.currentPage == count) || (pagerState.currentPage == (pagerState.pageCount - 1))) || state.timerStatus == TimerStatus.RUNNING) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.onBackground,
                            modifier = if(((pagerState.currentPage == count) || (pagerState.currentPage == (pagerState.pageCount - 1))) || state.timerStatus == TimerStatus.RUNNING) { Modifier.size(spacing.spaceLarge)
                            } else {
                                Modifier
                                    .clickable {
                                        if ((pagerState.currentPage + 1) < pagerState.pageCount) {
                                            coroutineScope.launch {
                                                pagerState.animateScrollToPage(
                                                    pagerState.currentPage + 1,
                                                    0f
                                                )
                                            }
                                        }
                                    }
                                    .size(spacing.spaceLarge)
                            }
                        )
                    }
                }
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
