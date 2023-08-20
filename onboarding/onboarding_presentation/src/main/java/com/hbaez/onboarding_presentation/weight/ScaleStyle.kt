package com.hbaez.onboarding_presentation.weight

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.graphics.Color as color

data class ScaleStyle(
    val minWeight: Int = 50,
    val maxWeight: Int = 400,
    val initialWeight: Int = 180,
    val scaleWidth: Dp = 100.dp,
    val radius: Dp = 500.dp,
    val scaleColor: Int = color.WHITE,
    val normalLineColor: Color = Color.LightGray,
    val fiveStepLineColor: Color = Color.Green,
    val tenStepLineColor: Color = Color.Black,
    val normalLineLength: Dp = 15.dp,
    val fiveStepLineLength: Dp = 25.dp,
    val tenStepLineLength: Dp = 35.dp,
    val scaleIndicatorColor: Color = Color.Green,
    val scaleIndicatorLength: Dp = 60.dp,
    val shadowColor: Int = color.RED,
    val textSize: TextUnit = 18.sp,
    val textColor: Int = color.BLACK
)
