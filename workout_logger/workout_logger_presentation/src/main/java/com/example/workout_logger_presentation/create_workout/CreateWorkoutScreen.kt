package com.example.workout_logger_presentation.create_workout

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import coil.annotation.ExperimentalCoilApi
import com.example.workout_logger_presentation.components.AddButton
import com.example.workout_logger_presentation.components.NameField
import com.hbaez.core_ui.LocalSpacing
import com.hbaez.core.R
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import com.example.workout_logger_presentation.components.ExerciseInfoDialog
import com.example.workout_logger_presentation.create_workout.components.ExerciseCard
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.hbaez.core.util.UiEvent

@OptIn(ExperimentalComposeUiApi::class, ExperimentalPagerApi::class)
@ExperimentalCoilApi
@Composable
fun CreateWorkoutScreen(
    scaffoldState: ScaffoldState,
    onNavigateUp: () -> Unit,
    onNavigateToSearchExercise: (page: Int) -> Unit,
    viewModel: CreateWorkoutViewModel = hiltViewModel()
) {
    val spacing = LocalSpacing.current
    val state = viewModel.state

    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val pagerState = rememberPagerState()
    val pageCount = remember { mutableStateOf(state.pageCount) }
    val showExerciseInfoDialog = remember { mutableStateOf(false) }

    LaunchedEffect(Unit){
        viewModel.onEvent(CreateWorkoutEvent.CheckTrackedExercise)

        viewModel.uiEvent.collect{ event ->
            when(event) {
                is UiEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message.asString(context)
                    )
                }
                is UiEvent.NavigateUp -> onNavigateUp()
                else -> Unit
            }
        }
    }
    LaunchedEffect(state.pageCount) {
        pageCount.value = state.pageCount
        if (pagerState.currentPage >= state.pageCount) {
            pagerState.scrollToPage(state.pageCount)
        }
    }
    LaunchedEffect(pagerState.isScrollInProgress){
        if(pagerState.targetPage > state.pageCount - 1){
            pagerState.scrollToPage(state.pageCount)
        }
    }
    LaunchedEffect(key1 = pagerState.currentPage, key2 = state.trackableExercises.size){
        if(state.trackableExercises.getOrNull(pagerState.currentPage) != null){
            viewModel.onEvent(CreateWorkoutEvent.GetExerciseInfo(state.trackableExercises[pagerState.currentPage].exercise!!.name!!))
        }
    }

    if(showExerciseInfoDialog.value){
        ExerciseInfoDialog(
            trackableExerciseState = state.exerciseInfo.first(),
            onDescrClick = {
                           viewModel.onEvent(CreateWorkoutEvent.OnToggleExerciseDescription(state.exerciseInfo.first()))
                           },
            onDismiss = { showExerciseInfoDialog.value = false }
        )
    }

    Scaffold(
        topBar = {
            Column {
                Spacer(modifier = Modifier.height(spacing.spaceMedium))
                NameField(
                    text = state.workoutName,
                    hint = stringResource(id = R.string.workout_name),
                    onValueChange = {
                        viewModel.onEvent(CreateWorkoutEvent.OnWorkoutNameChange(it))
                    },
                    onFocusChanged = {
                        viewModel.onEvent(CreateWorkoutEvent.OnWorkoutNameFocusChange(it.isFocused))
                    },
                    keyboardController = keyboardController
                )
                Spacer(modifier = Modifier.height(spacing.spaceSmall))
            }
        },
        content = { padding ->
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                verticalAlignment = Alignment.CenterVertically,
                count = 10,
                contentPadding = PaddingValues(spacing.spaceSmall)
            ) {page ->
                ExerciseCard(
                    page = page,
                    onAddCard = {
                        onNavigateToSearchExercise(page)
                    },
                    onAddSet = {
                        viewModel.onEvent(CreateWorkoutEvent.AddSet(page))
                    },
                    trackableExercises = state.trackableExercises.getOrNull(page),
                    onShowInfo = {
                        showExerciseInfoDialog.value = true
                    },
                    onDeleteRow = { id, exerciseId ->
                        viewModel.onEvent(CreateWorkoutEvent.OnRemoveSetRow(id, exerciseId))
                    },
                    onDeletePage = {
                        viewModel.onEvent(CreateWorkoutEvent.OnRemovePage(page))
                    },
                    onRepsChange = { text, index ->
                        viewModel.onEvent(CreateWorkoutEvent.OnTrackableExerciseUiRepsChange(reps = text, trackableExerciseUiStateId = state.trackableExercises[page].id, index = index))
                    },
                    onRestChange = { text, index ->
                        viewModel.onEvent(CreateWorkoutEvent.OnTrackableExerciseUiRestChange(rest = text, trackableExerciseUiStateId = state.trackableExercises[page].id, index = index))
                    },
                    onWeightChange = { text, index ->
                        viewModel.onEvent(CreateWorkoutEvent.OnTrackableExerciseUiWeightChange(weight = text, trackableExerciseUiStateId = state.trackableExercises[page].id, index = index))
                    }
                )
            }
        },
        bottomBar = {
            Row(
                Modifier.padding(spacing.spaceSmall)
            ){
                AddButton(
                    text = stringResource(id = R.string.submit),
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(start = spacing.spaceExtraExtraLarge, end = spacing.spaceSmall),
                    onClick = {
                        viewModel.onEvent(CreateWorkoutEvent.OnCreateWorkout(state.trackableExercises.toList(), state.workoutName, state.lastUsedId))
                    },
                    icon = Icons.Default.Done
                )
                Spacer(modifier = Modifier.width(spacing.spaceLarge))
            }
        }
    )
}