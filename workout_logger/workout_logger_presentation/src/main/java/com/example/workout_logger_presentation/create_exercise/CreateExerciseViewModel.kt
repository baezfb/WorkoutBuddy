package com.example.workout_logger_presentation.create_exercise

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.hbaez.core.util.UiEvent
import androidx.lifecycle.viewModelScope
import com.example.workout_logger_domain.use_case.AddExercise
import com.example.workout_logger_domain.use_case.ExerciseTrackerUseCases
import com.hbaez.core.R
import com.hbaez.core.util.UiText
import com.hbaez.user_auth_presentation.AuthViewModel
import com.hbaez.user_auth_presentation.model.ExerciseTemplate
import com.hbaez.user_auth_presentation.model.service.LogService
import com.hbaez.user_auth_presentation.model.service.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateExerciseViewModel @Inject constructor(
    private val storageService: StorageService,
    private val exerciseTrackerUseCases: ExerciseTrackerUseCases
): ViewModel() {

    var state by mutableStateOf(CreateExerciseState())
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: CreateExerciseEvent) {
        when(event) {
            is CreateExerciseEvent.OnUpdateExerciseName -> {
                state = state.copy(
                    exerciseName = event.exerciseName
                )
            }
            is CreateExerciseEvent.OnUpdateDescription -> {
                state = state.copy(
                    description = event.description
                )
            }

            is CreateExerciseEvent.OnCheckboxAdd -> {
                state = if(event.isPrimary){
                    state.copy(
                        primaryMuscles = state.primaryMuscles + event.muscle
                    )
                } else {
                    state.copy(
                        secondaryMuscles = state.secondaryMuscles + event.muscle
                    )
                }
            }
            is CreateExerciseEvent.OnCheckboxRemove -> {
                state = if(event.isPrimary){
                    val tmp = state.primaryMuscles.toMutableList()
                    tmp.remove(event.muscle)
                    state.copy(
                        primaryMuscles = tmp
                    )
                } else {
                    val tmp = state.secondaryMuscles.toMutableList()
                    tmp.remove(event.muscle)
                    state.copy(
                        secondaryMuscles = tmp
                    )
                }
            }
            is CreateExerciseEvent.OnSubmitExercise -> {
                run breaking@{
                    if(state.exerciseName.isEmpty() || state.description.isEmpty()){
                        viewModelScope.launch {
                            _uiEvent.send(
                                UiEvent.ShowSnackbar(
                                    UiText.StringResource(R.string.error_incomplete_table)
                                )
                            )
                        }
                        return@breaking
                    }
                    if(state.primaryMuscles.isEmpty()){
                        viewModelScope.launch {
                            _uiEvent.send(
                                UiEvent.ShowSnackbar(
                                    UiText.StringResource(R.string.error_no_primary_muscle)
                                )
                            )
                        }
                        return@breaking
                    }
                    viewModelScope.launch {
                        val docId = storageService.saveExerciseTemplate(
                            ExerciseTemplate(
                                name = state.exerciseName,
                                description = state.description,
                                muscle_name_main = state.primaryMuscles.joinToString(",") { it.name },
                                muscle_name_secondary = state.secondaryMuscles.joinToString(",") { it.name },
                                image_url_main = state.primaryMuscles.joinToString(",") { it.imageURL },
                                image_url_secondary = state.secondaryMuscles.joinToString(",") { it.imageURL }
                            )
                        )
                        exerciseTrackerUseCases.addExercise(
                            id = docId,
                            exerciseName = state.exerciseName,
                            description = state.description,
                            primaryMuscles = state.primaryMuscles.joinToString(",") { it.name },
                            secondaryMuscles = state.secondaryMuscles.joinToString(",") { it.name },
                            primaryURL = state.primaryMuscles.map { it.imageURL },
                            secondaryURL = state.secondaryMuscles.map { it.imageURL },
                            image_1 = null,
                            image_2 = null,
                            image_3 = null,
                            image_4 = null,
                        )
                        _uiEvent.send(UiEvent.NavigateUp)
                    }
                }
            }
        }
    }
}