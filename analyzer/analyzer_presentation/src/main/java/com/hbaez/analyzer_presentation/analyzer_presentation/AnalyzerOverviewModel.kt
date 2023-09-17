package com.hbaez.analyzer_presentation.analyzer_presentation

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hbaez.analyzer_presentation.analyzer_presentation.components.CalculateActivityIndexFromDate
import com.hbaez.core.domain.preferences.Preferences
import com.hbaez.core.util.UiEvent
import com.hbaez.user_auth_presentation.model.service.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class AnalyzerOverviewModel @Inject constructor(
    private val storageService: StorageService,
    private val preferences: Preferences
): ViewModel(){

    var state by mutableStateOf(AnalyzerState())
        private set

    val workoutTemplates = storageService.workouts

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        viewModelScope.launch {
            val activityList = MutableList(52) { 0 } // 52 weeks
            storageService.calendarDates.first().calendarDates.forEach {
                val currDate = LocalDate.parse(it)
                val index = CalculateActivityIndexFromDate(currDate, state.date)
                activityList[index] += 1
            }
            state = state.copy(
                activityCountList = activityList
            )
        }
    }

    fun onEvent(event: AnalyzerEvent){
        when(event) {
            is AnalyzerEvent.OnContributionChartClick -> {
                state = state.copy(
                    currentActivityIndex = event.index
                )
            }
        }
    }
}