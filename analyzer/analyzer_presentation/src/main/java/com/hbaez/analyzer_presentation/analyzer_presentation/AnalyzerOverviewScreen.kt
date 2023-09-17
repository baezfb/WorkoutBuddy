package com.hbaez.analyzer_presentation.analyzer_presentation

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.annotation.ExperimentalCoilApi
import com.hbaez.core_ui.LocalSpacing
import java.time.DayOfWeek

@ExperimentalCoilApi
@Composable
fun AnalyzerOverviewScreen(
    viewModel: AnalyzerOverviewModel = hiltViewModel()
){
    val spacing = LocalSpacing.current
    val state = viewModel.state

    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text("Analyzer placeholder", textAlign = TextAlign.Center)

        ActivityChart(weeklyContributions = state.activityCountList)

        Text(state.date.with(DayOfWeek.SUNDAY).minusDays((51 - state.currentActivityIndex) * 7L).toString(), textAlign = TextAlign.Center)
    }
}

@Composable
fun ActivityChart(
    weeklyContributions: List<Int>,
    viewModel: AnalyzerOverviewModel = hiltViewModel()
) {
    val spacing = LocalSpacing.current
    val startColor = MaterialTheme.colorScheme.surfaceVariant
    val endColor = Color.Green
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val cellCount = 13
    val cellPadding = spacing.spaceExtraSmall
    val maxValue = (weeklyContributions.maxOrNull() ?: 1).coerceAtLeast(1)
    Log.println(Log.DEBUG, "activityCountList", weeklyContributions.toString())

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(((screenWidth - spacing.spaceSmall * 2 - (3 * cellPadding.value).dp) / cellCount) * 4)
    ) {
        Canvas(
            modifier = Modifier
                .padding(horizontal = spacing.spaceSmall)
                .fillMaxWidth()
                .height(((screenWidth - spacing.spaceSmall * 2 - (3 * cellPadding.value).dp) / cellCount) * 4)
                .pointerInput(true) {
                    detectTapGestures { offset ->
                        val cellSize =
                            (screenWidth.toPx() - spacing.spaceSmall.toPx() * 2 - (cellCount - 1) * cellPadding.toPx()) / cellCount
                        val columnIndex = (offset.x / (cellSize + cellPadding.toPx())).toInt()
                        val rowIndex = (offset.y / (cellSize + cellPadding.toPx())).toInt()
                        val cellIndex = rowIndex + columnIndex * 4
                        viewModel.onEvent(AnalyzerEvent.OnContributionChartClick(cellIndex))
//                    onCellClick(cellIndex)
                        Log.println(Log.DEBUG, "squareTapped", cellIndex.toString())
                        Log.println(Log.DEBUG, "squareTapped column", columnIndex.toString())
                        Log.println(Log.DEBUG, "squareTapped row", rowIndex.toString())
                    }
                }
        ) {
            // Calculate the cellSize based on screen width and the number of columns
            val cellSize = (screenWidth.toPx() - spacing.spaceSmall.toPx() * 2 - (cellCount - 1) * cellPadding.toPx()) / cellCount

            // Draw the chart cells here based on weeklyContributions
            // You'll need to iterate through weeklyContributions and draw each cell
            for (i in 0 until 13) {
                for (j in 0 until 4) {
                    val contributionCount = weeklyContributions.getOrNull(i * 4 + j) ?: 0
                    val color = getCellColor(contributionCount, maxValue, startColor, endColor)
                    drawRoundRect(
                        color = color,
                        topLeft = Offset(i * (cellSize + cellPadding.toPx()), j * (cellSize + cellPadding.toPx())),
                        size = Size(cellSize - cellPadding.toPx(), cellSize - cellPadding.toPx()),
                        cornerRadius = CornerRadius(spacing.spaceExtraSmall.toPx())
                    )
                }
            }
        }
    }
}

private fun getCellColor(contributionCount: Int, maxValue: Int, startColor: Color, endColor: Color): Color {
    // Define your color logic here
    // You can use a gradient or a set of predefined colors
    // For simplicity, we'll use a gradient from green to red
    val fraction = contributionCount.toFloat() / maxValue.toFloat()
    val color = lerp(startColor, endColor, fraction)
    return color
}