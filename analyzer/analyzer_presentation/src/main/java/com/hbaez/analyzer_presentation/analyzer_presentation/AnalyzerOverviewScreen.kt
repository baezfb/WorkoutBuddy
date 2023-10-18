package com.hbaez.analyzer_presentation.analyzer_presentation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.annotation.ExperimentalCoilApi
import com.hbaez.analyzer_presentation.analyzer_presentation.components.ActivityChart
import com.hbaez.analyzer_presentation.analyzer_presentation.components.LineChart
import com.hbaez.core.R
import com.hbaez.core_ui.LocalSpacing
import com.hbaez.user_auth_presentation.components.FlatButton
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@ExperimentalCoilApi
@Composable
fun AnalyzerOverviewScreen(
    viewModel: AnalyzerOverviewModel = hiltViewModel(),
    onNavigateToWorkoutOverview: (date: LocalDate) -> Unit
){
    val spacing = LocalSpacing.current
    val state = viewModel.state

    LazyColumn(
        Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        item {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(spacing.spaceMedium),
                text = stringResource(id = R.string.activity_overview),
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.titleLarge
            )
            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(spacing.spaceLarge))
                    .padding(spacing.spaceExtraSmall)
                    .shadow(
                        elevation = 2.dp,
                        shape = RoundedCornerShape(spacing.spaceLarge)
                    )
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .fillMaxWidth()
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(spacing.spaceMedium),
                    text = stringResource(id = R.string.workout_activity),
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.titleLarge
                )
                ActivityChart(weeklyContributions = state.activityCountList, monthValue = state.date.monthValue, currentActivityIndex = state.currentActivityIndex)
                Spacer(modifier = Modifier.height(spacing.spaceSmall))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "${state.currentActivityDate.month.name} ${state.currentActivityDate.dayOfMonth}, ${state.currentActivityDate.year}",
                        textAlign = TextAlign.Start,
                        modifier = Modifier
                            .padding(horizontal = spacing.spaceSmall)
                    )
                    Divider(Modifier.padding(end = spacing.spaceSmall))
                }
                Spacer(Modifier.height(spacing.spaceMedium))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = spacing.spaceExtraExtraLarge),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = if (state.workoutList.isEmpty()) Arrangement.Center else Arrangement.Top
                ) {
                    if(state.workoutList.isEmpty()){
                        Text(
                            stringResource(id = R.string.no_activity),
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.inverseSurface,
                            modifier = Modifier
                                .padding(horizontal = spacing.spaceSmall)
                        )
                    } else {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(IntrinsicSize.Min)
                        ) {
                            Column(
                                modifier = Modifier.padding(horizontal = spacing.spaceSmall),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(
                                    modifier = Modifier.border(2.dp, MaterialTheme.colorScheme.onPrimaryContainer, CircleShape),
                                    contentAlignment = Alignment.Center,
                                    content = {
                                        Icon(
                                            imageVector = Icons.Default.ArrowForward,
                                            contentDescription = "tmp",
                                            modifier = Modifier.padding(spacing.spaceExtraSmall), // Adjust the size of the icon
                                            tint = MaterialTheme.colorScheme.onPrimaryContainer // Change the icon color as needed
                                        )
                                    }
                                )
                                Divider(
                                    modifier = Modifier
                                        .padding(spacing.spaceSmall)
                                        .fillMaxHeight()
                                        .width(2.dp),
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.Start
                            ) {
                                Text(
                                    "${state.workoutList.fold(0) { accumulator, element -> accumulator + element[1].toInt() }} exercises in ${state.workoutList.size} workouts.",
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.displaySmall,
                                    modifier = Modifier
                                        .padding(horizontal = spacing.spaceSmall)
                                )
                                Spacer(modifier = Modifier.height(spacing.spaceSmall))
                                state.workoutList.forEach {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            it[0],
                                            textAlign = TextAlign.Start,
                                            style = MaterialTheme.typography.labelLarge,
                                            modifier = Modifier.padding(start = spacing.spaceLarge)
                                        )
                                        Text(
                                            "(${it[1]} exercises)",
                                            textAlign = TextAlign.Start,
                                            style = MaterialTheme.typography.labelMedium,
                                            color = MaterialTheme.colorScheme.surfaceTint,
                                            modifier = Modifier.padding(start = spacing.spaceExtraSmall)
                                        )

                                    }
                                    Spacer(modifier = Modifier.height(spacing.spaceExtraSmall))
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(spacing.spaceSmall))
                        if(state.exerciseList.isNotEmpty()) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(IntrinsicSize.Min)
                            ) {
                                Column(
                                    modifier = Modifier.padding(horizontal = spacing.spaceSmall),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Box(
                                        modifier = Modifier.border(2.dp, MaterialTheme.colorScheme.onPrimaryContainer, CircleShape),
                                        contentAlignment = Alignment.Center,
                                        content = {
                                            Icon(
                                                imageVector = Icons.Default.ArrowForward,
                                                contentDescription = "tmp",
                                                modifier = Modifier.padding(spacing.spaceExtraSmall), // Adjust the size of the icon
                                                tint = MaterialTheme.colorScheme.onPrimaryContainer // Change the icon color as needed
                                            )
                                        }
                                    )
                                    Divider(
                                        modifier = Modifier
                                            .padding(spacing.spaceSmall)
                                            .fillMaxHeight()
                                            .width(2.dp),
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalAlignment = Alignment.Start
                                ) {
                                    Text(
                                        "Completed ${state.exerciseList.size} solo exercises.",
                                        textAlign = TextAlign.Center,
                                        style = MaterialTheme.typography.displaySmall,
                                        modifier = Modifier
                                            .padding(horizontal = spacing.spaceSmall)
                                    )
                                    Spacer(modifier = Modifier.height(spacing.spaceSmall))
                                    state.exerciseList.forEach {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                it,
                                                textAlign = TextAlign.Start,
                                                style = MaterialTheme.typography.labelLarge,
                                                modifier = Modifier.padding(start = spacing.spaceLarge),
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(spacing.spaceExtraSmall))
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(spacing.spaceSmall))
                        FlatButton(
                            text = R.string.show_more_activity,
                            modifier = Modifier
                                .fillMaxWidth(.95f)
                                .border(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    shape = RoundedCornerShape(100f)
                                )
                        ) {
                            onNavigateToWorkoutOverview(state.currentActivityDate.plusDays(6L))
                        }
                        Spacer(modifier = Modifier.height(spacing.spaceSmall))
                    }
                }
            }
            Spacer(modifier = Modifier.height(spacing.spaceMedium))
        }
        item {
            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(spacing.spaceLarge))
                    .padding(spacing.spaceExtraSmall)
                    .shadow(
                        elevation = 2.dp,
                        shape = RoundedCornerShape(spacing.spaceLarge)
                    )
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .fillMaxWidth()
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(spacing.spaceMedium),
                    text = stringResource(id = R.string.exercise_activity),
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.titleLarge
                )
                ExposedDropdownMenuBox(
                    modifier = Modifier.fillMaxWidth(.66f).padding(start = spacing.spaceMedium),
                    expanded = state.graph1_dropDownMenuExpanded,
                    onExpandedChange = {
                        viewModel.onEvent(AnalyzerEvent.OnGraphOneDropDownMenuClick)
                    }
                ) {
                    OutlinedTextField(
                        value = state.graph1_exerciseName,
                        onValueChange = { viewModel.onEvent(AnalyzerEvent.OnExerciseNameChange(it)) },
                        singleLine = true,
                        keyboardActions = KeyboardActions(
                            onSearch = {
                                defaultKeyboardAction(ImeAction.Search)
                            }
                        ),
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done,
                        ),
                        textStyle = TextStyle(MaterialTheme.colorScheme.onPrimaryContainer),
                        modifier = Modifier
                            .clip(RoundedCornerShape(5.dp))
                            .padding(2.dp)
                            .shadow(
                                elevation = 2.dp,
                                shape = RoundedCornerShape(5.dp)
                            )
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .fillMaxWidth()
                            .padding(end = spacing.spaceMedium)
                            .menuAnchor(),
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = state.graph1_dropDownMenuExpanded)
                        }
                    )
                    ExposedDropdownMenu(
                        expanded = state.graph1_dropDownMenuExpanded,
                        onDismissRequest = {
                            viewModel.onEvent(AnalyzerEvent.OnGraphOneDropDownMenuClick)
                        }
                    ) {
                        state.exerciseNameList.forEach { selectionOption ->
                            DropdownMenuItem(
                                text = { Text(text = selectionOption) },
                                onClick = {
                                    viewModel.onEvent(AnalyzerEvent.OnGraphOneDropDownMenuClick)
                                    viewModel.onEvent(AnalyzerEvent.OnExerciseNameChange(selectionOption))
                                    viewModel.onEvent(AnalyzerEvent.OnChooseExerciseGraphOne(selectionOption))
                                }
                            )
                        }
                    }
                }
                LineChart(state.graph1_repsPointsData)
                Spacer(modifier = Modifier.height(spacing.spaceExtraExtraLarge))
            }
        }
    }
}