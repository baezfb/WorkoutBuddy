package com.example.workout_logger_presentation.create_workout

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workout_logger_domain.model.TrackedExercise
import com.example.workout_logger_domain.use_case.CreateWorkoutUseCases
import com.hbaez.core.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import com.hbaez.core.domain.preferences.Preferences
import com.hbaez.core.util.UiText
import com.hbaez.core.R
import com.hbaez.user_auth_presentation.model.WorkoutTemplate
import com.hbaez.user_auth_presentation.model.service.StorageService
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateWorkoutViewModel @Inject constructor(
    val preferences: Preferences,
    private val createWorkoutUseCases: CreateWorkoutUseCases,
    private val storageService: StorageService
): ViewModel() {

    var state by mutableStateOf(CreateWorkoutState())
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: CreateWorkoutEvent) {
        when(event) {
            is CreateWorkoutEvent.OnAddExercise -> {
                addExercise()
            }
            is CreateWorkoutEvent.OnWorkoutNameChange -> {
                state = state.copy(
                    workoutName = if(event.name.trim().isNotEmpty() || event.name.isEmpty()){
                        event.name
                    } else state.workoutName
                )
            }

            is CreateWorkoutEvent.OnWorkoutNameFocusChange -> {
                state = state.copy(
                    isHintVisible = !event.isFocused && state.workoutName.isBlank()
                )
            }

            is CreateWorkoutEvent.OnTrackableExerciseUiNameChange -> {
                state = state.copy(
                    trackableExercises = state.trackableExercises.toList().map {
                        if (it.id == event.trackableExerciseUiState.id) {
                            if(event.name.trim().isNotEmpty() || event.name.isEmpty()){
                                it.copy(name = event.name)
                            } else it
                        } else it
                    }.toMutableList()
                )
            }

            is CreateWorkoutEvent.OnTrackableExerciseUiSetsChange -> {
//                state = state.copy(
//                    trackableExercises = state.trackableExercises.toList().map {
//                        if (it.id == event.trackableExerciseUiState.id) {
//                            if(event.sets.toIntOrNull() != 0 || event.sets.isEmpty()){
//                                it.copy(sets = event.sets)
//                            } else it.copy(sets = "")
//                        } else it
//                    }.toMutableList()
//                )
            }

            is CreateWorkoutEvent.OnTrackableExerciseUiRepsChange -> {
                state = state.copy(
                    trackableExercises = state.trackableExercises.toList().map {
                        if (it.id == event.trackableExerciseUiStateId) {
                            val tmp = it.reps.toMutableList()
                            tmp[event.index] = event.reps
                            it.copy(reps = tmp.toList())
                        } else it
                    }.toMutableList()
                )
            }

            is CreateWorkoutEvent.OnTrackableExerciseUiRestChange -> {
                state = state.copy(
                    trackableExercises = state.trackableExercises.toList().map {
                        if (it.id == event.trackableExerciseUiStateId) {
                            val tmp = it.rest.toMutableList()
                            tmp[event.index] = event.rest
                            it.copy(rest = tmp.toList())
                        } else it
                    }.toMutableList()
                )
            }

            is CreateWorkoutEvent.OnTrackableExerciseUiWeightChange -> {
                state = state.copy(
                    trackableExercises = state.trackableExercises.toList().map {
                        if (it.id == event.trackableExerciseUiStateId) {
                            val tmp = it.weight.toMutableList()
                            tmp[event.index] = event.weight
                            it.copy(weight = tmp.toList())
                        } else it
                    }.toMutableList()
                )
            }

            is CreateWorkoutEvent.OnRemoveTableRow -> {
                Log.println(Log.DEBUG, "onExpand", event.id.toString())
//                state = state.copy(
//                    trackableExercises = state.trackableExercises.toList().map {
//                        if(it.exercise!!.id!! == event.exerciseId) {
//                            val newList = it.isDeleted
//                            newList[event.id] = true
//                            it.copy(isDeleted = newList.toMutableList())
//                        } else it
//                    }.toMutableList()
//                )
                var counter = 0
                state = state.copy(
                    trackableExercises = state.trackableExercises.toList().map {
                        if(counter == event.exerciseId){
                            it.copy(
                                sets = it.sets - 1,
                                reps = it.reps.toMutableList().apply { removeAt(event.id) }.toList(),
                                rest = it.rest.toMutableList().apply { removeAt(event.id) }.toList(),
                                weight = it.weight.toMutableList().apply { removeAt(event.id) }.toList(),
                                isDeleted = it.isDeleted.toMutableList().apply { removeAt(event.id) }.toList(),
//                                reps = (it.reps.toList() + "").toMutableList(),
//                                rest = (it.rest.toList() + "").toMutableList(),
//                                weight = (it.weight.toList() + "").toMutableList(),
//                                isDeleted = (it.isDeleted.toList() + false).toMutableList()
                            )
                        } else {
                            counter++
                            it
                        }
                    }.toMutableList()
                )
                Log.println(Log.DEBUG, "viewmodel trackablelist", state.trackableExercises[0].isDeleted.toString())
            }

            is CreateWorkoutEvent.CheckTrackedExercise -> {
                val trackedExercise = preferences.loadTrackedExercise()
                Log.println(Log.DEBUG, "create workout init", trackedExercise.name)
                if (trackedExercise.rowId != -1){
                    state = state.copy(
                        trackableExercises = (state.trackableExercises.toList() + TrackableExerciseUiState(
                                name = trackedExercise.name,
                                exercise = TrackedExercise(
                                    id = trackedExercise.id,
                                    name = trackedExercise.name,
                                    exerciseBase = trackedExercise.exerciseBase,
                                    description = trackedExercise.description,
                                    muscles = trackedExercise.muscles,
                                    muscles_secondary = trackedExercise.muscles_secondary,
                                    equipment = trackedExercise.equipment,
                                    image_url = trackedExercise.image_url.toList(),
                                    is_main = trackedExercise.is_main,
                                    is_front = trackedExercise.is_front,
                                    muscle_name_main = trackedExercise.muscle_name_main,
                                    image_url_main = trackedExercise.image_url_main.toList(),
                                    image_url_secondary = trackedExercise.image_url_secondary.toList(),
                                    muscle_name_secondary = trackedExercise.muscle_name_secondary
                                )
                            )).toMutableList()
//                        trackableExercises = state.trackableExercises.toList().map {
//                            if(it.id == trackedExercise.rowId){
//                                it.copy(
//                                    name = trackedExercise.name,
//                                    exercise = TrackedExercise(
//                                        id = trackedExercise.id,
//                                        name = trackedExercise.name,
//                                        exerciseBase = trackedExercise.exerciseBase,
//                                        description = trackedExercise.description,
//                                        muscles = trackedExercise.muscles,
//                                        muscles_secondary = trackedExercise.muscles_secondary,
//                                        equipment = trackedExercise.equipment,
//                                        image_url = trackedExercise.image_url.toList(),
//                                        is_main = trackedExercise.is_main,
//                                        is_front = trackedExercise.is_front,
//                                        muscle_name_main = trackedExercise.muscle_name_main,
//                                        image_url_main = trackedExercise.image_url_main.toList(),
//                                        image_url_secondary = trackedExercise.image_url_secondary.toList(),
//                                        muscle_name_secondary = trackedExercise.muscle_name_secondary
//                                    )
//                                )
//                            } else it
//                        }.toMutableList()
                    )
                    preferences.removeTrackedExercise()
                    onEvent(CreateWorkoutEvent.AddPageCount)
                    Log.println(Log.DEBUG, "create workout init after", state.trackableExercises.size.toString())
                }
            }

            is CreateWorkoutEvent.OnCreateWorkout -> {
                run breaking@{
                    if(event.workoutName.isEmpty()){
                        viewModelScope.launch {
                            _uiEvent.send(
                                UiEvent.ShowSnackbar(
                                    UiText.StringResource(R.string.error_incomplete_table)
                                )
                            )
                        }
                        return@breaking
                    }
                    var counter = 0
                    event.trackableExercise.forEach { exercise ->
                        if(!exercise.isDeleted[0]){ /*TODO: fix index*/
                            counter += 1
                            if(exercise.name.isEmpty() || exercise.sets == 0 || exercise.reps.isEmpty() || exercise.rest.isEmpty() || exercise.weight.isEmpty()){
                                Log.println(Log.DEBUG, "exercise sets", "reached inside if")
                                viewModelScope.launch {
                                    _uiEvent.send(
                                        UiEvent.ShowSnackbar(
                                            UiText.StringResource(R.string.error_incomplete_table)
                                        )
                                    )
                                }
                                return@breaking
                            }
                        }
                    }
                    if(counter == 0) {
                        viewModelScope.launch {
                            _uiEvent.send(
                                UiEvent.ShowSnackbar(
                                    UiText.StringResource(R.string.error_no_rows)
                                )
                            )
                        }
                        return@breaking
                    }
                    trackWorkout(event)
                }
            }

            is CreateWorkoutEvent.AddPageCount -> {
                state = state.copy(
                    pageCount = state.pageCount + 1
                )
            }

            is CreateWorkoutEvent.SubtractPageCount -> {
                state = state.copy(
                    pageCount = state.pageCount - 1
                )
            }

            is CreateWorkoutEvent.AddSet -> {
                var counter = 0
                state = state.copy(
                    trackableExercises = state.trackableExercises.toList().map {
                        if(counter == event.page){
                            it.copy(
                                sets = it.sets + 1,
                                reps = (it.reps + ""),
                                rest = (it.rest + ""),
                                weight = (it.weight + ""),
                                isDeleted = (it.isDeleted + false)
                            )
                        } else {
                            counter++
                            it
                        }
                    }.toMutableList()
                )
            }
        }
    }

    private fun addExercise(){
        state = state.copy(
            trackableExercises = (state.trackableExercises.toList() + TrackableExerciseUiState(id = state.lastUsedId + 1, exercise = null)).toMutableList(),
            lastUsedId = state.lastUsedId + 1
        )
    }

    private fun trackWorkout(event: CreateWorkoutEvent.OnCreateWorkout){
//        viewModelScope.launch {
//            event.trackableExercise.forEach {
//                if(it.isDeleted) return@forEach
//                createWorkoutUseCases.addWorkout(
//                    workoutName = event.workoutName,
//                    exerciseName = it.name,
//                    exerciseId = it.id,
//                    sets = it.sets.toInt(),
//                    rest = it.rest.toInt(),
//                    reps = it.reps.toInt(),
//                    weight = it.weight.toInt(),
//                    rowId = it.id,
//                    lastUsedId = event.lastUsedId
//                )
//                storageService.saveWorkoutTemplate(WorkoutTemplate(
//                    name = event.workoutName,
//                    exerciseName = it.name,
//                    exerciseId = it.id,
//                    sets = it.sets.toInt(),
//                    rest = it.rest.toInt(),
//                    reps = it.reps.toInt(),
//                    weight = it.weight.toInt(),
//                    rowId = it.id,
//                    lastUsedId = event.lastUsedId,
//                ))
//            }
//            _uiEvent.send(UiEvent.NavigateUp)
//        }
    }
}
