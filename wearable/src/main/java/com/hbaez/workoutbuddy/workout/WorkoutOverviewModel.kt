package com.hbaez.workoutbuddy.workout

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.hbaez.core.util.UiEvent
import com.hbaez.user_auth_presentation.model.service.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class WorkoutOverviewModel @Inject constructor(
    private val storageService: StorageService
): ViewModel() {

    var state by mutableStateOf(WorkoutOverviewState())
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    val workoutTemplates = storageService.workouts

    fun onEvent(event: WorkoutOverviewEvent) {
        when(event) {
            is WorkoutOverviewEvent.OnWorkoutClick -> {

            }
        }
    }
}