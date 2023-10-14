package com.hbaez.analyzer_presentation.analyzer_presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.unit.dp
import co.yml.charts.axis.AxisData
import co.yml.charts.common.model.Point
import co.yml.charts.ui.linechart.model.GridLines
import co.yml.charts.ui.linechart.model.IntersectionPoint
import co.yml.charts.ui.linechart.model.Line
import co.yml.charts.ui.linechart.model.LineChartData
import co.yml.charts.ui.linechart.model.LinePlotData
import co.yml.charts.ui.linechart.model.LineStyle
import co.yml.charts.ui.linechart.model.LineType
import co.yml.charts.ui.linechart.model.SelectionHighlightPoint
import co.yml.charts.ui.linechart.model.SelectionHighlightPopUp
import co.yml.charts.ui.linechart.model.ShadowUnderLine

@Composable
fun LineChart(pointsData: List<List<Point>>){
    val steps = 4

    val xAxisData = AxisData.Builder()
        .axisStepSize(100.dp)
        .backgroundColor(Color.Transparent)
        .steps((pointsData.maxByOrNull { it.size }?.size ?: 1) - 1)
        .labelData { i -> "Set ${i + 1}" }
        .labelAndAxisLinePadding(15.dp)
        .axisLineColor(MaterialTheme.colorScheme.tertiary)
        .axisLabelColor(MaterialTheme.colorScheme.tertiary)
        .build()

    val maxY = (pointsData.flatten().maxByOrNull { it.y }?.y ?: 0f).toInt()
    val minY = (pointsData.flatten().minByOrNull { it.y }?.y ?: 0f).toInt()
    val yAxisData = AxisData.Builder()
        .steps(steps)
        .backgroundColor(Color.Transparent)
        .labelAndAxisLinePadding(20.dp)
        .labelData { i ->
            val yScale = (maxY - minY).toFloat() / steps
            (i * yScale + minY).toInt().toString()
        }
        .axisLineColor(MaterialTheme.colorScheme.tertiary)
        .axisLabelColor(MaterialTheme.colorScheme.tertiary)
        .build()

    val lineChartData = LineChartData(
        linePlotData = LinePlotData(
            lines = pointsData.mapIndexed { index, pointsList ->
                Line(
                    dataPoints = pointsList,
                    LineStyle(
                        color = getLineColor(index, pointsData.size, MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.colorScheme.tertiary),
                        lineType = LineType.Straight(isDotted = false)
                    ),
                    IntersectionPoint(
                        color = getLineColor(index, pointsData.size, MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.colorScheme.tertiary)
                    ),
                    SelectionHighlightPoint(color = MaterialTheme.colorScheme.tertiary),
                    ShadowUnderLine(
                        alpha = 0.5f,
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                getLineColor(index, pointsData.size, MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.colorScheme.inversePrimary),
                                Color.Transparent
                            )
                        )
                    ),
                    SelectionHighlightPopUp()
                )
            },
        ),
        backgroundColor = MaterialTheme.colorScheme.surface,
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        gridLines = GridLines(color = MaterialTheme.colorScheme.outlineVariant)
    )

    co.yml.charts.ui.linechart.LineChart(
        modifier = Modifier
            .fillMaxWidth()
            .height(350.dp),
        lineChartData = lineChartData
    )
}

private fun getLineColor(
    orderIndex: Int,
    maxValue: Int,
    startColor: Color,
    endColor: Color
): Color {
    val fraction = orderIndex.toFloat() / maxValue.toFloat()
    return lerp(startColor, endColor, fraction)
}