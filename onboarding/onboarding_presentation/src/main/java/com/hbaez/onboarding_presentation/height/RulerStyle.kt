package com.hbaez.onboarding_presentation.height

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class RulerStyle(
    val minHeight: Int = 12,
    val maxHeight: Int = 108,
    val initialHeight: Int = 70,
    val rulerHeight: Dp = 300.dp,
    val rulerColor: Int = android.graphics.Color.WHITE,
    val normalLineColor: Color = Color.LightGray,
    val fiveStepLineColor: Color = Color.Green,
    val tenStepLineColor: Color = Color.Black,
    val normalLineLength: Dp = 15.dp,
    val fiveStepLineLength: Dp = 25.dp,
    val tenStepLineLength: Dp = 35.dp,
    val heightIndicatorColor: Color = Color.Green,
    val heightIndicatorLength: Dp = 60.dp,
    val textSize: TextUnit = 18.sp,
    val textColor: Int = android.graphics.Color.BLACK
)
