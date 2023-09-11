package com.example.workout_logger_presentation.create_workout.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import com.example.workout_logger_presentation.components.AddButton
import com.example.workout_logger_presentation.components.IconButton
import com.example.workout_logger_presentation.create_workout.CreateWorkoutTableRow
import com.example.workout_logger_presentation.create_workout.EditTableCell
import com.example.workout_logger_presentation.create_workout.TrackableExerciseUiState
import com.example.workout_logger_presentation.start_workout.TimerStatus
import com.hbaez.core.R
import com.hbaez.core_ui.LocalSpacing
import kotlinx.coroutines.launch

@ExperimentalCoilApi
@Composable
fun ExerciseCard(
    onAddCard: () -> Unit,
    onAddSet: () -> Unit,
    onMakeSuperset: () -> Unit,
    page: Int,
    trackableExercises: List<TrackableExerciseUiState?>,
    onShowInfo: () -> Unit,
    onShowInfoSuperset: () -> Unit,
    onDeleteRow: (id: Int) -> Unit,
    onDeletePage: () -> Unit,
    onRepsChange: (text: String, index: Int, exerciseName: String)  -> Unit,
    onRestChange: (text: String, index: Int, exerciseName: String)  -> Unit,
    onWeightChange: (text: String, index: Int, exerciseName: String)  -> Unit
){
    val spacing = LocalSpacing.current
    var isLongPressed by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    // Handle clicks outside the Card
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(spacing.spaceSmall)
            .pointerInput(Unit) {
                detectTapGestures {
                    isLongPressed = false // Reset isLongPressed to false when clicked outside Card
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Column {
            if(trackableExercises.size == 1 && trackableExercises[0] != null){
                Row {
                    Text(
                        text = trackableExercises[0]!!.name.uppercase(),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        style = MaterialTheme.typography.displaySmall,
                        modifier = Modifier
                            .padding(spacing.spaceExtraSmall)
                            .weight(.8f)
                    )
                    Spacer(modifier = Modifier.width(spacing.spaceSmall))
                    IconButton(
                        onClick = { onShowInfo() },
                        icon = Icons.Outlined.Info,
                        padding = 0.dp
                    )
                }
                Spacer(modifier = Modifier.height(spacing.spaceSmall))
            }
            else if (trackableExercises.size > 1){
                Row {
                    Text(
                        text = trackableExercises[0]!!.name.uppercase(),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        style = MaterialTheme.typography.displaySmall,
                        modifier = Modifier
                            .padding(spacing.spaceExtraSmall)
                            .weight(.8f)
                    )
                    Spacer(modifier = Modifier.width(spacing.spaceSmall))
                    IconButton(
                        onClick = { onShowInfo() },
                        icon = Icons.Outlined.Info,
                        padding = 0.dp
                    )
                    Spacer(modifier = Modifier.width(spacing.spaceMedium))
                    Text(
                        text = trackableExercises[1]!!.name.uppercase(),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        style = MaterialTheme.typography.displaySmall,
                        modifier = Modifier
                            .padding(spacing.spaceExtraSmall)
                            .weight(.8f)
                    )
                    Spacer(modifier = Modifier.width(spacing.spaceSmall))
                    IconButton(
                        onClick = { onShowInfoSuperset() },
                        icon = Icons.Outlined.Info,
                        padding = 0.dp
                    )
                }
                Spacer(modifier = Modifier.height(spacing.spaceSmall))
            }
            Card(
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if(isLongPressed && trackableExercises.isNotEmpty()) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.background
                ),
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(50.dp)
                    )
                    .border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(50.dp))
                    .width(325.dp)
                    .height(375.dp)
                    .padding(if (isLongPressed) 0.dp else spacing.spaceMedium)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onLongPress = {
                                coroutineScope.launch {
                                    isLongPressed = true
                                }
                            }
                        )
                    }
            ) {

                if(trackableExercises.isEmpty()){
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        AddButton(
                            text = stringResource(id = R.string.add_exercise),
                            onClick = {
                                onAddCard()
                            },
                            icon = Icons.Default.Search,
                            color = MaterialTheme.colorScheme.primary,
                            borderColor = MaterialTheme.colorScheme.background
                        )
                    }
                }
                else if (!isLongPressed) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        LazyColumn(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Top
                        ) {
                            items(count = trackableExercises[0]!!.sets) {
                                Spacer(modifier = Modifier.height(spacing.spaceSmall))
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(MaterialTheme.colorScheme.primaryContainer)
                                        .padding(horizontal=spacing.spaceMedium),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = (it + 1).toString(),
                                        style = MaterialTheme.typography.displaySmall,
                                        modifier = Modifier.padding(spacing.spaceSmall).weight(.15f)
                                    )
                                    Column(Modifier.weight(0.65f)) {
                                        CreateWorkoutTableRow(
                                            onRepsChange = { text -> onRepsChange(text, it, trackableExercises[0]!!.name) },
                                            onWeightChange = { text -> onWeightChange(text, it, trackableExercises[0]!!.name) },
                                            reps = trackableExercises[0]!!.reps[it],
                                            weight = trackableExercises[0]!!.weight[it],
                                            hasExercise = true
                                        )
//                                        DraggableRow(
//                                            reps = trackableExercises[0]!!.reps[it],
//                                            rest = trackableExercises[0]!!.rest[it],
//                                            weight = trackableExercises[0]!!.weight[it],
//                                            hasExercise = true,
//                                            id = it,
//                                            cardOffset = 600f,
//                                            onRepsChange = { text ->
//                                                onRepsChange(text, it, trackableExercises[0]!!.name)
//                                            },
//                                            onRestChange = { text ->
//                                                onRestChange(text, it, trackableExercises[0]!!.name)
//                                            },
//                                            onWeightChange = { text ->
//                                                onWeightChange(text, it, trackableExercises[0]!!.name)
//                                            },
//                                            onDeleteRow = { id ->
//                                                onDeleteRow(id)
//                                            }
//                                        )
                                        if(trackableExercises.size > 1){
                                            Spacer(modifier = Modifier.height(spacing.spaceSmall))
                                            CreateWorkoutTableRow(
                                                onRepsChange = { text -> onRepsChange(text, it, trackableExercises[1]!!.name) },
                                                onWeightChange = { text -> onWeightChange(text, it, trackableExercises[1]!!.name) },
                                                reps = trackableExercises[1]!!.reps[it],
                                                weight = trackableExercises[1]!!.weight[it],
                                                hasExercise = true
                                            )
//                                            DraggableRow(
//                                                reps = trackableExercises[1]!!.reps[it],
//                                                rest = trackableExercises[1]!!.rest[it],
//                                                weight = trackableExercises[1]!!.weight[it],
//                                                hasExercise = true,
//                                                id = it,
//                                                cardOffset = 600f,
//                                                onRepsChange = { text ->
//                                                    onRepsChange(text, it, trackableExercises[1]!!.name)
//                                                },
//                                                onRestChange = { text ->
//                                                    onRestChange(text, it, trackableExercises[1]!!.name)
//                                                },
//                                                onWeightChange = { text ->
//                                                    onWeightChange(text, it, trackableExercises[1]!!.name)
//                                                },
//                                                onDeleteRow = { id ->
//                                                    onDeleteRow(id)
//                                                }
//                                            )
                                        }
                                    }
                                    EditTableCell(
                                        label = stringResource(id = R.string.rest),
                                        text = trackableExercises[0]!!.rest[it],
                                        weight = .25f,
                                        keyboardType = KeyboardType.Number,
                                        onValueChange = { textChange ->
                                            val filteredValue = textChange.filter { tmp -> !(tmp == '.' || tmp == '-') } // Filter out non-digit characters
                                            onRestChange(filteredValue, it, trackableExercises[0]!!.name)
                                        },
                                        borderColor = MaterialTheme.colorScheme.primaryContainer,
                                        backgroundColor = Color.Transparent
                                    )
                                }
                                Spacer(modifier = Modifier.height(spacing.spaceSmall))
                            }
                            item {
                                Spacer(modifier = Modifier.height(spacing.spaceMedium))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ){
                                    Row(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(100f))
                                            .clickable(enabled = trackableExercises[0]!!.sets > 0) {
                                                onDeleteRow(trackableExercises[0]!!.sets - 1)
                                            }
                                            .border(
                                                width = 1.dp,
                                                color = MaterialTheme.colorScheme.background,
                                                shape = RoundedCornerShape(100f)
                                            )
                                            .padding(spacing.spaceMedium),
                                        horizontalArrangement = Arrangement.Center,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Del Set",
                                            maxLines = 2,
                                            style = MaterialTheme.typography.labelLarge,
                                            color = if(trackableExercises[0]!!.sets > 0) MaterialTheme.colorScheme.primary else Color.DarkGray
                                        )
                                    }
                                    Row(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(100f))
                                            .clickable { onAddSet() }
                                            .border(
                                                width = 1.dp,
                                                color = MaterialTheme.colorScheme.background,
                                                shape = RoundedCornerShape(100f)
                                            )
                                            .padding(spacing.spaceMedium),
                                        horizontalArrangement = Arrangement.Center,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Add Set",
                                            maxLines = 2,
                                            style = MaterialTheme.typography.labelLarge,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }
                            }
                            if(trackableExercises.size < 2){
                                item {
                                    Spacer(modifier = Modifier.height(spacing.spaceLarge))
                                    AddButton(
                                        text = stringResource(id = R.string.make_superset),
                                        onClick = {
                                            onMakeSuperset()
                                        },
                                        color = MaterialTheme.colorScheme.tertiary,
                                        borderColor = MaterialTheme.colorScheme.background
                                    )
                                }
                            }
                        }
                    }
                }
                else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable {
                                onDeletePage()
                                isLongPressed = false
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = stringResource(id = R.string.delete).uppercase(),
                                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint = MaterialTheme.colorScheme.onErrorContainer
                            )
                        }
                    }
                }
            }
        }
    }
}