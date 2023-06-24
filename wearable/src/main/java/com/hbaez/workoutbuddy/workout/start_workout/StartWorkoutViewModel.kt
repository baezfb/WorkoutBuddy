package com.hbaez.workoutbuddy.workout.start_workout

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.hbaez.core.util.UiEvent
import com.hbaez.user_auth_presentation.model.service.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class StartWorkoutViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val storageService: StorageService
): ViewModel() {

    var state by mutableStateOf(StartWorkoutState())
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    val workoutTemplates = storageService.workouts

    private val workoutId: Int
    var workoutIds: List<String>

    init {
        workoutId = savedStateHandle["workoutId"] ?: -1
        workoutIds = (savedStateHandle["workoutIds"] ?: "").trim('[').trim(']').replace(" ","").split(',').toList()
        Log.println(Log.DEBUG, "workoutids viewmodel", workoutIds.toString())
    }

    fun onEvent(event: StartWorkoutEvent) {
        when(event) {
            is StartWorkoutEvent.AddLoggerList -> {
                val tmp = state.loggerListStates
                tmp.add(event.loggerListState)
                state = state.copy(
                    loggerListStates = tmp
                )
            }

            is StartWorkoutEvent.OnRepIncrease -> {
                var counter = 0
                state = state.copy(
                    loggerListStates = state.loggerListStates.map {
                        if(counter == event.page){
                            counter++
                            val tmp = it.reps.toMutableList()
                            tmp[event.index] = (it.reps[event.index].toInt() + 1).toString()
                            it.copy(reps = tmp.toList())
                        }
                        else {
                            counter++
                            it
                        }
                    }.toMutableList()
                )
            }

            is StartWorkoutEvent.OnRepDecrease -> {
                var counter = 0
                state = state.copy(
                    loggerListStates = state.loggerListStates.map {
                        if(counter == event.page){
                            counter++
                            val tmp = it.reps.toMutableList()
                            tmp[event.index] = (it.reps[event.index].toInt() - 1).coerceIn(0,null).toString()
                            it.copy(reps = tmp.toList())
                        }
                        else {
                            counter++
                            it
                        }
                    }.toMutableList()
                )
            }

            is StartWorkoutEvent.OnWeightIncrease -> {
                var counter = 0
                state = state.copy(
                    loggerListStates = state.loggerListStates.map {
                        if(counter == event.page){
                            counter++
                            val tmp = it.weight.toMutableList()
                            tmp[event.index] = (it.weight[event.index].toInt() + 1).toString()
                            it.copy(weight = tmp.toList())
                        }
                        else {
                            counter++
                            it
                        }
                    }.toMutableList()
                )
            }

            is StartWorkoutEvent.OnWeightDecrease -> {
                var counter = 0
                state = state.copy(
                    loggerListStates = state.loggerListStates.map {
                        if(counter == event.page){
                            counter++
                            val tmp = it.weight.toMutableList()
                            tmp[event.index] = (it.weight[event.index].toInt() - 1).coerceIn(0,null).toString()
                            it.copy(weight = tmp.toList())
                        }
                        else {
                            counter++
                            it
                        }
                    }.toMutableList()
                )
            }

            is StartWorkoutEvent.OnSetIncrease -> {
                var counter = 0
                state = state.copy(
                    loggerListStates = state.loggerListStates.map {
                        if(counter == event.page){
                            counter++
                            it.copy(currentSet = it.currentSet + 1)
                        }
                        else {
                            counter++
                            it
                        }
                    }.toMutableList()
                )
            }
        }
    }
}