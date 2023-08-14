package com.example.workout_logger_presentation.workout_logger_overview

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.annotation.ExperimentalCoilApi
import com.example.workout_logger_presentation.components.DaySelector
import com.example.workout_logger_presentation.components.IconButton
import com.example.workout_logger_presentation.components.OptionsHeader
import com.example.workout_logger_presentation.components.parseDateText
import com.example.workout_logger_presentation.workout_logger_overview.components.CompletedWorkoutItem
import com.example.workout_logger_presentation.workout_logger_overview.components.ExerciseDialog
import com.example.workout_logger_presentation.workout_logger_overview.components.ExerciseRow
import com.example.workout_logger_presentation.workout_logger_overview.components.OptionsHeaderDialog
import com.example.workout_logger_presentation.workout_logger_overview.components.WorkoutDialog
import com.hbaez.core_ui.LocalSpacing
import com.hbaez.core.R
import com.hbaez.user_auth_presentation.components.FlatButton
import com.himanshoe.kalendar.Kalendar
import com.himanshoe.kalendar.KalendarEvent
import com.himanshoe.kalendar.KalendarEvents
import com.himanshoe.kalendar.KalendarType
import com.himanshoe.kalendar.color.KalendarColor
import com.himanshoe.kalendar.color.KalendarColors
import com.himanshoe.kalendar.ui.component.day.KalendarDayKonfig
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@ExperimentalCoilApi
@Composable
fun WorkoutLoggerOverviewScreen(
    onNavigateToCreateWorkout: () -> Unit,
    onNavigateToEditWorkout: (workoutName: String, workoutIds: String) -> Unit,
    onNavigateToStartWorkout: (workoutName: String, day: Int, month: Int, year: Int, workoutIds: String) -> Unit,
    onNavigateToCreateExercise: () -> Unit,
    onNavigateToEditExercise: (exerciseName: String, description: String, primaryMuscles: String?, secondaryMuscles: String?, imageURL: List<String>) -> Unit,
    onNavigateToStartExercise: (exerciseName: String, day: Int, month: Int, year: Int) -> Unit,
    viewModel: WorkoutLoggerOverviewModel = hiltViewModel()
) {
    val spacing = LocalSpacing.current
    val state = viewModel.state
    val workoutTemplates = viewModel.workoutTemplates.collectAsStateWithLifecycle(emptyList())
    val context = LocalContext.current
    val showDialog = remember { mutableStateOf(false) }
    val showDialogEdit = remember { mutableStateOf(false) }
    val showExercise = remember { mutableStateOf(false) }
    val showExerciseEdit = remember { mutableStateOf(false) }
    val showOptionsHeaderDialog = remember { mutableStateOf(false) }
    val optionsHeaderType = remember { mutableStateOf("") }
    val isFloatingButtonExpanded = remember { mutableStateOf(false) }
    val isCalendarExpanded = remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            if (isFloatingButtonExpanded.value) {
                Dialog(
                    onDismissRequest = {
                        isFloatingButtonExpanded.value = false
                    }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = spacing.spaceExtraLarge + spacing.spaceMedium),
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        FloatingActionButton(
                            modifier = Modifier.padding(bottom = spacing.spaceLarge, end = spacing.spaceSmall),
                            onClick = {
                                showOptionsHeaderDialog.value = true
                                optionsHeaderType.value = "workout"
                                isFloatingButtonExpanded.value = !isFloatingButtonExpanded.value
                            },
                            shape = CircleShape,
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                            contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                        ) {
                            Row(Modifier.padding(spacing.spaceMedium)) {
                                Icon(imageVector = Icons.Filled.Edit, contentDescription = "floatingActionButton Icon")
                                Spacer(modifier = Modifier.width(spacing.spaceMedium))
                                Text(text = stringResource(id = R.string.workout_routine))
                            }
                        }
                        FloatingActionButton(
                            modifier = Modifier.padding(bottom = spacing.spaceLarge, end = spacing.spaceSmall),
                            onClick = {
                                showOptionsHeaderDialog.value = true
                                optionsHeaderType.value = "exercise"
                                isFloatingButtonExpanded.value = !isFloatingButtonExpanded.value
                            },
                            shape = CircleShape,
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                            contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                        ) {
                            Row(Modifier.padding(spacing.spaceMedium)) {
                                Icon(imageVector = Icons.Filled.Edit, contentDescription = "floatingActionButton Icon")
                                Spacer(modifier = Modifier.width(spacing.spaceMedium))
                                Text(text = stringResource(id = R.string.exercise))
                            }
                        }
                        FloatingActionButton(
                            modifier = Modifier.padding(bottom = spacing.spaceLarge, end = spacing.spaceSmall),
                            onClick = {
                                viewModel.onEvent(WorkoutLoggerOverviewEvent.OnStartWorkoutClick)
                                showDialog.value = true
                                isFloatingButtonExpanded.value = !isFloatingButtonExpanded.value
                                      },
                            shape = CircleShape,
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                            contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                        ) {
                            Row(Modifier.padding(spacing.spaceMedium)) {
                                Icon(imageVector = Icons.Filled.Add, contentDescription = "floatingActionButton Icon")
                                Spacer(modifier = Modifier.width(spacing.spaceMedium))
                                Text(text = stringResource(id = R.string.start_routine))
                            }
                        }
                        FloatingActionButton(
                            modifier = Modifier.padding(bottom = spacing.spaceLarge, end = spacing.spaceSmall),
                            onClick = {
                                showExercise.value = true
                                showExerciseEdit.value = false
                                isFloatingButtonExpanded.value = !isFloatingButtonExpanded.value
                                      },
                            shape = CircleShape,
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                            contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                        ) {
                            Row(Modifier.padding(spacing.spaceMedium)) {
                                Icon(imageVector = Icons.Filled.Add, contentDescription = "floatingActionButton Icon")
                                Spacer(modifier = Modifier.width(spacing.spaceMedium))
                                Text(text = stringResource(id = R.string.start_exercise))
                            }
                        }
                        FloatingActionButton(
                            modifier = Modifier.padding(bottom = spacing.spaceLarge, end = spacing.spaceSmall),
                            onClick = { isFloatingButtonExpanded.value = !isFloatingButtonExpanded.value },
                            shape = CircleShape,
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                            contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                        ) {
                            Icon(imageVector = Icons.Filled.Close, contentDescription = "floatingActionButton Icon")
                        }
                    }

                }
            } else {
                FloatingActionButton(
                    modifier = Modifier.padding(bottom = spacing.spaceLarge, end = spacing.spaceSmall),
                    onClick = { isFloatingButtonExpanded.value = !isFloatingButtonExpanded.value },
                    shape = CircleShape,
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                ) {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = "floatingActionButton Icon")
                }
            }

        },
        content = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = spacing.spaceLarge)
            ) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(
                                RoundedCornerShape(
                                    bottomStart = 50.dp,
                                    bottomEnd = 50.dp
                                )
                            )
                            .background(MaterialTheme.colorScheme.primary)
                            .padding(spacing.spaceSmall)
                    ) {
                        Kalendar(
                            modifier = if(isCalendarExpanded.value) Modifier.height(360.dp) else Modifier,
                            showLabel = isCalendarExpanded.value,
                            currentDay = if(isCalendarExpanded.value) {
                                LocalDate(state.date.year, state.date.monthValue, state.date.dayOfMonth)
                            } else {
                                LocalDate(state.date.minusDays(6).year, state.date.minusDays(6).monthValue, state.date.minusDays(6).dayOfMonth)
                            },
                            events = state.kalendarEvents,
                            kalendarColors = KalendarColors(List(12) {KalendarColor(
                                backgroundColor = MaterialTheme.colorScheme.primary,
                                dayBackgroundColor = MaterialTheme.colorScheme.primaryContainer,
                                headerTextColor = MaterialTheme.colorScheme.onPrimary
                            )}),
                            dayContent = {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(end = 4.dp, bottom = 4.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    var isDateSelected = state.date.year == it.year && state.date.dayOfMonth == it.dayOfMonth && state.date.monthValue == it.monthNumber
                                    Column(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(
                                                if (isDateSelected) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.tertiaryContainer,
                                                RoundedCornerShape(4.dp)
                                            )
                                            .padding(spacing.spaceSmall)
                                            .clickable {
                                                viewModel.onEvent(
                                                    WorkoutLoggerOverviewEvent.OnDateClick(
                                                        it.year,
                                                        it.dayOfMonth,
                                                        it.monthNumber
                                                    )
                                                )
                                            },
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        if(!isCalendarExpanded.value){
                                            Text(
                                                text = it.dayOfWeek.name.take(3), // First 3 characters of the day name
                                                style = MaterialTheme.typography.labelMedium,
                                                color = MaterialTheme.colorScheme.onSecondaryContainer
                                            )
                                            Spacer(modifier = Modifier.height(4.dp))
                                        }
                                        Text(
                                            text = String.format("%02d", it.dayOfMonth),
                                            style = if(isCalendarExpanded.value) MaterialTheme.typography.displaySmall else MaterialTheme.typography.displayLarge,
                                            color = MaterialTheme.colorScheme.onSecondaryContainer
                                        )
                                        val isDateInEvents = state.kalendarEvents.events.any { event ->
                                            event.date == it
                                        }
                                        Box(
                                            modifier = Modifier
                                                .size(spacing.spaceSmall)
                                                .background(if(isDateInEvents) MaterialTheme.colorScheme.primary else if (isDateSelected) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.primaryContainer, CircleShape)
                                        )
                                    }
                                }
                            },
                            kalendarType = if(isCalendarExpanded.value) KalendarType.Firey else KalendarType.Oceanic
                        )
                        Spacer(modifier = Modifier.height(spacing.spaceLarge))
                        Row(
                            modifier = Modifier
                                .background(Color.Transparent)
                                .clickable { isCalendarExpanded.value = !isCalendarExpanded.value }
                                .padding(spacing.spaceSmall)
                                .fillMaxWidth(1f),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if(isCalendarExpanded.value) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowUp,
                                tint = MaterialTheme.colorScheme.onPrimary,
                                contentDescription = "Calendar Expand Button"
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = parseDateText(date = state.date),
                                style = MaterialTheme.typography.displayMedium,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                        Spacer(modifier = Modifier.height(spacing.spaceMedium))
                    }
                    if(showDialog.value){
                        WorkoutDialog(
                            onDismiss = {
                                showDialog.value = false
                                showDialogEdit.value = false
                            },
                            onChooseWorkout = { workoutName, workoutIds ->
                                if(showDialogEdit.value){
                                    onNavigateToEditWorkout(
                                        workoutName,
                                        workoutIds
                                    )
                                } else {
                                    onNavigateToStartWorkout(
                                        workoutName,
                                        state.date.dayOfMonth,
                                        state.date.monthValue,
                                        state.date.year,
                                        workoutIds
                                    )
                                }
                            },
                            workoutNames = state.workoutNames,
                            workoutTemplates = workoutTemplates
                        )
                    }
                    if(showExercise.value){
                        ExerciseDialog(
                            filterText = state.exerciseFilterText,
                            trackableExercises = state.trackableExercise,
                            onDismiss = {
                                showExercise.value = false
                                showExerciseEdit.value = false
                            },
                            onChooseExercise = {
                                if(showExerciseEdit.value){
                                    onNavigateToEditExercise(
                                        it.exercise.name!!,
                                        it.exercise.description ?: "",
                                        it.exercise.muscle_name_main,
                                        it.exercise.muscle_name_secondary,
                                        it.exercise.image_url.filterNotNull(),

                                        )
                                } else {
                                    onNavigateToStartExercise(
                                        it.exercise.name!!,
                                        state.date.dayOfMonth,
                                        state.date.monthValue,
                                        state.date.year,
                                    )
                                }
                            },
                            onFilterTextChange = {
                                viewModel.onEvent(WorkoutLoggerOverviewEvent.OnExerciseSearch(it))
                            },
                            onItemClick = {
                                viewModel.onEvent(WorkoutLoggerOverviewEvent.OnExerciseItemClick(it))
                            },
                            onDescrClick = {
                                viewModel.onEvent(WorkoutLoggerOverviewEvent.OnExerciseDescrClick(it))
                            }
                        )
                    }
                    if(showOptionsHeaderDialog.value){
                        when(optionsHeaderType.value) {
                            "workout" -> {
                                OptionsHeaderDialog(
                                    onDismiss = { showOptionsHeaderDialog.value = false },
                                    onClickCreate = {
                                        onNavigateToCreateWorkout()
                                    },
                                    onClickEdit = {
                                        showDialog.value = true
                                        showDialogEdit.value = true
                                    },
                                    title = R.string.workout_routine,
                                    text1 = stringResource(id = R.string.create_routine),
                                    text2 = stringResource(id = R.string.edit_routine)
                                )
                            }
                            "exercise" -> {
                                OptionsHeaderDialog(
                                    onDismiss = { showOptionsHeaderDialog.value = false },
                                    onClickCreate = {
                                        onNavigateToCreateExercise()
                                    },
                                    onClickEdit = {
                                        showExercise.value = true
                                        showExerciseEdit.value = true
                                    },
                                    title = R.string.exercise,
                                    text1 = stringResource(id = R.string.create_exercise),
                                    text2 = stringResource(id = R.string.edit_exercise)
                                )
                            }
                        }
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(spacing.spaceMedium))
                }
                items(viewModel.imageUrls.keys.size){// index, completedWorkout -> //items(state.completedWorkouts){ completedWorkout ->
                    CompletedWorkoutItem(
                        workout = viewModel.completedWorkouts[it],
                        imageUrl = viewModel.imageUrls[viewModel.completedWorkouts[it].exerciseName],
                        isExpanded = state.completedWorkoutIsExpanded[it],
                        modifier = Modifier,
                        onClick = {
                            viewModel.onEvent(WorkoutLoggerOverviewEvent.OnCompletedWorkoutClick(it))
                        },
                        content = {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = spacing.spaceSmall)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text= stringResource(id = R.string.sets),
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Text(
                                        text= stringResource(id = R.string.weight),
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Text(
                                        text= stringResource(id = R.string.reps),
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Text(
                                        text= stringResource(id = R.string.completed_question),
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                                val weight = viewModel.completedWorkouts[it].weight
                                val reps = viewModel.completedWorkouts[it].reps
                                val isCompleted = viewModel.completedWorkouts[it].isCompleted
                                for(i in 1..viewModel.completedWorkouts[it].sets){
                                    ExerciseRow(
                                        set = i,
                                        reps = reps[i-1].toInt(),
                                        weight = weight[i-1].toInt(),
                                        completed = isCompleted[i-1].toBoolean()
                                    )
                                    if(i != viewModel.completedWorkouts[it].sets) Divider(color = MaterialTheme.colorScheme.secondary, thickness = 1.dp)
                                }
                                Spacer(modifier = Modifier.height(spacing.spaceExtraSmall))
                            }
                        },
//                color = MaterialTheme.colors.primaryVariant
                    )
                    Spacer(modifier = Modifier.height(spacing.spaceMedium))
                }
            }
        }
    )

}