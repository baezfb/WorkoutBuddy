package com.example.workout_logger_presentation.workout_logger_overview.components

import android.annotation.SuppressLint
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
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
        content = {
            Scaffold(
                containerColor = MaterialTheme.colorScheme.surface,
                modifier = Modifier.fillMaxHeight(),
                content = {
                    Column {
                        Spacer(modifier = Modifier.height(spacing.spaceMedium))
                        Text(
                            stringResource(id = R.string.choose_exercise),
                            textAlign = TextAlign.Center,
                            fontSize = 32.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                        )
                        Spacer(modifier = Modifier.height(spacing.spaceMedium))
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
                }
            )
        }
    )
}