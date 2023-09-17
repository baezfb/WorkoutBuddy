package com.hbaez.analyzer_presentation.analyzer_presentation

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.hbaez.core.domain.preferences.Preferences
import com.hbaez.core.util.UiEvent
import com.hbaez.user_auth_presentation.model.service.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
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

    fun onEvent(event: AnalyzerEvent){
        when(event) {
            is AnalyzerEvent.onContributionChartClick -> {
                state = state.copy(
                    currentContributionIndex = event.index
                )
            }
        }
    }
}