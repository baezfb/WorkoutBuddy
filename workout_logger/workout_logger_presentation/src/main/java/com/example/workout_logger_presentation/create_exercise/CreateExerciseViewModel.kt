package com.example.workout_logger_presentation.create_exercise

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.hbaez.core.util.UiEvent
import androidx.lifecycle.viewModelScope
import com.example.workout_logger_domain.use_case.AddExercise
import com.example.workout_logger_domain.use_case.ExerciseTrackerUseCases
import com.example.workout_logger_presentation.create_exercise.model.Muscle
import com.hbaez.core.R
import com.hbaez.core.util.UiText
import com.hbaez.user_auth_presentation.AuthViewModel
import com.hbaez.user_auth_presentation.model.ExerciseTemplate
import com.hbaez.user_auth_presentation.model.service.LogService
import com.hbaez.user_auth_presentation.model.service.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateExerciseViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val storageService: StorageService,
    private val exerciseTrackerUseCases: ExerciseTrackerUseCases
): ViewModel() {

    var state by mutableStateOf(CreateExerciseState())
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private var checkExerciseName: Job? = null
    private var getExercise: Job? = null

    private var exercises = storageService.exercises

    var createExercise: Boolean
    lateinit var initExerciseName: String
    lateinit var initDescr: String
    lateinit var initPrimaryMuscle: List<Muscle?>
    lateinit var initSecondaryMuscle: List<Muscle?>
    lateinit var initImageURL: String
    init {
        createExercise = savedStateHandle["createExercise"]!!
        if(!createExercise){
            initExerciseName = savedStateHandle["exerciseName"]!!
            initDescr = savedStateHandle["description"] ?: ""
            initPrimaryMuscle = (savedStateHandle["primaryMuscles"] ?: "").split(",").map { currMuscle ->
                state.muscles.find {
                    it.name == currMuscle
                }
            }
            initSecondaryMuscle = (savedStateHandle["secondaryMuscles"] ?: "").split(",").map { currMuscle ->
                state.muscles.find {
                    it.name == currMuscle
                }
            }
            initImageURL = savedStateHandle["imageURL"]!!
            state = state.copy(
                image_URL = initImageURL
            )
            initImageURL.split(",").forEachIndexed { index, imageUrl ->
                state = when (index) {
                    0 -> state.copy(image_1 = imageUrl)
                    1 -> state.copy(image_2 = imageUrl)
                    2 -> state.copy(image_3 = imageUrl)
                    3 -> state.copy(image_4 = imageUrl)
                    else -> state // No modification for indexes beyond the specified cases
                }
            }
            Log.println(Log.DEBUG, "createexercise image", initImageURL)
            Log.println(Log.DEBUG, "createexercise image", state.image_1.toString())
            Log.println(Log.DEBUG, "createexercise image", state.image_2.toString())
            Log.println(Log.DEBUG, "createexercise image", state.image_3.toString())
            Log.println(Log.DEBUG, "createexercise image", state.image_4.toString())
            Log.println(Log.DEBUG, "createexercise image", state.image_URL)

            state = state.copy(
                exerciseName = initExerciseName,
                description = initDescr,
                primaryMuscles = initPrimaryMuscle.filterNotNull(),
                secondaryMuscles = initSecondaryMuscle.filterNotNull()
            )
        }
    }
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
            is CreateExerciseEvent.OnUpdateFilter -> {
                state = state.copy(
                    filterText = event.filter
                )
            }
            is CreateExerciseEvent.OnClearFilter -> {
                state = state.copy(
                    filterText = ""
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

                    checkExerciseName?.cancel()
                    checkExerciseName =  exerciseTrackerUseCases
                        .getExerciseForName(state.exerciseName)
                        .onEach { trackedExerciseList ->
                            if(trackedExerciseList.isNotEmpty()) {
                                _uiEvent.send(
                                    UiEvent.ShowSnackbar(
                                        UiText.StringResource(R.string.exercise_name_taken)
                                    )
                                )
                                return@onEach
                            }
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
                                secondaryURL = state.secondaryMuscles.map { it.imageURL.replace("main", "secondary") },
                                image_1 = null,
                                image_2 = null,
                                image_3 = null,
                                image_4 = null,
                            )
                            _uiEvent.send(UiEvent.NavigateUp)
                        }
                        .launchIn(viewModelScope)
                }
            }
            is CreateExerciseEvent.OnUpdateExercise -> {
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

                    checkExerciseName?.cancel()
                    checkExerciseName = exerciseTrackerUseCases
                        .getUniqueExerciseForName(state.exerciseName)
                        .onEach {
                            if(state.exerciseName != initExerciseName) {
                                _uiEvent.send(
                                    UiEvent.ShowSnackbar(
                                        UiText.StringResource(R.string.exercise_name_taken)
                                    )
                                )
                                return@onEach
                            }
                        }.launchIn(viewModelScope)

                    var counter = 0
                    getExercise?.cancel()
                    getExercise = exerciseTrackerUseCases
                        .getUniqueExerciseForName(initExerciseName)
                        .onEach { trackedExercise ->
                            if(counter > 0){
                                return@onEach
                            }
                            counter++

                            val exerciseTemplates = exercises.first { it.isNotEmpty() }
                            val currExercise = exerciseTemplates.find {
                                it.name == initExerciseName
                            }
                            Log.println(Log.DEBUG, "currentExercise 1234", currExercise?.id ?: "null")
                            if(currExercise != null){ // update on firebase
                                Log.println(Log.DEBUG, "currentExercise 1234", currExercise.id)
                                Log.println(Log.DEBUG, "state imageURL", state.image_URL)
                                Log.println(Log.DEBUG, "state imageURL", state.image_URL.split(",").toString())
                                val docId = storageService.updateExerciseTemplate(
                                    ExerciseTemplate(
                                        docId = currExercise.id,
                                        id = trackedExercise.id!!,
                                        name = state.exerciseName,
                                        description = state.description,
                                        muscle_name_main = state.primaryMuscles.joinToString(",") { it.name },
                                        muscle_name_secondary = state.secondaryMuscles.joinToString(",") { it.name },
                                        image_url_main = state.primaryMuscles.joinToString(",") { it.imageURL },
                                        image_url_secondary = state.secondaryMuscles.joinToString(",") { it.imageURL },
                                        image_url = state.image_URL
                                    )
                                )
                                exerciseTrackerUseCases.updateExercise(
                                    id = docId,
                                    exerciseName = state.exerciseName,
                                    description = state.description,
                                    primaryMuscles = state.primaryMuscles.joinToString(",") { it.name },
                                    secondaryMuscles = state.secondaryMuscles.joinToString(",") { it.name },
                                    primaryURL = state.primaryMuscles.map { it.imageURL },
                                    secondaryURL = state.secondaryMuscles.map { it.imageURL.replace("main", "secondary") },
                                    image_url = state.image_URL.split(","),
                                    image_1 = null,
                                    image_2 = null,
                                    image_3 = null,
                                    image_4 = null,
                                )
                                _uiEvent.send(UiEvent.NavigateUp)
                            }
                            else { // new on firebase
                                Log.println(Log.DEBUG, "currentExercise 1234", "reached inside else")
                                Log.println(Log.DEBUG, "state imageURL", state.image_URL)
                                val docId = storageService.saveExerciseTemplate(
                                    ExerciseTemplate(
                                        id = trackedExercise.id!!,
                                        name = state.exerciseName,
                                        description = state.description,
                                        muscle_name_main = state.primaryMuscles.joinToString(",") { it.name },
                                        muscle_name_secondary = state.secondaryMuscles.joinToString(",") { it.name },
                                        image_url_main = state.primaryMuscles.joinToString(",") { it.imageURL },
                                        image_url_secondary = state.secondaryMuscles.joinToString(",") { it.imageURL },
                                        image_url = state.image_URL
                                    )
                                )
                                exerciseTrackerUseCases.updateExercise(
                                    id = docId,
                                    exerciseName = state.exerciseName,
                                    description = state.description,
                                    primaryMuscles = state.primaryMuscles.joinToString(",") { it.name },
                                    secondaryMuscles = state.secondaryMuscles.joinToString(",") { it.name },
                                    primaryURL = state.primaryMuscles.map { it.imageURL },
                                    secondaryURL = state.secondaryMuscles.map { it.imageURL.replace("main", "secondary") },
                                    image_url = state.image_URL.split(","),
                                    image_1 = null,
                                    image_2 = null,
                                    image_3 = null,
                                    image_4 = null,
                                )
                                _uiEvent.send(UiEvent.NavigateUp)
                            }
                        }.launchIn(viewModelScope)
                }
            }
        }
    }
}