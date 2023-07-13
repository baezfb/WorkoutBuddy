package com.example.workout_logger_presentation.start_exercise

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import com.hbaez.core.domain.preferences.Preferences
import com.hbaez.core.util.UiEvent
import com.hbaez.user_auth_presentation.AuthViewModel
import com.hbaez.user_auth_presentation.model.service.LogService
import com.hbaez.user_auth_presentation.model.service.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class StartExerciseViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val preferences: Preferences,
    private val storageService: StorageService,
    logService: LogService
): AuthViewModel(logService) {

    var state by mutableStateOf(StartExerciseState())
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        state = state.copy(
            exerciseName = savedStateHandle["exerciseName"] ?: ""
        )
    }
}