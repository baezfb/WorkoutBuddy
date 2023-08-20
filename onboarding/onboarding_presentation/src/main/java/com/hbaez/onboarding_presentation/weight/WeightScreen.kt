package com.hbaez.onboarding_presentation.weight

import androidx.compose.ui.graphics.Color
import android.graphics.Color as ColorG
import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.LastBaseline
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.withRotation
import androidx.hilt.navigation.compose.hiltViewModel
import com.hbaez.core_ui.LocalSpacing
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin
import com.hbaez.core.R
import com.hbaez.core.util.UiEvent
import com.hbaez.onboarding_presentation.components.ActionButton

@Composable
fun WeightScreen(
    snackBarHost: SnackbarHostState,
    onNextClick: () -> Unit,
    viewModel: WeightViewModel = hiltViewModel()
) {
    val spacing = LocalSpacing.current
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.Success -> onNextClick()
                is UiEvent.ShowSnackbar -> {
                    snackBarHost.showSnackbar(
                        message = event.message.asString(context)
                    )
                }
                else -> Unit
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(spacing.spaceLarge)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.whats_your_weight),
                style = MaterialTheme.typography.displayMedium
            )
            Row(
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = viewModel.weight,
                    style = MaterialTheme.typography.displayLarge,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier
                        .alignBy(LastBaseline)
                )
                Spacer(modifier = Modifier.width(spacing.spaceSmall))
                Text(
                    text = stringResource(id = R.string.lbs),
                    modifier = Modifier.alignBy(LastBaseline)
                )
            }
            Spacer(modifier = Modifier.height(spacing.spaceExtraLarge))
            Scale(
                style = ScaleStyle(
                    scaleWidth = 150.dp,
                    initialWeight = viewModel.initWeight.toInt(),
                    scaleColor = MaterialTheme.colorScheme.primaryContainer.toArgb(),
                    normalLineColor = MaterialTheme.colorScheme.secondary,
                    fiveStepLineColor = MaterialTheme.colorScheme.secondary,
                    tenStepLineColor = MaterialTheme.colorScheme.tertiary,
                    shadowColor = MaterialTheme.colorScheme.inversePrimary.toArgb(),
                    textColor = MaterialTheme.colorScheme.onPrimaryContainer.toArgb(),
                    scaleIndicatorColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
            ) {
                viewModel.onWeightChange(it.toString())
            }
        }
        Button(
            onClick = viewModel::onNextClick,
            modifier = Modifier.align(Alignment.BottomEnd),
            shape = RoundedCornerShape(100.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Next"
            )
        }
    }
}

@Composable
fun Scale(
    modifier: Modifier = Modifier,
    style: ScaleStyle = ScaleStyle(),
    onWeightChange: (Int) -> Unit,
) {
    val radius = style.radius
    val scaleWidth = style.scaleWidth
    var center by remember {
        mutableStateOf(Offset.Zero)
    }
    var circleCenter by remember {
        mutableStateOf(Offset.Zero)
    }
    var angle by remember {
        mutableStateOf(0f)
    }
    var dragStartedAngle by remember {
        mutableStateOf(0f)
    }
    var oldAngle by remember {
        mutableStateOf(angle)
    }
    Canvas(
        modifier = modifier
            .pointerInput(true) {
                detectDragGestures(
                    onDragStart = { offset ->
                        dragStartedAngle = -atan2(
                            y = circleCenter.x - offset.x,
                            x = circleCenter.y - offset.y
                        ) * (180f / PI.toFloat())
                    },
                    onDragEnd = {
                        oldAngle = angle
                    }
                ) { change, _ ->
                    val touchAngle = -atan2(
                        y = circleCenter.x - change.position.x,
                        x = circleCenter.y - change.position.y
                    ) * (180f / PI.toFloat())

                    val newAngle = oldAngle + (touchAngle - dragStartedAngle)
                    angle = newAngle.coerceIn(
                        minimumValue = style.initialWeight - style.maxWeight.toFloat(),
                        maximumValue = style.initialWeight - style.minWeight.toFloat()
                    )
                    onWeightChange((style.initialWeight - angle).roundToInt())
                }
            }
    ) {
        center = this.center
        circleCenter = Offset(center.x, scaleWidth.toPx() / 2f + radius.toPx())
        val outerRadius = radius.toPx() + scaleWidth.toPx() / 2f
        val innerRadius = radius.toPx() - scaleWidth.toPx() / 2f
        drawContext.canvas.nativeCanvas.apply {
            drawCircle(
                circleCenter.x,
                circleCenter.y,
                radius.toPx(),
                Paint().apply {
                    strokeWidth = scaleWidth.toPx()
                    color = style.scaleColor
                    setStyle(Paint.Style.STROKE)
                    setShadowLayer(
                        15f,
                        0f,
                        0f,
                        style.shadowColor
                    )
                }
            )
        }
        // Draw Lines
        for(i in style.minWeight..style.maxWeight){
            val angleInRad = (i - style.initialWeight + angle - 90) * (PI / 180f).toFloat()
            val lineType = when {
                i % 10 == 0 -> LineType.TenStep
                i % 5 == 0 -> LineType.FiveStep
                else -> LineType.Normal
            }
            val lineLength = when(lineType) {
                LineType.Normal -> style.normalLineLength.toPx()
                LineType.FiveStep -> style.fiveStepLineLength.toPx()
                LineType.TenStep -> style.tenStepLineLength.toPx()
            }
            val lineColor = when(lineType) {
                LineType.Normal -> style.normalLineColor
                LineType.FiveStep -> style.fiveStepLineColor
                LineType.TenStep -> style.tenStepLineColor
            }
            val lineStart = Offset(
                x = (outerRadius - lineLength) * cos(angleInRad) + circleCenter.x,
                y = (outerRadius - lineLength) * sin(angleInRad) + circleCenter.y
            )
            val lineEnd = Offset(
                x = outerRadius * cos(angleInRad) + circleCenter.x,
                y = outerRadius * sin(angleInRad) + circleCenter.y
            )

            drawContext.canvas.nativeCanvas.apply {
                if(lineType is LineType.TenStep){
                    val textRadius = (outerRadius - lineLength - 5.dp.toPx() - style.textSize.toPx())
                    val x = textRadius * cos(angleInRad) + circleCenter.x
                    val y = textRadius * sin(angleInRad) + circleCenter.y
                    withRotation(
                        degrees = angleInRad * (180f / PI.toFloat()) + 90f,
                        pivotX = x,
                        pivotY = y
                    ) {
                        drawText(
                            abs(i).toString(),
                            x,
                            y,
                            Paint().apply {
                                textSize = style.textSize.toPx()
                                textAlign = Paint.Align.CENTER
                                color = style.textColor
                            }
                        )
                    }
                }
            }
            drawLine(
                color = lineColor,
                start = lineStart,
                end = lineEnd,
                strokeWidth = 1.dp.toPx()
            )
            val middleTop = Offset(
                x = circleCenter.x,
                y = circleCenter.y - innerRadius - style.scaleIndicatorLength.toPx()
            )
            val bottomLeft = Offset(
                x = circleCenter.x - 4f,
                y = circleCenter.y - innerRadius
            )
            val bottomRight = Offset(
                x = circleCenter.x + 4f,
                y = circleCenter.y - innerRadius
            )
            val indicator = Path().apply {
                moveTo(middleTop.x, middleTop.y)
                lineTo(bottomLeft.x, bottomLeft.y)
                lineTo(bottomRight.x, bottomRight.y)
                lineTo(middleTop.x, middleTop.y)
            }
            drawPath(
                path = indicator,
                color = style.scaleIndicatorColor
            )
        }
    }
}