package com.example.workout_logger_presentation.search_exercise

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workout_logger_domain.use_case.ExerciseTrackerUseCases
import com.hbaez.core.util.UiEvent
import com.hbaez.core.util.UiText
import com.hbaez.core.R
import com.hbaez.core.domain.model.TrackedExercise
import com.hbaez.core.domain.preferences.Preferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchExerciseViewModel @Inject constructor(
    private val preferences: Preferences,
    private val searchExerciseUseCases: ExerciseTrackerUseCases
): ViewModel() {
    var state by mutableStateOf(SearchExerciseState())
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private var getExerciseJob: Job? = null

    fun onEvent(event: SearchExerciseEvent) {
        when(event) {
            is SearchExerciseEvent.OnExerciseNameChange -> {
                state = state.copy(
                    query = event.name
                )
            }

            is SearchExerciseEvent.OnExerciseNameFocusChange -> {
                state = state.copy(
                    isHintVisible = !event.isFocused && state.query.isBlank()
                )
            }

            is SearchExerciseEvent.OnSearch -> {
                executeSearch()
            }

            is SearchExerciseEvent.OnToggleTrackableExercise -> {
                state = state.copy(
                    trackableExercise = state.trackableExercise.map {
                        if(it.exercise.id == event.exercise.exercise.id){
                            it.copy(isExpanded = !it.isExpanded)
                        } else it
                    }
                )
            }

            is SearchExerciseEvent.OnToggleTrackableExerciseDescription -> {
                state = state.copy(
                    trackableExercise = state.trackableExercise.map {
                        if(it.exercise.id == event.exercise.exercise.id){
                            it.copy(isDescrExpanded = !it.isDescrExpanded)
                        } else it
                    }
                )
            }

            is SearchExerciseEvent.OnTrackExercise -> {
                trackExercise(event.exercise, event.rowId)
            }
        }
    }

    private fun executeSearch() {
        state = state.copy(
            query = state.query.trim()
        )
        getExerciseJob?.cancel()
        getExerciseJob = searchExerciseUseCases
            .getExerciseForName(state.query)
            .onEach { exercises ->
                if(exercises.isEmpty()){
                    _uiEvent.send(
                        UiEvent.ShowSnackbar(
                            UiText.StringResource(R.string.empty_results)
                        )
                    )
                }
                state = state.copy(
                    trackableExercise = exercises.map {
                        TrackableExerciseState(exercise = it)
                    },
                    isSearching = false
                )
            }
            .launchIn(viewModelScope)

    }

    private fun trackExercise(exercise: TrackableExerciseState, rowId: Int){
        viewModelScope.launch {
            preferences.newTrackedExercise(
                TrackedExercise(
                    rowId = rowId,
                    id = exercise.exercise.id,
                    name = exercise.exercise.name!!,
                    exerciseBase = exercise.exercise.exerciseBase!!,
                    description = exercise.exercise.description,
                    muscles = exercise.exercise.muscles,
                    muscles_secondary = exercise.exercise.muscles_secondary,
                    equipment = exercise.exercise.equipment,
                    image_url = exercise.exercise.image_url.toSet(),
                    is_main = exercise.exercise.is_main,
                    is_front = exercise.exercise.is_front,
                    muscle_name_main = exercise.exercise.muscle_name_main,
                    image_url_main = exercise.exercise.image_url_main.toSet(),
                    image_url_secondary = exercise.exercise.image_url_secondary.toSet(),
                    muscle_name_secondary = exercise.exercise.muscle_name_secondary
                )
            )
            _uiEvent.send(UiEvent.NavigateUp)
        }
    }
}