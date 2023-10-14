package com.hbaez.analyzer_presentation.analyzer_presentation

import co.yml.charts.common.model.Point
import java.time.DayOfWeek
import java.time.LocalDate

data class AnalyzerState(
    val date: LocalDate = LocalDate.now(),
    val currentActivityIndex: Int = 51,
    val currentActivityDate: LocalDate = date.with(DayOfWeek.MONDAY).minusDays((51 - currentActivityIndex) * 7L),
    val activityCountList: List<Int> = List(52) { 0 },
    val workoutList: List<List<String>> = mutableListOf(),
    val exerciseList: List<String> = emptyList(),
    val graph1_exerciseName: String = "Crunches on Machine",
    val graph1_weightPointsData: List<List<Point>> = emptyList(),
    val graph1_repsPointsData: List<List<Point>> = emptyList()
)
