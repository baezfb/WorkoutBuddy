package com.example.workout_logger_presentation.workout_logger_overview

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workout_logger_domain.use_case.ExerciseTrackerUseCases
import com.example.workout_logger_presentation.search_exercise.TrackableExerciseState
import com.hbaez.core.R
import com.hbaez.core.domain.preferences.Preferences
import com.hbaez.core.util.UiEvent
import com.hbaez.core.util.UiText
import com.hbaez.user_auth_presentation.model.CompletedWorkout
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
        refreshWorkouts()
        refreshExercises()
        executeSearch()
        refreshKalendarEvents(
            beginning = LocalDate.of(state.date.year, state.date.month, state.date.dayOfMonth)
                .minusDays(if(state.date.dayOfMonth > 7) state.date.dayOfMonth.toLong() else 7),
            end = LocalDate.of(state.date.year, state.date.month, state.date.dayOfMonth).plusDays(31)
        )
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
                refreshKalendarEvents(
                    beginning = state.date
                        .minusDays(if(event.dayOfMonth > 7) event.dayOfMonth.toLong() else 7),
                    end = LocalDate.of(event.year, event.month, event.dayOfMonth).plusDays((31 - event.dayOfMonth).toLong())
                )
            }
        }
    }

    private fun refreshKalendarEvents(beginning: LocalDate, end: LocalDate){
        viewModelScope.launch {
            val dateList = storageService.getCalendarEvents(begDate = beginning, endDate = end)
            dateList.forEach { date ->
                if(!state.kalendarEvents.events.any { event ->
                        event.date.toString() == date.toString()
                    }){
                    state = state.copy(
                        kalendarEvents = state.kalendarEvents.copy(
                            events = (state.kalendarEvents.events.toMutableList() + KalendarEvent(
                                date = kotlinx.datetime.LocalDate(date.year, date.monthValue, date.dayOfMonth),
                                eventName = date.toString()
                            )).toList()
                        )
                    )
                }
            }
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