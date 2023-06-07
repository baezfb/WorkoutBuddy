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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import coil.annotation.ExperimentalCoilApi
import com.example.workout_logger_presentation.components.AddButton
import com.example.workout_logger_presentation.components.NameField
import com.example.workout_logger_presentation.create_workout.components.DraggableRow
import com.hbaez.core_ui.LocalSpacing
import com.hbaez.core.R
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import com.example.workout_logger_presentation.create_workout.components.ExerciseCard
import com.example.workout_logger_presentation.start_workout.TimerStatus
import com.example.workout_logger_presentation.start_workout.scrollEnabled
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.hbaez.core.util.UiEvent
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class, ExperimentalPagerApi::class)
@ExperimentalCoilApi
@Composable
fun CreateWorkoutScreen(
    scaffoldState: ScaffoldState,
    onNavigateUp: () -> Unit,
    onNavigateToSearchExercise: (rowId: Int) -> Unit,
    viewModel: CreateWorkoutViewModel = hiltViewModel()
) {
    val spacing = LocalSpacing.current
    val state = viewModel.state

    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val pagerState = rememberPagerState()
    val pageCount = remember { mutableStateOf(state.pageCount) }
    val coroutineScope = rememberCoroutineScope()

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
                CreateWorkoutTableHeader()
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
//                         state = state,
                    addCard = page >= state.pageCount,
                    onAddCard = {
                        viewModel.onEvent(CreateWorkoutEvent.AddPageCount)
                    }
                )
            }
//            LazyColumn(
//                modifier = Modifier
//                    .padding(padding)
//                    .fillMaxWidth()
//                    .wrapContentHeight()
////                    .padding(spacing.spaceMedium)
//            ) {
//                items(state.trackableExercises) {
//                    if (!it.isDeleted) {
//                        DraggableRow(
//                            name = it.name,
//                            sets = it.sets,
//                            reps = it.reps,
//                            rest = it.rest,
//                            weight = it.weight,
//                            isRevealed = it.isRevealed,
//                            isSearchRevealed = it.isSearchRevealed,
//                            hasExercise = (it.exercise != null),
//                            id = it.id,
//                            cardOffset = 400f,
//                            onExpand = { id ->
//                                viewModel.onEvent(CreateWorkoutEvent.OnDraggableRowExpand(id))
//                            },
//                            onCollapse = { id ->
//                                viewModel.onEvent(CreateWorkoutEvent.OnDraggableRowCollapse(id))
//                            },
//                            onCenter = {id ->
//                                viewModel.onEvent(CreateWorkoutEvent.OnDraggableRowCenter(id))
//                            },
//                            onNameChange = { newText ->
//                                viewModel.onEvent(
//                                    CreateWorkoutEvent.OnTrackableExerciseUiNameChange(
//                                        newText,
//                                        it
//                                    )
//                                )
//                            },
//                            onSetsChange = { newText ->
//                                viewModel.onEvent(
//                                    CreateWorkoutEvent.OnTrackableExerciseUiSetsChange(
//                                        newText,
//                                        it
//                                    )
//                                )
//                            },
//                            onRepsChange = { newText ->
//                                viewModel.onEvent(
//                                    CreateWorkoutEvent.OnTrackableExerciseUiRepsChange(
//                                        newText,
//                                        it
//                                    )
//                                )
//                            },
//                            onRestChange = { newText ->
//                                viewModel.onEvent(
//                                    CreateWorkoutEvent.OnTrackableExerciseUiRestChange(
//                                        newText,
//                                        it
//                                    )
//                                )
//                            },
//                            onWeightChange = { newText ->
//                                viewModel.onEvent(
//                                    CreateWorkoutEvent.OnTrackableExerciseUiWeightChange(
//                                        newText,
//                                        it
//                                    )
//                                )
//                            },
//                            onDeleteClick = {
//                                viewModel.onEvent(CreateWorkoutEvent.OnRemoveTableRow(it.id))
//                            },
//                            onSearchClick = {
//                                onNavigateToSearchExercise(it.id)
//                            }
//                        )
//                        Spacer(modifier = Modifier.height(spacing.spaceExtraSmall))
//                    }
//                }
//                item {
//                    AddButton(
//                        text = stringResource(id = R.string.add_exercise),
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .wrapContentHeight(),
//                        onClick = {
//                            viewModel.onEvent(CreateWorkoutEvent.OnAddExercise)
//                        },
//                        icon = Icons.Default.Add
//                    )
//                }
//            }
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