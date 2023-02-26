package com.hbaez.onboarding_presentation.height

sealed class LineType {
    object Normal : LineType()
    object FiveStep: LineType()
    object TenStep: LineType()
}
