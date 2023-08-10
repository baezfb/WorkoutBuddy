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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import com.example.workout_logger_presentation.components.AddButton
import com.example.workout_logger_presentation.components.IconButton
import com.example.workout_logger_presentation.create_workout.TrackableExerciseUiState
import com.hbaez.core.R
import com.hbaez.core_ui.LocalSpacing
import kotlinx.coroutines.launch

@ExperimentalCoilApi
@Composable
fun ExerciseCard(
    onAddCard: () -> Unit,
    onAddSet: () -> Unit,
    page: Int,
    trackableExercises: TrackableExerciseUiState?,
    onShowInfo: () -> Unit,
    onDeleteRow: (id: Int) -> Unit,
    onDeletePage: () -> Unit,
    onRepsChange: (text: String, index: Int)  -> Unit,
    onRestChange: (text: String, index: Int)  -> Unit,
    onWeightChange: (text: String, index: Int)  -> Unit
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
            if(trackableExercises != null){
                Row {
                    Text(
                        text = trackableExercises.name.uppercase(),
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
            Card(
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if(isLongPressed && trackableExercises != null) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.background
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

                if(trackableExercises == null){
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
                            items(count = trackableExercises.sets) {
                                Spacer(modifier = Modifier.height(spacing.spaceMedium))
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
                                        onDeleteRow(id)
                                    }
                                )
                            }
                            item{
                                AddButton(
                                    text = stringResource(id = R.string.add_set),
                                    onClick = {
                                        onAddSet()
                                    },
                                    color = MaterialTheme.colorScheme.primary,
                                    borderColor = MaterialTheme.colorScheme.background
                                )
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
                                color = MaterialTheme.colorScheme.onSecondary
                            )
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint = MaterialTheme.colorScheme.onSecondary
                            )
                        }
                    }
                }
            }
        }
    }
}