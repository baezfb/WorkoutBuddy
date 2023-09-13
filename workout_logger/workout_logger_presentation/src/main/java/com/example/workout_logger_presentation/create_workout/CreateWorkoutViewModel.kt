package com.example.workout_logger_presentation.create_workout

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workout_logger_domain.model.TrackedExercise
import com.example.workout_logger_domain.use_case.CreateWorkoutUseCases
import com.example.workout_logger_domain.use_case.ExerciseTrackerUseCases
import com.example.workout_logger_presentation.search_exercise.TrackableExerciseState
import com.hbaez.core.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import com.hbaez.core.domain.preferences.Preferences
import com.hbaez.core.util.UiText
import com.hbaez.core.R
import com.hbaez.user_auth_presentation.model.WorkoutTemplate
import com.hbaez.user_auth_presentation.model.service.StorageService
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateWorkoutViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    val preferences: Preferences,
    private val createWorkoutUseCases: CreateWorkoutUseCases,
    private val storageService: StorageService,
    private val searchExerciseUseCases: ExerciseTrackerUseCases
): ViewModel() {

    var state by mutableStateOf(CreateWorkoutState())
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private var getExerciseJob: Job? = null
    private var getExerciseByNameJob: Job? = null
    private var getAllExerciseJob: Job? = null

    val workoutTemplates = storageService.workouts

    lateinit var workoutIds: List<String>
    lateinit var initWorkoutName: String
    var createWorkout: Boolean
    init {
        createWorkout = savedStateHandle["createWorkout"] ?: true
        if(!createWorkout){
            initWorkoutName = savedStateHandle["workoutName"] ?: ""
            state = state.copy(
                workoutName = initWorkoutName
            )

            workoutIds = (savedStateHandle["workoutIds"] ?: "").trim('[').trim(']').replace(" ","").split(',').toList()
            viewModelScope.launch {
                var lastusedid = -1
                val initTrackableExercises: MutableList<TrackableExerciseUiState> = emptyList<TrackableExerciseUiState>().toMutableList()
                workoutTemplates.first().onEach{
                    if(it.name == initWorkoutName){
                        val currTrackableExercise: TrackableExerciseUiState
                        if(it.lastUsedDate != null && !it.lastUsedDate.equals("null")){
                            Log.println(Log.DEBUG, "CreateWorkoutViewModel lastUsedDate", it.lastUsedDate.toString())
                            val completedWorkouts = storageService.getCompletedWorkoutByDate(it.lastUsedDate!!).toMutableList()
                            val completedWorkout = completedWorkouts.find { completed ->
                                it.exerciseName == completed.exerciseName
                            }
                            currTrackableExercise = TrackableExerciseUiState(
                                docId = it.id,
                                name = it.exerciseName,
                                sets = completedWorkout?.sets ?: it.sets,
                                reps = completedWorkout?.reps ?: it.reps,
                                rest = completedWorkout?.rest ?: it.rest,
                                weight = completedWorkout?.weight ?: it.weight,
                                id = it.rowId,
                                exercise = null,
                                position = it.position,
                                lastUsedDate = it.lastUsedDate,
                                isSuperset = it.isSuperset
                            )
                        } else{
                            currTrackableExercise = TrackableExerciseUiState(
                                docId = it.id,
                                name = it.exerciseName,
                                sets = it.sets,
                                reps = it.reps,
                                rest = it.rest,
                                weight = it.weight,
                                id = it.rowId,
                                exercise = null,
                                position = it.position,
                                lastUsedDate = it.lastUsedDate,
                                isSuperset = it.isSuperset
                            )
                        }
                        lastusedid = it.lastUsedId
                        initTrackableExercises.add(currTrackableExercise)
                    }
                }
                initTrackableExercises.sortBy { it.position }
                val initTrackableExerciseSize = initTrackableExercises.filter { !it.isSuperset }.size + (initTrackableExercises.filter { it.isSuperset }.size / 2)
                state = state.copy(
                    trackableExercises = initTrackableExercises,
                    trackableExercisesSize = initTrackableExerciseSize,
                    lastUsedId = lastusedid,
                    pageCount = initTrackableExerciseSize
                )
            }
        }
    }

    fun onEvent(event: CreateWorkoutEvent) {
        when(event) {
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

            is CreateWorkoutEvent.OnTrackableExerciseUiRepsChange -> {
                state = state.copy(
                    trackableExercises = state.trackableExercises.toList().map {
                        if(it.position == event.page && it.name == event.exercise){
                            val tmp = it.reps.toMutableList()
                            tmp[event.index] = event.reps
                            it.copy(reps = tmp.toList())
                        }
                        else{
                            it
                        }
                    }.toMutableList()
                )
            }

            is CreateWorkoutEvent.OnTrackableExerciseUiRestChange -> {
                state = state.copy(
                    trackableExercises = state.trackableExercises.toList().map {
                        if(it.position == event.page){ // update rest for all exercises in that page (if superset)
                            val tmp = it.rest.toMutableList()
                            tmp[event.index] = event.rest
                            it.copy(rest = tmp.toList())
                        }
                        else{
                            it
                        }
                    }.toMutableList()
                )
            }

            is CreateWorkoutEvent.OnTrackableExerciseUiWeightChange -> {
                state = state.copy(
                    trackableExercises = state.trackableExercises.toList().map {
                        if(it.position == event.page && it.name == event.exercise){
                            val tmp = it.weight.toMutableList()
                            tmp[event.index] = event.weight
                            it.copy(weight = tmp.toList())
                        }
                        else{
                            it
                        }
                    }.toMutableList()
                )
            }

            is CreateWorkoutEvent.OnRemoveSetRow -> {
                state = state.copy(
                    trackableExercises = state.trackableExercises.map {
                        if(it.position == event.page){
                            it.copy(
                                sets = it.sets - 1,
                                reps = it.reps.toMutableList().apply { removeAt(event.id) }.toList(),
                                rest = it.rest.toMutableList().apply { removeAt(event.id) }.toList(),
                                weight = it.weight.toMutableList().apply { removeAt(event.id) }.toList(),
                            )
                        } else {
                            it
                        }
                    }.toMutableList()
                )
            }

            is CreateWorkoutEvent.OnRemovePage -> {
                val tmpExercises = state.trackableExercises.toMutableList()
                tmpExercises.forEachIndexed { index, it ->
                    if(it.position == event.page) {
                        tmpExercises[index] = it.copy(
                            isDeleted = true
                        )
                    }
                    else if (it.position > event.page) {
                        tmpExercises[index] = it.copy(
                            position = it.position - 1
                        )
                    }
                }
                state = state.copy(
                    trackableExercises = tmpExercises.toList()
                )
                onEvent(CreateWorkoutEvent.SubtractPageCount)
            }

            is CreateWorkoutEvent.CheckTrackedExercise -> {
                val trackedExercise = preferences.loadTrackedExercise()
                if (trackedExercise.rowId != -1){
                    Log.println(Log.DEBUG, "trackedExercise row ID", trackedExercise.rowId.toString())
                    if(trackedExercise.rowId < state.pageCount){
                        var currentSets = 0
                        // update original exercise to superset status
                        var firstRest: List<String> = emptyList()
                        state = state.copy(
                            trackableExercises = state.trackableExercises.map {
                                if(it.position == trackedExercise.rowId){
                                    currentSets = it.sets
                                    firstRest = it.rest
                                    it.copy(isSuperset = true)
                                } else it
                            }
                        )
                        // add new exercise
                        state = state.copy(
                            trackableExercises = (state.trackableExercises.toList() + TrackableExerciseUiState(
                                name = trackedExercise.name,
                                id = state.lastUsedId,
                                position = trackedExercise.rowId,
                                isSuperset = true,
                                sets = currentSets,
                                reps = List(currentSets) { "" },
                                rest = firstRest,
                                weight = List(currentSets) { "" },
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
                            )).toMutableList(),
                            lastUsedId = state.lastUsedId + 1,
                            trackableExercisesSize = state.trackableExercisesSize
                        )
                    } else {
                        state = state.copy(
                            trackableExercises = (state.trackableExercises.toList() + TrackableExerciseUiState(
                                name = trackedExercise.name,
                                id = state.lastUsedId,
                                position = state.trackableExercisesSize,
                                isSuperset = false,
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
                            )).toMutableList(),
                            lastUsedId = state.lastUsedId + 1,
                            trackableExercisesSize = state.trackableExercisesSize + 1
                        )
                        onEvent(CreateWorkoutEvent.AddPageCount)
                    }
                    preferences.removeTrackedExercise()
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
                        if(!exercise.isDeleted){
                            counter += 1
                        }
                        if((exercise.name.isEmpty() || exercise.sets == 0 || exercise.reps.contains("") || exercise.rest.contains("") || exercise.weight.contains("")) && !exercise.isDeleted){
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

            is CreateWorkoutEvent.OnUpdateWorkout -> {
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
                        if(!exercise.isDeleted){
                            counter += 1
                        }
                        if((exercise.name.isEmpty() || exercise.sets == 0 || exercise.reps.contains("") || exercise.rest.contains("") || exercise.weight.contains("")) && !exercise.isDeleted){
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
                    updateWorkout(event)
                }
            }

            is CreateWorkoutEvent.AddPageCount -> {
                state = state.copy(
                    pageCount = state.pageCount + 1
                )
            }

            is CreateWorkoutEvent.SubtractPageCount -> {
                state = state.copy(
                    pageCount = state.pageCount - 1,
                    trackableExercisesSize = state.trackableExercisesSize - 1
                )
            }

            is CreateWorkoutEvent.AddSet -> {
                state = state.copy(
                    trackableExercises = state.trackableExercises.toList().map {
                        Log.println(Log.DEBUG, "CcreateWOrkoutViewModel AddSet event.page", event.page.toString())
                        Log.println(Log.DEBUG, "CcreateWOrkoutViewModel AddSet it.position", it.position.toString())
                        if(it.position == event.page){
                            it.copy(
                                sets = it.sets + 1,
                                reps = (it.reps + ""),
                                rest = (it.rest + ""),
                                weight = (it.weight + "")
                            )
                        } else {
                            it
                        }
                    }.toMutableList()
                )
            }

            is CreateWorkoutEvent.GetExerciseInfo -> {
                if(event.isSuperset){
                    var name1 = ""
                    var name2 = ""
                    state.trackableExercises.forEach {
                        if(it.position == event.position) {
                            if(name1.isEmpty()) {
                                name1 = it.name
                            } else {
                                name2 = it.name
                            }
                        }
                    }
                    getSupersetExerciseByName(name1, name2)
                }
                else {
                    getExerciseByName(state.trackableExercises[event.position].name)
                }
            }

            is CreateWorkoutEvent.GetAllExerciseInfo -> {
                state.trackableExercises.onEach { currTrackableExercise ->
                    if(currTrackableExercise.exercise == null){
                        getAllExerciseJob?.cancel()
                        getAllExerciseJob = searchExerciseUseCases
                            .getExerciseForName(currTrackableExercise.name)
                            .onEach { exercises ->
                                val currExercise = exercises.first()
                                state = state.copy(
                                    trackableExercises = state.trackableExercises.map {
                                        if(it == currTrackableExercise) {
                                            it.copy(
                                                exercise = currExercise
                                            )
                                        } else it
                                    }
                                )
                            }
                            .launchIn(viewModelScope)
                    }
                }
            }

            is CreateWorkoutEvent.OnToggleExerciseDescription -> {
                state = state.copy(
                    exerciseInfo = state.exerciseInfo.mapIndexed { index, trackableExerciseState ->
                        if(trackableExerciseState.exercise.id == event.exercise.exercise.id && index == event.pos){
                            trackableExerciseState.copy(isDescrExpanded = !trackableExerciseState.isDescrExpanded)
                        } else trackableExerciseState
                    }
                )
            }

            is CreateWorkoutEvent.DeleteRoutine -> {
                deleteRoutine()
            }
        }
    }

    private fun getExerciseByName(name: String) {
        getExerciseJob?.cancel()
        getExerciseJob = searchExerciseUseCases
            .getExerciseForName(name)
            .onEach { exercises ->
                if(exercises.isEmpty()){
                    _uiEvent.send(
                        UiEvent.ShowSnackbar(
                            UiText.StringResource(R.string.empty_results)
                        )
                    )
                }
                state = state.copy(
                    exerciseInfo = exercises.map {
                        TrackableExerciseState(exercise = it)
                    }
                )
            }
            .launchIn(viewModelScope)
    }
    private fun getSupersetExerciseByName(name1: String, name2: String) {
        state = state.copy(
            exerciseInfo = List(2) { TrackableExerciseState(exercise = TrackedExercise(name=null, exerciseBase = null))}
        )
        getExerciseJob?.cancel()
        getExerciseJob = searchExerciseUseCases
            .getExerciseForName(name1)
            .onEach { exercises ->
                if(exercises.isEmpty()){
                    _uiEvent.send(
                        UiEvent.ShowSnackbar(
                            UiText.StringResource(R.string.empty_results)
                        )
                    )
                }
                state = state.copy(
                    exerciseInfo = state.exerciseInfo.mapIndexed { index, trackableExerciseState ->
                        if(index == 0){
                            TrackableExerciseState(exercise = exercises.first())
                        } else trackableExerciseState
                    }
                )
            }
            .launchIn(viewModelScope)
        getExerciseByNameJob?.cancel()
        getExerciseByNameJob = searchExerciseUseCases
            .getExerciseForName(name2)
            .onEach { exercises ->
                if(exercises.isEmpty()){
                    _uiEvent.send(
                        UiEvent.ShowSnackbar(
                            UiText.StringResource(R.string.empty_results)
                        )
                    )
                }
                state = state.copy(
                    exerciseInfo = state.exerciseInfo.mapIndexed { index, trackableExerciseState ->
                        if(index == 1){
                            TrackableExerciseState(exercise = exercises.first())
                        } else trackableExerciseState
                    }
                )
            }
            .launchIn(viewModelScope)
    }

    private fun trackWorkout(event: CreateWorkoutEvent.OnCreateWorkout){
        viewModelScope.launch {
            event.trackableExercise.forEach {
                if(!it.isDeleted){
                    storageService.saveWorkoutTemplate(
                        WorkoutTemplate(
                            id = it.docId,
                            name = state.workoutName,
                            exerciseName = it.name,
                            exerciseId = it.exercise!!.id,
                            sets = it.sets,
                            rest = it.rest,
                            reps = it.reps,
                            weight = it.weight,
                            rowId = it.id,
                            position = it.position,
                            lastUsedId = state.lastUsedId,
                            lastUsedDate = it.lastUsedDate,
                            isSuperset = it.isSuperset
                        )
                    )
                }
            }
            _uiEvent.send(UiEvent.NavigateUp)
        }
    }
    private fun updateWorkout(event: CreateWorkoutEvent.OnUpdateWorkout){
        viewModelScope.launch {
            event.trackableExercise.forEach {
                if(!it.isDeleted){
                    if(it.docId.isEmpty()){
                        storageService.saveWorkoutTemplate(
                            WorkoutTemplate(
                                id = it.docId,
                                name = state.workoutName,
                                exerciseName = it.name,
                                exerciseId = null,
                                sets = it.sets,
                                rest = it.rest,
                                reps = it.reps,
                                weight = it.weight,
                                rowId = it.id,
                                position = it.position,
                                lastUsedId = state.lastUsedId,
                                lastUsedDate = it.lastUsedDate,
                                isSuperset = it.isSuperset
                            )
                        )
                    } else {
                        storageService.updateWorkoutTemplate(
                            WorkoutTemplate(
                                id = it.docId,
                                name = state.workoutName,
                                exerciseName = it.name,
                                exerciseId = null,
                                sets = it.sets,
                                rest = it.rest,
                                reps = it.reps,
                                weight = it.weight,
                                rowId = it.id,
                                position = it.position,
                                lastUsedId = state.lastUsedId,
                                lastUsedDate = it.lastUsedDate,
                                isSuperset = it.isSuperset
                            )
                        )
                    }
                } else if (it.docId.isNotEmpty()) {
                    storageService.deleteWorkoutTemplate(
                        WorkoutTemplate(
                            id = it.docId,
                            name = state.workoutName,
                            exerciseName = it.name,
                            exerciseId = null,
                            sets = it.sets,
                            rest = it.rest,
                            reps = it.reps,
                            weight = it.weight,
                            rowId = it.id,
                            position = it.position,
                            lastUsedId = state.lastUsedId,
                            lastUsedDate = it.lastUsedDate,
                            isSuperset = it.isSuperset
                        )
                    )
                }
            }
            _uiEvent.send(UiEvent.NavigateUp)
        }
    }

    private fun deleteRoutine() {
        viewModelScope.launch {
            state.trackableExercises.forEach {
                if(it.docId.isNotEmpty()){
                    storageService.deleteWorkoutTemplate(
                        WorkoutTemplate(
                            id = it.docId,
                            name = state.workoutName,
                            exerciseName = it.name,
                            exerciseId = null,
                            sets = it.sets,
                            rest = it.rest,
                            reps = it.reps,
                            weight = it.weight,
                            rowId = it.id,
                            position = it.id,
                            lastUsedId = state.lastUsedId,
                            lastUsedDate = null,
                            isSuperset = it.isSuperset
                        )
                    )
                }
            }
            _uiEvent.send(UiEvent.NavigateUp)
        }
    }
}
