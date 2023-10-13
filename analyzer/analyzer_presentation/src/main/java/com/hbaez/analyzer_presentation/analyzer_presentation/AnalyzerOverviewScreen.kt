package com.hbaez.analyzer_presentation.analyzer_presentation

import android.util.Log
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.annotation.ExperimentalCoilApi
import com.hbaez.analyzer_presentation.analyzer_presentation.components.LineChart
import com.hbaez.core.R
import com.hbaez.core_ui.LocalSpacing
import com.hbaez.user_auth_presentation.components.FlatButton
import java.time.LocalDate

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
                text = "Personal Stats",
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.headlineMedium
            )
        }
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    stringResource(id = R.string.workout_activity),
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .padding(horizontal = spacing.spaceSmall)
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
                            color = MaterialTheme.colorScheme.surfaceVariant,
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
                                    modifier = Modifier.border(2.dp, MaterialTheme.colorScheme.surfaceVariant, CircleShape),
                                    contentAlignment = Alignment.Center,
                                    content = {
                                        Icon(
                                            imageVector = Icons.Default.ArrowForward,
                                            contentDescription = "tmp",
                                            modifier = Modifier.padding(spacing.spaceExtraSmall), // Adjust the size of the icon
                                            tint = MaterialTheme.colorScheme.onSurface // Change the icon color as needed
                                        )
                                    }
                                )
                                Divider(
                                    modifier = Modifier
                                        .padding(spacing.spaceSmall)
                                        .fillMaxHeight()
                                        .width(2.dp)
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
                                        modifier = Modifier.border(2.dp, MaterialTheme.colorScheme.surfaceVariant, CircleShape),
                                        contentAlignment = Alignment.Center,
                                        content = {
                                            Icon(
                                                imageVector = Icons.Default.ArrowForward,
                                                contentDescription = "tmp",
                                                modifier = Modifier.padding(spacing.spaceExtraSmall), // Adjust the size of the icon
                                                tint = MaterialTheme.colorScheme.onSurface // Change the icon color as needed
                                            )
                                        }
                                    )
                                    Divider(
                                        modifier = Modifier
                                            .padding(spacing.spaceSmall)
                                            .fillMaxHeight()
                                            .width(2.dp)
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
                                .fillMaxWidth()
                                .border(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.surfaceVariant,
                                    shape = RoundedCornerShape(100f)
                                )
                        ) {
                            onNavigateToWorkoutOverview(state.currentActivityDate.plusDays(6L))
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(spacing.spaceMedium))
        }
        item {
            LineChart()
            Spacer(modifier = Modifier.height(spacing.spaceExtraExtraLarge))
        }
    }
}

@OptIn(ExperimentalTextApi::class)
@Composable
fun ActivityChart(
    weeklyContributions: List<Int>,
    monthValue: Int,
    currentActivityIndex: Int,
    viewModel: AnalyzerOverviewModel = hiltViewModel()
) {
    val spacing = LocalSpacing.current
    val borderColor = MaterialTheme.colorScheme.primary
    val startColor = MaterialTheme.colorScheme.surfaceVariant
    val endColor = Color.Green
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val cellCount = 13
    val cellPadding = spacing.spaceExtraSmall
    val maxValue = (weeklyContributions.maxOrNull() ?: 1).coerceAtLeast(1)
    val textMeasurer = rememberTextMeasurer()
    val style = TextStyle(
        fontSize = MaterialTheme.typography.bodyLarge.fontSize,
        color = MaterialTheme.colorScheme.onBackground
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(((screenWidth - spacing.spaceSmall * 2 - (3 * cellPadding.value).dp) / cellCount) * 5)
    ) {
        Canvas(
            modifier = Modifier
                .padding(horizontal = spacing.spaceSmall)
                .fillMaxWidth()
                .height(((screenWidth - spacing.spaceSmall * 2 - (3 * cellPadding.value).dp) / cellCount) * 5)
                .pointerInput(true) {
                    detectTapGestures { offset ->
                        val cellSize =
                            (screenWidth.toPx() - spacing.spaceSmall.toPx() * 2 - (cellCount - 1) * cellPadding.toPx()) / cellCount
                        val columnIndex = (offset.x / (cellSize + cellPadding.toPx())).toInt()
                        var rowIndex = (offset.y / (cellSize + cellPadding.toPx())).toInt()
                        if (rowIndex != 0) {
                            rowIndex--
                            val cellIndex = rowIndex + columnIndex * 4
                            viewModel.onEvent(AnalyzerEvent.OnContributionChartClick(cellIndex))
                        }
                    }
                }
        ) {
            // Label quarterly months
            val q1 = 12 - monthValue
            val q2 = (15 - monthValue) % 12
            val q3 = (18 - monthValue) % 12
            val q4 = (21 - monthValue) % 12
            // Calculate the cellSize based on screen width and the number of columns
            val cellSize = (screenWidth.toPx() - spacing.spaceSmall.toPx() * 2 - (cellCount - 1) * cellPadding.toPx()) / cellCount

            // iterate through weeklyContributions and draw each cell
            var index = 0
            for (i in 0 until 13) {
                when(i) {
                    q1 -> {
                        drawText(
                            textMeasurer = textMeasurer,
                            text = "Jan",
                            style = style,
                            topLeft = Offset(
                                x = i * (cellSize + cellPadding.toPx()),
                                y = 0f,
                            )
                        )
                    }
                    q2 -> {
                        drawText(
                            textMeasurer = textMeasurer,
                            text = "Mar",
                            style = style,
                            topLeft = Offset(
                                x = i * (cellSize + cellPadding.toPx()),
                                y = 0f,
                            )
                        )
                    }
                    q3 -> {
                        drawText(
                            textMeasurer = textMeasurer,
                            text = "Jun",
                            style = style,
                            topLeft = Offset(
                                x = i * (cellSize + cellPadding.toPx()),
                                y = 0f,
                            )
                        )
                    }
                    q4 -> {
                        drawText(
                            textMeasurer = textMeasurer,
                            text = "Sept",
                            style = style,
                            topLeft = Offset(
                                x = i * (cellSize + cellPadding.toPx()),
                                y = 0f,
                            )
                        )
                    }
                }
                for (j in 1 until 5) {
                    val contributionCount = weeklyContributions.getOrNull(i * 4 + (j - 1)) ?: 0
                    val color = getCellColor(contributionCount, maxValue, startColor, endColor)
                    drawRoundRect(
                        color = color,
                        topLeft = Offset(i * (cellSize + cellPadding.toPx()), j * (cellSize + cellPadding.toPx())),
                        size = Size(cellSize - cellPadding.toPx(), cellSize - cellPadding.toPx()),
                        cornerRadius = CornerRadius(spacing.spaceExtraSmall.toPx())
                    )
                    if(index == currentActivityIndex){
                        drawRoundRect(
                            color = borderColor, // Specify the border color
                            topLeft = Offset(i * (cellSize + cellPadding.toPx()), j * (cellSize + cellPadding.toPx())),
                            size = Size(cellSize - cellPadding.toPx(), cellSize - cellPadding.toPx()),
                            cornerRadius = CornerRadius(spacing.spaceExtraSmall.toPx()), // Use the same corner radius as the filled rectangle
                            style = Stroke(width = 2.dp.toPx()) // Specify the border width
                        )
                    }
                    index++
                }
            }
        }
    }
}

private fun getCellColor(
    contributionCount: Int,
    maxValue: Int,
    startColor: Color,
    endColor: Color
): Color {
    val fraction = contributionCount.toFloat() / maxValue.toFloat()
    return lerp(startColor, endColor, fraction)
}