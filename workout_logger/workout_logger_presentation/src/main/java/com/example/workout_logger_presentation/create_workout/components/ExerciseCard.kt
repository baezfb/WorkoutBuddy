package com.example.workout_logger_presentation.create_workout.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import com.example.workout_logger_presentation.components.AddButton
import com.example.workout_logger_presentation.create_workout.TrackableExerciseUiState
import com.example.workout_logger_presentation.start_workout.LoggerListState
import com.example.workout_logger_presentation.start_workout.TrackableInProgressExerciseUi
import com.example.workout_logger_presentation.start_workout.components.ExerciseCardRow
import com.hbaez.core.R
import com.hbaez.core_ui.LocalSpacing
import com.hbaez.user_auth_presentation.model.WorkoutTemplate

@ExperimentalCoilApi
@Composable
fun ExerciseCard(
    addCard: Boolean = false,
    onAddCard: () -> Unit,
    onAddSet: () -> Unit,
    page: Int,
    trackableExercises: TrackableExerciseUiState?
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
                            isRevealed = false,
                            isSearchRevealed = false,
                            hasExercise = true,
                            id = 1,
                            cardOffset = 400f,
                            onExpand = {},
                            onCollapse = {},
                            onCenter = {},
                            onRepsChange = {},
                            onRestChange = {},
                            onWeightChange = {},
                            onDeleteClick = { /*TODO*/ }) {

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
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth(),
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.SpaceBetween
//            ) {
//                Text(
//                    text= stringResource(id = R.string.sets),
//                    style = MaterialTheme.typography.body2
//                )
//                Text(
//                    text= stringResource(id = R.string.weight),
//                    style = MaterialTheme.typography.body2
//                )
//                Text(
//                    text= stringResource(id = R.string.reps),
//                    style = MaterialTheme.typography.body2
//                )
//                Text(
//                    text= stringResource(id = R.string.completed_question),
//                    style = MaterialTheme.typography.body2
//                )
//            }
//            LazyColumn{
//                List(loggerListState.sets.toInt()) { it + 1 }.forEach {
//                    item {
//                        ExerciseCardRow(
//                            it,
//                            loggerListState,
//                            workoutTemplate,
//                            it - 1,
//                            onRepsChange,
//                            onWeightChange,
//                            onCheckboxChange,
//                            page
//                        )
//                    }
//                }
//            }
        }
    }
}