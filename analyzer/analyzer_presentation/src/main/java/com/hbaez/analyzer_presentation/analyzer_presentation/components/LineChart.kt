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
fun LineChart(pointData: List<List<Point>>){
    val steps = 4
    val pointsData: List<List<Point>>
    var maxY = (pointData.flatten().maxByOrNull { it.y }?.y ?: 100f).toInt()
    var minY = (pointData.flatten().minByOrNull { it.y }?.y ?: 0f).toInt()

    // add extra line if (maxY - minY = 0)
    if(maxY - minY == 0){
        pointsData = pointData.toMutableList()
        pointsData.add(listOf(Point(0f, 0f)))
        minY = 0
    } else pointsData = pointData

    val xAxisData = AxisData.Builder()
        .axisStepSize(100.dp)
        .backgroundColor(Color.Transparent)
        .steps((pointsData.maxByOrNull { it.size }?.size ?: 5) - 1)
        .labelData { i -> "Set ${i + 1}" }
        .labelAndAxisLinePadding(15.dp)
        .axisLineColor(MaterialTheme.colorScheme.tertiary)
        .axisLabelColor(MaterialTheme.colorScheme.tertiary)
        .build()

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
            lines = if(pointsData.isNotEmpty()) {
                pointsData.mapIndexed { index, pointsList ->
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
                }
            } else {
                   listOf(Line(
                       dataPoints = listOf(Point(0f, 0f), Point(1f, 1f), Point(2f, 2f), Point(3f, 3f)),
                       LineStyle(
                           color = getLineColor(1, 1, MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.colorScheme.outlineVariant),
                           lineType = LineType.Straight(isDotted = false)
                       ),
                       IntersectionPoint(
                           color = getLineColor(1, 1, MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.colorScheme.outline)
                       ),
                       SelectionHighlightPoint(color = MaterialTheme.colorScheme.tertiary),
                       ShadowUnderLine(
                           alpha = 0.5f,
                           brush = Brush.verticalGradient(
                               colors = listOf(
                                   getLineColor(1, 1, MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.colorScheme.outlineVariant),
                                   Color.Transparent
                               )
                           )
                       )
                   ))
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
            .height(250.dp),
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