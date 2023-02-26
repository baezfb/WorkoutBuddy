package com.hbaez.onboarding_presentation.height

import android.graphics.Paint
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.LastBaseline
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hbaez.core.R
import com.hbaez.core.util.UiEvent
import com.hbaez.core_ui.LocalSpacing
import com.hbaez.onboarding_presentation.components.ActionButton
import com.hbaez.onboarding_presentation.weight.LineType
import kotlin.math.abs
import kotlin.math.absoluteValue
import kotlin.math.roundToInt
import android.graphics.Color as ColorG

@Composable
fun HeightScreen(
    scaffoldState: ScaffoldState,
    onNextClick: () -> Unit,
    viewModel: HeightViewModel = hiltViewModel()
) {
    val spacing = LocalSpacing.current
    val context = LocalContext.current
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.Success -> onNextClick()
                is UiEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
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
                text = stringResource(id = R.string.whats_your_height),
                style = MaterialTheme.typography.h2
            )
            Row(
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = viewModel.height,
                    style = MaterialTheme.typography.h1,
                    color = MaterialTheme.colors.primaryVariant,
                    modifier = Modifier
                        .alignBy(LastBaseline)
                )
                Spacer(modifier = Modifier.width(spacing.spaceSmall))
                Text(
                    text = stringResource(id = R.string.inches),
                    modifier = Modifier.alignBy(LastBaseline)
                )
            }
            Ruler(
                style = RulerStyle(
                    rulerHeight = 125.dp,
                    rulerColor = ColorG.BLACK,
                    normalLineColor = Color.White,
                    fiveStepLineColor = Color.White,
                    tenStepLineColor = MaterialTheme.colors.primary,
                    textColor = ColorG.WHITE,
                    heightIndicatorColor = MaterialTheme.colors.primaryVariant
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(125.dp),
            ){
                viewModel.onHeightChange(it.toString())
            }
        }
        ActionButton(
            text = stringResource(id = R.string.next),
            onClick = viewModel::onNextClick,
            modifier = Modifier.align(Alignment.BottomEnd)
        )
    }
}

@Composable
fun Ruler(
    modifier: Modifier = Modifier,
    style: RulerStyle = RulerStyle(),
    onHeightChange: (Int) -> Unit,
) {
    val rulerHeight = style.rulerHeight
    var width = 12500f
    val rectTopLeft = Offset(x=0f, y=0f)
    val spaceBetween = width / (style.maxHeight * 10 - style.minHeight * 10)
    var center by remember {
        mutableStateOf(Offset.Zero)
    }
    var rulerCenter by remember {
        mutableStateOf(Offset.Zero)
    }
    var posX by remember {
        mutableStateOf(0f) // canvas this.center
    }
    var dragStartedPosition by remember {
        mutableStateOf(0f)
    }
    var oldPosition by remember {
        mutableStateOf(posX)
    }

    Canvas(
        modifier = modifier
            .pointerInput(true) {
                detectDragGestures(
                    onDragStart = { offset ->
                        dragStartedPosition = center.x - offset.x
                    },
                    onDragEnd = {
                        oldPosition = posX
                    }
                ) { change, _ ->
                    val touchPosition = center.x - change.position.x
                    val newPosition = oldPosition + (touchPosition - dragStartedPosition)

                    posX = newPosition.coerceIn(
                        minimumValue = center.x + spaceBetween * 10 * (style.minHeight - style.initialHeight),
                        maximumValue = center.x + spaceBetween * 10 * (style.maxHeight - style.initialHeight)
                    )

                    onHeightChange(((posX - center.x) / (spaceBetween * 10) + style.initialHeight).roundToInt())
                }
            }
    ) {
        center = this.center
        rulerCenter = Offset(center.x, center.y)
        drawContext.canvas.nativeCanvas.apply {
            drawRect(
                rectTopLeft.x,
                rectTopLeft.y,
                width,
                rulerHeight.toPx(),
                Paint().apply {
                    color = style.rulerColor
                    setStyle(Paint.Style.FILL)
                    setShadowLayer(
                        60f,
                        0f,
                        0f,
                        ColorG.argb(50,250,0,0)
                    )
                }
            )
        }

        // draw lines
        for(i in (style.minHeight * 10)..(style.maxHeight * 10)){
            val linePosX= center.x + spaceBetween * (i - style.initialHeight * 10)
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
                x = linePosX,
                y = rectTopLeft.y
            )
            val lineEnd = Offset(
                x = linePosX,
                y = rectTopLeft.y + lineLength
            )
            drawLine(
                color = lineColor,
                start = lineStart,
                end = lineEnd,
                strokeWidth = 1.dp.toPx()
            )

            drawContext.canvas.nativeCanvas.apply {
                if(lineType is LineType.TenStep) {
                    drawText(
                        abs((i/10f).toInt()).toString(),
                        linePosX,
                        rectTopLeft.y + lineLength + 5.dp.toPx() + style.textSize.toPx(),
                        Paint().apply {
                            textSize = style.textSize.toPx()
                            textAlign = Paint.Align.CENTER
                            color = style.textColor
                        }
                    )
                }
            }
        }
    }
}