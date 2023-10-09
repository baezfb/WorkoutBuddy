package com.example.workout_logger_presentation.workout_logger_overview

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workout_logger_domain.use_case.ExerciseTrackerUseCases
import com.example.workout_logger_presentation.search_exercise.TrackableExerciseState
import com.example.workout_logger_presentation.workout_logger_overview.components.EditableWorkoutItemState
import com.hbaez.core.R
import com.hbaez.core.domain.preferences.Preferences
import com.hbaez.core.util.UiEvent
import com.hbaez.core.util.UiText
import com.hbaez.user_auth_presentation.model.CalendarDates
import com.hbaez.user_auth_presentation.model.CompletedWorkout
import com.hbaez.user_auth_presentation.model.WorkoutTemplate
import com.hbaez.user_auth_presentation.model.service.StorageService
import com.himanshoe.kalendar.KalendarEvent
import com.himanshoe.kalendar.KalendarEvents
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import java.time.LocalDate

@HiltViewModel
class WorkoutLoggerOverviewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    preferences: Preferences,
    private val exerciseTrackerUseCases: ExerciseTrackerUseCases,
    private val storageService: StorageService
): ViewModel() {

    var state by mutableStateOf(WorkoutLoggerOverviewState())
        private set

    var completedWorkouts: MutableList<CompletedWorkout> = mutableListOf()
    var imageUrls = HashMap<String, String>()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private var getWorkoutsForDateJob: Job? = null
    private var getExerciseJob: Job? = null
    private var getAllExercisesJob: Job? = null
    private var getWorkoutNames: Job? = null

    val workoutTemplates = storageService.workouts
    val trackedExercises = storageService.exercises
    val calendarEvents = storageService.calendarDates

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun swipeRefreshWorkouts() {
        viewModelScope.launch {
            _isLoading.value = true
            imageUrls.clear()
            refreshWorkouts()
            delay(2000L)
            _isLoading.value = false
        }
    }
    init {
        val date = savedStateHandle["date"] ?: ""
        savedStateHandle.remove<String>("date")
        Log.println(Log.DEBUG,"overview savedstate date", date)
        if(date.isNotEmpty()){
            state = state.copy(
                date = LocalDate.parse(date)
            )
        }
        refreshWorkouts()
        refreshExercises()
        executeSearch()
    }
    fun onEvent(event: WorkoutLoggerOverviewEvent) {
        when(event) {
            is WorkoutLoggerOverviewEvent.OnNextDayClick -> {
                state = state.copy(
                    date = state.date.plusDays(1)
                )
                imageUrls.clear()
                refreshWorkouts()
            }
            is WorkoutLoggerOverviewEvent.OnPreviousDayClick -> {
                state = state.copy(
                    date = state.date.minusDays(1)
                )
                imageUrls.clear()
                refreshWorkouts()
            }
            is WorkoutLoggerOverviewEvent.OnStartWorkoutClick -> {
                getWorkoutNames?.cancel()
                val workoutNames = mutableListOf<String>()
                val workoutId = mutableListOf<Int>()
                getWorkoutNames = exerciseTrackerUseCases.getWorkouts().onEach {
                    it.onEach { trackedWorkout ->
                        state = state.copy(
                            workoutNames = (workoutNames + trackedWorkout.name).toMutableList(),
                            workoutId = (workoutId + trackedWorkout.workoutId!!).toMutableList()
                        )
                        workoutNames.add(trackedWorkout.name)
                        workoutId.add(trackedWorkout.workoutId!!)
                    }
                }.launchIn(viewModelScope)
            }
            is WorkoutLoggerOverviewEvent.OnCompletedWorkoutClick -> {
                val tmp = state.completedWorkoutIsExpanded.toMutableList()
                tmp[event.index] = !tmp[event.index]
                state = state.copy(
                    completedWorkoutIsExpanded = tmp
                )
            }
            is WorkoutLoggerOverviewEvent.OnExerciseSearch -> {
                state = state.copy(
                    exerciseFilterText = event.filterText
                )
                executeSearch()
            }
            is WorkoutLoggerOverviewEvent.OnExerciseItemClick -> {
                state = state.copy(
                    trackableExercise = state.trackableExercise.map {
                        if(it == event.trackableExerciseState){
                            it.copy(isExpanded = !it.isExpanded)
                        } else it
                    }
                )
            }
            is WorkoutLoggerOverviewEvent.OnExerciseDescrClick -> {
                state = state.copy(
                    trackableExercise = state.trackableExercise.map {
                        if(it == event.trackableExerciseState){
                            it.copy(isDescrExpanded = !it.isDescrExpanded)
                        } else it
                    }
                )
            }
            is WorkoutLoggerOverviewEvent.OnChooseExercise -> {
                /*TODO*/
            }
            is WorkoutLoggerOverviewEvent.OnDateClick -> {
                state = state.copy(
                    date = LocalDate.of(event.year, event.month, event.dayOfMonth)
                )
                imageUrls.clear()
                refreshWorkouts()
            }

            is WorkoutLoggerOverviewEvent.OnDeleteCompletedWorkout -> {
                viewModelScope.launch {
                    val deleteDate = storageService.deleteCompletedWorkout(
                        event.completedWorkout,
                        state.date.toString()
                    )
                    Log.println(Log.DEBUG, "delete date value", deleteDate.toString())
                    if(deleteDate){
                        val calendarDates = storageService.calendarDates.first().calendarDates.toMutableList()
                        Log.println(Log.DEBUG, "calendarDates today", state.date.toString())
                        calendarDates.remove(state.date.toString())
                        Log.println(Log.DEBUG, "calendarDates", calendarDates.toString())
                        storageService.saveCalendarDate(CalendarDates(calendarDates.toList()))
                    }
                    imageUrls.clear()
                    refreshWorkouts()
                }
            }

            is WorkoutLoggerOverviewEvent.OnEditWorkoutItem -> {
                state = state.copy(
                    editableWorkoutItemState = EditableWorkoutItemState(
                        origReps = completedWorkouts[event.index].reps,
                        origWeights = completedWorkouts[event.index].weight,
                        origIsCompleted = completedWorkouts[event.index].isCompleted,
                        reps = completedWorkouts[event.index].reps,
                        weight = completedWorkouts[event.index].weight,
                        isCompleted = completedWorkouts[event.index].isCompleted
                    )
                )
            }
            is WorkoutLoggerOverviewEvent.OnEditWorkoutItemCompleted -> {
                val tmpCompleted = state.editableWorkoutItemState.isCompleted.toMutableList()
                tmpCompleted[event.row] = event.newValue.toString()
                state = state.copy(
                    editableWorkoutItemState = state.editableWorkoutItemState.copy(
                        isCompleted = tmpCompleted
                    )
                )
            }
            is WorkoutLoggerOverviewEvent.OnEditWorkoutItemReps -> {
                val tmpReps = state.editableWorkoutItemState.reps.toMutableList()
                tmpReps[event.row] = event.newValue
                state = state.copy(
                    editableWorkoutItemState = state.editableWorkoutItemState.copy(
                        reps = tmpReps
                    )
                )
            }
            is WorkoutLoggerOverviewEvent.OnEditWorkoutItemWeight -> {
                val tmpWeight = state.editableWorkoutItemState.weight.toMutableList()
                tmpWeight[event.row] = event.newValue
                state = state.copy(
                    editableWorkoutItemState = state.editableWorkoutItemState.copy(
                        weight = tmpWeight
                    )
                )
            }

            is WorkoutLoggerOverviewEvent.OnEditWorkoutItemUpdate -> {
                updateCompletedWorkoutItem(
                    reps = state.editableWorkoutItemState.reps,
                    weight = state.editableWorkoutItemState.weight,
                    isCompleted = state.editableWorkoutItemState.isCompleted,
                    index = event.index,
                    dayOfMonth = state.date.dayOfMonth,
                    month = state.date.monthValue,
                    year = state.date.year
                )
            }
        }
    }

    private fun updateCompletedWorkoutItem(reps: List<String>, weight: List<String>, isCompleted: List<String>, index: Int, dayOfMonth: Int, month: Int, year: Int){
        val date = "${year}-${month.toString().padStart(2,'0')}-${dayOfMonth.toString().padStart(2, '0')}"
        viewModelScope.launch {
            storageService.updateCompletedWorkout(
                CompletedWorkout(
                    docId = completedWorkouts[index].docId,
                    workoutName = completedWorkouts[index].workoutName,
                    workoutId = completedWorkouts[index].workoutId,
                    exerciseName = completedWorkouts[index].exerciseName,
                    exerciseId = null,
                    sets = completedWorkouts[index].sets,
                    rest = completedWorkouts[index].rest,
                    reps = reps,
                    weight = weight,
                    isCompleted = isCompleted,
                    dayOfMonth = dayOfMonth,
                    month = month,
                    year = year
                ),
                date = date
            )
            refreshWorkouts()
        }
    }

    private fun refreshWorkouts(){
        viewModelScope.launch {
            Log.println(Log.DEBUG, "current date", state.date.toString())
            completedWorkouts = storageService.getCompletedWorkoutByDate(state.date.toString()).toMutableList()
            completedWorkouts.forEach {
                coroutineScope {
                    getExerciseByName(it.exerciseName)
                }
            }
            delay(150L)
            state = state.copy(
                completedWorkoutIsExpanded = MutableList(completedWorkouts.size) { false }
            )
        }
    }

    private fun refreshExercises(){
        viewModelScope.launch {
            trackedExercises.collect{ list ->
                var counter = 0
                getExerciseJob?.cancel()
                getExerciseJob = exerciseTrackerUseCases
                    .getExerciseForName("")
                    .onEach { roomTrackedExercises ->
                        if(counter > 0){
                            return@onEach
                        }
                        counter++
                        list.onEach {
                            /*TODO: check if firestore exercise exists in room db and use insert/update accordingly*/
                            Log.println(Log.DEBUG, "tracked exercises ID", it.id)
                            if (roomTrackedExercises.find { elem -> elem.name == it.name } != null){ // element is in room db
                                exerciseTrackerUseCases.updateExercise(
                                    id = it.id,
                                    exerciseName = it.name,
                                    description = it.description,
                                    primaryMuscles = it.muscle_name_main,
                                    secondaryMuscles = it.muscle_name_secondary,
                                    primaryURL = it.image_url_main.split(",").map { url -> url.trim() },
                                    secondaryURL = it.image_url_secondary.split(",").map { url -> url.trim() },
                                    image_url = it.image_url?.split(",") ?: emptyList(),
                                    image_1 = null,
                                    image_2 = null,
                                    image_3 = null,
                                    image_4 = null,
                                )
                            }
                            else { // element is new to room db
                                exerciseTrackerUseCases.addExercise(
                                    id = it.id,
                                    exerciseName = it.name,
                                    description = it.description,
                                    primaryMuscles = it.muscle_name_main,
                                    secondaryMuscles = it.muscle_name_secondary,
                                    primaryURL = it.image_url_main.split(",").map { url -> url.trim() },
                                    secondaryURL = it.image_url_secondary.split(",").map { url -> url.trim() },
                                    image_url = it.image_url?.split(",") ?: emptyList(),
                                    image_1 = null,
                                    image_2 = null,
                                    image_3 = null,
                                    image_4 = null,
                                )
                            }
                        }
                    }.launchIn(viewModelScope)
            }
        }
    }

    private suspend fun getExerciseByName(name: String) {
//        getExerciseJob?.cancel()
        exerciseTrackerUseCases
            .getUniqueExerciseForName(name)
            .first()
            .let { exercise ->
                imageUrls[name] = if(exercise.image_url.isNotEmpty()) exercise.image_url[0]!! else ""
                Log.println(Log.DEBUG, "WorkoutLoggerOverview imageURLs", exercise.image_url.toString())
            }
    }

    private fun executeSearch() {
        getAllExercisesJob?.cancel()
        getAllExercisesJob = exerciseTrackerUseCases
            .getExerciseForName(state.exerciseFilterText.trim())
            .onEach { exercises ->
                if(exercises.isEmpty()) {
                    _uiEvent.send(
                        UiEvent.ShowSnackbar(
                            UiText.StringResource(R.string.empty_results)
                        )
                    )
                }
                state = state.copy(
                    trackableExercise = exercises.map {
                        TrackableExerciseState(exercise = it)
                    }
                )
            }.launchIn(viewModelScope)
    }

}