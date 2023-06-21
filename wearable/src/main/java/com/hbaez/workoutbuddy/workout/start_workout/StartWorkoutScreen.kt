package com.hbaez.workoutbuddy.workout.start_workout

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.rotary.onRotaryScrollEvent
import androidx.compose.ui.res.stringResource
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
    viewModel: StartWorkoutViewModel = hiltViewModel()
) {
    val spacing = LocalSpacing.current
    val state = viewModel.state
    val workoutTemplates = viewModel.workoutTemplates.collectAsStateWithLifecycle(emptyList())
    val pagerState = rememberPagerState(initialPage = 0)

    val listState = rememberScalingLazyListState()
    val focusRequester = remember { FocusRequester() }
    val coroutineScope = rememberCoroutineScope()

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
        workoutTemplates.value.forEach {
            Log.println(Log.DEBUG, "startworkoutscreen check", it.name)
            Log.println(Log.DEBUG, "startworkoutscreen check", workoutName)
            if(it.name == workoutName){
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
        }
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
            count = state.loggerListStates.size
        ) {
            LaunchedEffect(Unit) { focusRequester.requestFocus() }
            SetCard(exerciseName = state.loggerListStates[it].exerciseName)
        }
//        Spacer(modifier = Modifier.height(spacing.spaceMedium))
//        ScalingLazyColumn(
//            modifier = Modifier
//                .fillMaxSize(),
//            autoCentering = AutoCenteringParams(itemIndex = 0),
//            state = listState
//        ) {
//            state.loggerListStates.forEach {
//                item { SetCard(exerciseName = it.exerciseName) }
//                item { Spacer(modifier = Modifier.height(spacing.spaceMedium)) }
//            }
//        }
    }
}