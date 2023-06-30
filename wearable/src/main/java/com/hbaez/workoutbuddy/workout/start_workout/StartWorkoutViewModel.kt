package com.hbaez.workoutbuddy.workout.start_workout

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hbaez.core.util.UiEvent
import com.hbaez.user_auth_presentation.model.CompletedWorkout
import com.hbaez.user_auth_presentation.model.service.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
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

            is StartWorkoutEvent.OnSubmitWorkout -> {
                run breaking@{
                    var hasLogged = false
                    event.trackableExercises.forEach {
                        if(it.currentSet > 0){
                            hasLogged = !hasLogged
                            return@forEach
                        }
                    }
                    if(!hasLogged){
                        return@breaking
                    }
                    var counter = 0
                    event.trackableExercises.forEach{// for each exercise
                        val repsList = mutableListOf<String>()
                        val weightList = mutableListOf<String>()
                        val isCompletedList = mutableListOf<String>()

                        // completed sets
                        for (i in 0 until it.currentSet){
                            isCompletedList.add("true")
                            if(it.reps[i].isEmpty()){
                                repsList.add(event.workoutTemplates[counter].reps[i])
                            } else repsList.add(it.reps[i])

                            if(it.weight[i].isEmpty()){
                                weightList.add(event.workoutTemplates[counter].weight[i])
                            } else weightList.add(it.weight[i])
                        }
                        // skipped sets
                        for (i in it.currentSet until it.reps.size){
                            isCompletedList.add("false")
                            if(it.reps[i].isEmpty()){
                                repsList.add(event.workoutTemplates[counter].reps[i])
                            } else repsList.add(it.reps[i])
                            if(it.weight[i].isEmpty()){
                                weightList.add(event.workoutTemplates[counter].weight[i])
                            } else weightList.add(it.weight[i])
                        }

                        if(repsList.isNotEmpty() && weightList.isNotEmpty()){
                            trackCompletedWorkout(it, repsList, weightList, isCompletedList.toList(), event.dayOfMonth, event.month, event.year)
                        }
                        counter++
                    }
                    viewModelScope.launch {
                        _uiEvent.send(UiEvent.NavigateUp)
                    }
                }
            }
        }
    }

    private fun trackCompletedWorkout(loggerListState: LoggerListState, repsList: List<String>, weightList: List<String>, isCompletedList: List<String>, dayOfMonth: Int, month: Int, year: Int){
        viewModelScope.launch {
            val date = "${year}-${month.toString().padStart(2,'0')}-${dayOfMonth.toString().padStart(2, '0')}"
            storageService.saveCompletedWorkout(
                CompletedWorkout(
                    workoutName = state.workoutName,
                    workoutId = workoutId,
                    exerciseName = loggerListState.exerciseName,
                    exerciseId = loggerListState.exerciseId,
                    sets = loggerListState.sets.toInt(),
                    rest = loggerListState.rest,
                    reps = repsList,
                    weight = weightList,
                    isCompleted = isCompletedList,
                    dayOfMonth = dayOfMonth,
                    month = month,
                    year = year
                ),
                date = date
            )
        }
    }
}