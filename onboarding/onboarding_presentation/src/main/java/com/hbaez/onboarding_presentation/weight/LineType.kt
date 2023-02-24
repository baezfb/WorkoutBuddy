package com.hbaez.onboarding_presentation.weight

sealed class LineType {
    object Normal : LineType()
    object FiveStep: LineType()
    object TenStep: LineType()
}
