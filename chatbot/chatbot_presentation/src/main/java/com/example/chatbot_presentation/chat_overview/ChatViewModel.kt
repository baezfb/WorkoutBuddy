package com.example.chatbot_presentation.chat_overview

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewModelScope
import com.hbaez.core.util.UiEvent
import com.hbaez.user_auth_presentation.AuthViewModel
import com.hbaez.user_auth_presentation.model.service.LogService
import com.hbaez.user_auth_presentation.model.service.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val storageService: StorageService,
    logService: LogService
): AuthViewModel(logService) {
    var routineFormState by mutableStateOf(RoutineFormState())
        private set

//    var state by mutableStateOf(ChatState())
//        private set

    var workoutNames: List<String> = emptyList()
    private val _uiEvent = Channel<UiEvent>()

    val uiEvent = _uiEvent.receiveAsFlow()
    init {
        viewModelScope.launch {
            workoutNames = storageService.workouts.first().map {
                it.name
            }.distinct()
        }
    }

    fun onEvent(event: ChatEvent) {
        when(event) {
            is ChatEvent.OnCheckboxFormAdd -> {
                routineFormState = if(event.isPrimary) {
                    routineFormState.copy(
                        primaryMuscles = routineFormState.primaryMuscles + event.muscle
                    )
                } else {
                    routineFormState.copy(
                        secondaryMuscles = routineFormState.secondaryMuscles + event.muscle
                    )
                }
            }
            is ChatEvent.OnCheckboxFormRemove -> {
                routineFormState = if(event.isPrimary){
                    val tmp = routineFormState.primaryMuscles.toMutableList()
                    tmp.remove(event.muscle)
                    routineFormState.copy(
                        primaryMuscles = tmp
                    )
                } else {
                    val tmp = routineFormState.secondaryMuscles.toMutableList()
                    tmp.remove(event.muscle)
                    routineFormState.copy(
                        secondaryMuscles = tmp
                    )
                }
            }
            is ChatEvent.OnGoalTypeSelect -> {
                routineFormState = routineFormState.copy(
                    weightGoalType = event.goalType
                )
            }
            is ChatEvent.OnTimeLimitSelect -> {
                routineFormState = routineFormState.copy(
                    timeLimit = event.timeLimit
                )
            }
        }
    }
}