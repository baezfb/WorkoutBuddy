package com.hbaez.analyzer_presentation.analyzer_presentation

sealed class AnalyzerEvent {

    data class onContributionChartClick(val index: Int): AnalyzerEvent()
}