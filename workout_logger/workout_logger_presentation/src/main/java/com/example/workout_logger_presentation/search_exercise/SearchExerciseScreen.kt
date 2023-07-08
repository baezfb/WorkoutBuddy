package com.example.workout_logger_presentation.search_exercise

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import coil.annotation.ExperimentalCoilApi
import com.hbaez.core_ui.LocalSpacing
import com.hbaez.core.R
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextAlign
import com.example.workout_logger_presentation.components.SearchTextField
import com.example.workout_logger_presentation.search_exercise.components.TrackableExerciseItem
import com.hbaez.core.util.UiEvent

@OptIn(ExperimentalComposeUiApi::class)
@ExperimentalCoilApi
@Composable
fun SearchExerciseScreen(
    scaffoldState: ScaffoldState,
    onNavigateUp: () -> Unit,
    rowId: Int,
    viewModel: SearchExerciseViewModel  = hiltViewModel()
){
    val spacing = LocalSpacing.current
    val state = viewModel.state
    val context = LocalContext.current

    val keyboardController = LocalSoftwareKeyboardController.current
    LaunchedEffect(key1 = keyboardController) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message.asString(context)
                    )
                    keyboardController?.hide()
                }
                is UiEvent.NavigateUp -> onNavigateUp()
                else -> Unit
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(spacing.spaceMedium)
    ) {
        Text(
            text = stringResource(id = com.hbaez.core.R.string.search_exercise),
            style = MaterialTheme.typography.h2
        )
        Spacer(modifier = Modifier.height(spacing.spaceMedium))
        Row {
            SearchTextField(
                text = state.query,
                onValueChange = {
                    viewModel.onEvent(SearchExerciseEvent.OnExerciseNameChange(it))
                },
                onSearch = {
                    keyboardController?.hide()
                    viewModel.onEvent(SearchExerciseEvent.OnSearch)
                           },
                onFocusChanged = {
                    viewModel.onEvent(SearchExerciseEvent.OnExerciseNameFocusChange(it.isFocused))
                }
            )
            Box(
                modifier = Modifier
                    .clip(RectangleShape)
                    .align(Alignment.CenterVertically)
                    .clickable {
                        viewModel.onEvent(SearchExerciseEvent.OnSearch)
                    }
            ){
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(spacing.spaceMedium)
        ) {
            items(state.trackableExercise) {
                TrackableExerciseItem(
                    trackableExerciseState = it,
                    onClick = {
                              viewModel.onEvent(SearchExerciseEvent.OnToggleTrackableExercise(it))
                    },
                    onDescrClick = {
                                   viewModel.onEvent(SearchExerciseEvent.OnToggleTrackableExerciseDescription(it))
                    },
                    onTrack = {
                              viewModel.onEvent(SearchExerciseEvent.OnTrackExercise(it, rowId))
                    },
                )
            }
        }
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            when {
                state.isSearching -> CircularProgressIndicator()
                state.trackableExercise.isEmpty() -> {
                    Text(
                        text = stringResource(id = R.string.no_results),
                        style = MaterialTheme.typography.body1,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}