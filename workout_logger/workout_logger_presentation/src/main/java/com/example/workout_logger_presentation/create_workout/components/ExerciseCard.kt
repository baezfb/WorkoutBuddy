package com.example.workout_logger_presentation.create_workout.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import com.example.workout_logger_presentation.components.AddButton
import com.example.workout_logger_presentation.create_workout.TrackableExerciseUiState
import com.hbaez.core.R
import com.hbaez.core_ui.LocalSpacing

@ExperimentalCoilApi
@Composable
fun ExerciseCard(
    addCard: Boolean = false,
    onAddCard: () -> Unit,
    onAddSet: () -> Unit,
    page: Int,
    trackableExercises: TrackableExerciseUiState?,
    onDeleteRow: (id: Int, exerciseId: Int) -> Unit,
    onRepsChange: (text: String, index: Int)  -> Unit,
    onRestChange: (text: String, index: Int)  -> Unit,
    onWeightChange: (text: String, index: Int)  -> Unit
){
    val spacing = LocalSpacing.current
    Card(
        shape = RoundedCornerShape(8.dp),
        backgroundColor = MaterialTheme.colors.background,
        modifier = Modifier
            .clip(
                RoundedCornerShape(50.dp)
            )
            .border(2.dp, MaterialTheme.colors.primary, RoundedCornerShape(50.dp))
            .padding(spacing.spaceMedium)
            .width(275.dp)
            .height(325.dp)
    ) {

        if(trackableExercises == null){
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                item {
                    AddButton(
                        text = stringResource(id = R.string.add_exercise),
                        onClick = {
                            onAddCard()
                        },
                        icon = Icons.Default.Search,
                        color = MaterialTheme.colors.primary,
                        borderColor = MaterialTheme.colors.background
                    )
                }
            }
        }
        else {
            Column(modifier = Modifier.fillMaxSize()) {
                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    items(count = trackableExercises.sets) {
                        Spacer(modifier = Modifier.height(spacing.spaceSmall))
                        DraggableRow(
                            sets = it.toString(),
                            reps = trackableExercises.reps[it],
                            rest = trackableExercises.rest[it],
                            weight = trackableExercises.weight[it],
                            hasExercise = true,
                            id = it,
                            cardOffset = 600f,
                            onRepsChange = { text ->
                                           onRepsChange(text, it)
                            },
                            onRestChange = { text ->
                                onRestChange(text, it)
                            },
                            onWeightChange = { text ->
                                onWeightChange(text, it)
                            },
                            onDeleteRow = { id ->
                                onDeleteRow(id, page)
                            }) {

                        }
                    }
                    item{
                        AddButton(
                            text = stringResource(id = R.string.add_set),
                            onClick = {
                                onAddSet()
                            },
                            color = MaterialTheme.colors.primary,
                            borderColor = MaterialTheme.colors.background
                        )
                    }
                }
            }
        }
    }
}