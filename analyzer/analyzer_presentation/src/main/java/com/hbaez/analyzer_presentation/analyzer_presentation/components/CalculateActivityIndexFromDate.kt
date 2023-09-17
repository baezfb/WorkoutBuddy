package com.hbaez.analyzer_presentation.analyzer_presentation.components

import java.time.LocalDate
import java.time.temporal.ChronoUnit

fun CalculateActivityIndexFromDate(
    date: LocalDate,
    endDate: LocalDate
): Int {
    val weeksBetween = ChronoUnit.WEEKS.between(date, endDate)
    return 51 - weeksBetween.toInt()
}