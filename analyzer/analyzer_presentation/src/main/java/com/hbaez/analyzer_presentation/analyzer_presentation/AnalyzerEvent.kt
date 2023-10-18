package com.hbaez.analyzer_presentation.analyzer_presentation

sealed class AnalyzerEvent {
    data class OnContributionChartClick(val index: Int): AnalyzerEvent()
    data class OnChooseExerciseGraphOne(val exerciseName: String): AnalyzerEvent()
    data class OnChooseOptionGraphOne(val option: String): AnalyzerEvent()
    data class OnExerciseNameChange(val exerciseName: String): AnalyzerEvent()
    object OnGraphOneDropDownMenuClick: AnalyzerEvent()
    object OnGraphOneDropDownOptionClick: AnalyzerEvent()
}