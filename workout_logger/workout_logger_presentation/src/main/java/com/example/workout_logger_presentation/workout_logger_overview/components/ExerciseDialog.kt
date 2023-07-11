package com.example.workout_logger_presentation.workout_logger_overview.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import com.example.workout_logger_presentation.components.SearchTextField
import com.example.workout_logger_presentation.search_exercise.TrackableExerciseState
import com.example.workout_logger_presentation.search_exercise.components.TrackableExerciseItem
import com.hbaez.core.R
import com.hbaez.core_ui.LocalSpacing

@OptIn(ExperimentalComposeUiApi::class)
@ExperimentalCoilApi
@Composable
fun ExerciseDialog(
    filterText: String,
    trackableExercises: List<TrackableExerciseState>,
    onDismiss: () -> Unit,
    onChooseExercise: (trackableExercise: TrackableExerciseState) -> Unit,
    onFilterTextChange: (text: String) -> Unit,
    onItemClick: (trackableExercise: TrackableExerciseState) -> Unit,
    onDescrClick: (trackableExercise: TrackableExerciseState) -> Unit,
    modifier: Modifier = Modifier
) {
    val spacing = LocalSpacing.current
    val keyboardController = LocalSoftwareKeyboardController.current

    AlertDialog(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(max = 475.dp)
            .wrapContentHeight(align = Alignment.CenterVertically)
            .clip(RoundedCornerShape(50.dp)),
        onDismissRequest = { onDismiss() },
        title = null,
        text = null,
        buttons = {
            Scaffold(
                backgroundColor = MaterialTheme.colors.surface,
                modifier = Modifier.fillMaxHeight(),
                topBar = {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column() {
                            Text(
                                stringResource(id = R.string.choose_exercise),
                                textAlign = TextAlign.Center,
                                fontSize = 32.sp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight()
                            )
                            Spacer(modifier = Modifier.height(spacing.spaceLarge))
                            SearchTextField(
                                modifier = Modifier.padding(spacing.spaceMedium),
                                text = filterText,
                                onValueChange = {
                                    onFilterTextChange(it)
                                },
                                onClear = {
                                    onFilterTextChange("")
                                    keyboardController?.hide()
                                },
                                onFocusChanged = {
//                                    viewModel.onEvent(SearchExerciseEvent.OnExerciseNameFocusChange(it.isFocused))
                                }
                            )
                        }
                    }
                },
                content = {
                    LazyColumn(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        contentPadding = PaddingValues(vertical = spacing.spaceMedium),
                        modifier = Modifier.heightIn(250.dp)
                    ){
                        items(trackableExercises.size) {
                            TrackableExerciseItem(
                                trackableExerciseState = trackableExercises[it],
                                onClick = {
                                    onItemClick(trackableExercises[it])
                                          },
                                onDescrClick = {
                                    onDescrClick(trackableExercises[it])
                                               },
                                onTrack = {
                                    onChooseExercise(trackableExercises[it])
                                })
                        }
                    }
                }
            )
        }
    )
}