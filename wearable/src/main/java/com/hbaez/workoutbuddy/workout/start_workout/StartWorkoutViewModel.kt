package com.hbaez.workoutbuddy.workout.start_workout

import android.util.Log
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
class StartWorkoutViewModel @Inject constructor(
    private val storageService: StorageService
): ViewModel() {

    var state by mutableStateOf(StartWorkoutState())
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    val workoutTemplates = storageService.workouts

    fun onEvent(event: StartWorkoutEvent) {
        when(event) {
            is StartWorkoutEvent.AddLoggerList -> {
                val tmp = state.loggerListStates
                tmp.add(event.loggerListState)
                state = state.copy(
                    loggerListStates = tmp
                )
            }
        }
    }
}