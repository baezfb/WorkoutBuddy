package com.hbaez.workoutbuddy.workout.start_workout

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.rotary.onRotaryScrollEvent
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.wear.compose.material.AutoCenteringParams
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.ScalingLazyColumn
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.material.Vignette
import androidx.wear.compose.material.VignettePosition
import androidx.wear.compose.material.rememberScalingLazyListState
import androidx.wear.compose.material.scrollAway
import coil.annotation.ExperimentalCoilApi
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.VerticalPager
import com.google.accompanist.pager.rememberPagerState
import com.hbaez.core_ui.LocalSpacing
import com.hbaez.user_auth_presentation.model.WorkoutTemplate
import com.hbaez.workoutbuddy.R
import com.hbaez.workoutbuddy.components.WearButton
import com.hbaez.workoutbuddy.components.WearText
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class, ExperimentalLifecycleComposeApi::class,
    ExperimentalPagerApi::class
)
@ExperimentalCoilApi
@Composable
fun StartWorkoutScreen(
    workoutName: String,
    dayOfMonth: Int,
    month: Int,
    year: Int,
    onNavigateToTimer: (seconds: Int, exerciseName: String, currentSet: Int, totalSet: Int) -> Unit,
    viewModel: StartWorkoutViewModel = hiltViewModel()
) {
    val spacing = LocalSpacing.current
    val state = viewModel.state
    val workoutTemplates = viewModel.workoutTemplates.collectAsStateWithLifecycle(emptyList())
    val workoutIds = viewModel.workoutIds
    val pagerState = rememberPagerState(initialPage = 0)

    val listState = rememberScalingLazyListState()
    val focusRequester = remember { FocusRequester() }
    val coroutineScope = rememberCoroutineScope()

    val workoutExerciseNames = ArrayList<String>()
    var counter = 0
    workoutTemplates.value.forEach {
        if(it.name == workoutName){
            counter += 1
            workoutExerciseNames.add(it.exerciseName)
        }
    }

    Scaffold(
        timeText = {
            TimeText(modifier = Modifier.scrollAway(listState))
        },
        vignette = {
            // Only show Vignettes on scrollable screens
            Vignette(vignettePosition = VignettePosition.TopAndBottom)
        },
        positionIndicator = {
            PositionIndicator(
                scalingLazyListState = listState
            )
        },
        modifier = Modifier
            .background(color = MaterialTheme.colors.background),
    ) {

        val contentModifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
        val iconModifier = Modifier
            .size(24.dp)
            .wrapContentSize(align = Alignment.Center)

        var loggerListState: LoggerListState
        Log.println(Log.DEBUG, "startworkoutscreen size", workoutTemplates.value.size.toString())
        VerticalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .padding(spacing.spaceSmall)
                .onRotaryScrollEvent {
                    coroutineScope.launch {
                        Log.println(
                            Log.DEBUG,
                            "verticalScrollPixels",
                            it.verticalScrollPixels.toString()
                        )
                        pagerState.scrollBy(it.verticalScrollPixels)
                    }
                    true
                }
                .focusRequester(focusRequester)
                .focusable(),
            count = counter
        ) {page ->
            workoutTemplates.value.forEach {
                if(it.name == workoutName){
                    if (it.rowId == workoutIds[page].toInt()){
                        if(state.loggerListStates.size > page && state.loggerListStates[page].id != it.rowId){
                            loggerListState = LoggerListState(
                                id = it.rowId,
                                exerciseName = it.exerciseName,
                                exerciseId = it.exerciseId,
                                timerStatus = TimerStatus.START,
                                sets = it.sets.toString(),
                                rest = it.rest,
                                reps = it.reps,
                                weight = it.weight,
                                isCompleted = List(it.sets) { false },
                                checkedColor = List(it.sets) { Color.DarkGray },
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
                                rest = it.rest,
                                reps = it.reps,
                                weight = it.weight,
                                isCompleted = List(it.sets) { false },
                                checkedColor = List(it.sets) { Color.DarkGray }
                            )
                            viewModel.onEvent(StartWorkoutEvent.AddLoggerList(loggerListState))
                        }
                        return@forEach
                    }
                }
            }

            // get current exercise from workoutTemplates
            // using workoutName and exerciseName
            lateinit var currentExercise: WorkoutTemplate
            workoutTemplates.value.forEach {
                if(it.name == workoutName && it.exerciseName ==  workoutExerciseNames[page]){
                    currentExercise = it
                }
            }
            LaunchedEffect(Unit) { focusRequester.requestFocus() }
            if(state.loggerListStates[page].currentSet >= state.loggerListStates[page].reps.size){
                Card(
                    shape = RoundedCornerShape(8.dp),
                    backgroundColor = androidx.compose.material.MaterialTheme.colors.background,
                    modifier = Modifier
                        .clip(
                            RoundedCornerShape(50.dp)
                        )
                        .border(
                            2.dp,
                            androidx.compose.material.MaterialTheme.colors.primary,
                            RoundedCornerShape(50.dp)
                        )
                        .padding(spacing.spaceSmall)
                        .fillMaxWidth(.9f)
                        .height(155.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        WearText(
                            modifier = Modifier.padding(horizontal = spacing.spaceSmall),
                            color = androidx.compose.material.MaterialTheme.colors.onBackground,
                            text = state.loggerListStates[page].exerciseName,
                            style = androidx.compose.material.MaterialTheme.typography.body2,
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(spacing.spaceSmall))
                        WearText(
                            modifier = Modifier.padding(horizontal = spacing.spaceSmall),
                            color = androidx.compose.material.MaterialTheme.colors.onBackground,
                            text = stringResource(id = com.hbaez.core.R.string.redo),
                            style = androidx.compose.material.MaterialTheme.typography.body2
                        )
                    }
                }
            }
            else {
                SetCard(
                    exerciseName = state.loggerListStates[page].exerciseName,
                    currSet = state.loggerListStates[page].currentSet,
                    totalSets = state.loggerListStates[page].reps.size,
                    currReps = state.loggerListStates[page].reps[state.loggerListStates[page].currentSet],
                    currWeight = state.loggerListStates[page].weight[state.loggerListStates[page].currentSet],
                    onRepIncrease = {
                        viewModel.onEvent(StartWorkoutEvent.OnRepIncrease(page, state.loggerListStates[page].currentSet))
                    },
                    onRepDecrease = {
                        viewModel.onEvent(StartWorkoutEvent.OnRepDecrease(page, state.loggerListStates[page].currentSet))
                    },
                    onWeightIncrease = {
                        viewModel.onEvent(StartWorkoutEvent.OnWeightIncrease(page, state.loggerListStates[page].currentSet))
                    },
                    onWeightDecrease = {
                        viewModel.onEvent(StartWorkoutEvent.OnWeightDecrease(page, state.loggerListStates[page].currentSet))
                    },
                    onRest = {
                        viewModel.onEvent(StartWorkoutEvent.OnSetIncrease(page))
                        onNavigateToTimer(
                            currentExercise.rest[state.loggerListStates[page].currentSet].toInt(),
                            currentExercise.exerciseName,
                            state.loggerListStates[page].currentSet + 1,
                            state.loggerListStates[page].reps.size
                        )
                    }
                )
            }
        }
    }
}