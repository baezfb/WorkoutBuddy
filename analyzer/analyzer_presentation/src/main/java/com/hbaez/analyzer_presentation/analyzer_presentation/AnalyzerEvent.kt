package com.hbaez.analyzer_presentation.analyzer_presentation

sealed class AnalyzerEvent {

    data class OnContributionChartClick(val index: Int): AnalyzerEvent()
}