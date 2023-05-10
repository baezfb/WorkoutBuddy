package com.hbaez.workoutbuddy.workout

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
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.input.rotary.onRotaryScrollEvent
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.wear.compose.material.AutoCenteringParams
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.ScalingLazyColumn
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.material.Vignette
import androidx.wear.compose.material.VignettePosition
import androidx.wear.compose.material.rememberScalingLazyListState
import androidx.wear.compose.material.scrollAway
import coil.annotation.ExperimentalCoilApi
import com.hbaez.core_ui.LocalSpacing
import com.hbaez.workoutbuddy.R
import com.hbaez.workoutbuddy.components.WearButton
import com.hbaez.workoutbuddy.components.WearText
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class, ExperimentalLifecycleComposeApi::class)
@ExperimentalCoilApi
@Composable
fun WorkoutOverviewScreen(
    onNavigateToWorkout: (workoutName: String, day: Int, month: Int, year: Int, workoutIds: String) -> Unit,
    viewModel: WorkoutOverviewModel = hiltViewModel()
) {
    val spacing = LocalSpacing.current
    val state = viewModel.state
    val workoutTemplates = viewModel.workoutTemplates.collectAsStateWithLifecycle(emptyList())

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
            .background(color = MaterialTheme.colors.background)
            .onRotaryScrollEvent {
                coroutineScope.launch {
                    Log.println(
                        Log.DEBUG,
                        "verticalScrollPixels",
                        it.verticalScrollPixels.toString()
                    )
                    listState.scrollBy(it.verticalScrollPixels)
                }
                true
            }
            .focusRequester(focusRequester)
            .focusable()
    ) {
        LaunchedEffect(Unit) { focusRequester.requestFocus() }

        val contentModifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
        val iconModifier = Modifier
            .size(24.dp)
            .wrapContentSize(align = Alignment.Center)

        ScalingLazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            autoCentering = AutoCenteringParams(itemIndex = 0),
            state = listState
        ) {

            item { WearText(color = MaterialTheme.colors.onBackground, text = stringResource(R.string.choose_workout)) }
            item { Spacer(modifier = Modifier.height(spacing.spaceMedium)) }
            val uniqueNames = mutableListOf<String>()
            workoutTemplates.value.forEach { workout ->
                if(!uniqueNames.contains(workout.name)) {
                    item {
                        WearButton(
                            text = workout.name,
                            onClick = { /*TODO*/ },
                            icon = null
                        )
                    }
                    uniqueNames.add(workout.name)
                }
            }
        }
    }

}