package com.hbaez.analyzer_presentation.analyzer_presentation

import co.yml.charts.common.model.Point
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hbaez.analyzer_presentation.analyzer_presentation.components.CalculateActivityIndexFromDate
import com.hbaez.core.R
import com.hbaez.core.domain.preferences.Preferences
import com.hbaez.core.util.UiEvent
import com.hbaez.core.util.UiText
import com.hbaez.user_auth_presentation.model.service.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class AnalyzerOverviewModel @Inject constructor(
    private val storageService: StorageService,
    private val preferences: Preferences
): ViewModel(){

    var state by mutableStateOf(AnalyzerState())
        private set
    private var getWorkoutsForDateJob: Job? = null

    val workoutTemplates = storageService.workouts

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        viewModelScope.launch {
            val activityList = MutableList(52) { 0 } // 52 weeks
            storageService.calendarDates.first().calendarDates.forEach {
                val currDate = LocalDate.parse(it)
                val index = CalculateActivityIndexFromDate(currDate, state.date)
                activityList[index] += 1
            }
            state = state.copy(
                activityCountList = activityList
            )
            onEvent(AnalyzerEvent.OnContributionChartClick(51))
            onEvent(AnalyzerEvent.OnChooseExerciseGraphOne(state.graph1_exerciseName))
        }
    }

    fun onEvent(event: AnalyzerEvent){
        when(event) {
            is AnalyzerEvent.OnContributionChartClick -> {
                val currentActivityDate = state.date.with(DayOfWeek.MONDAY).minusDays((51 - event.index) * 7L)
                state = state.copy(
                    currentActivityIndex = event.index,
                    currentActivityDate = currentActivityDate,
                    workoutList = emptyList(),
                    exerciseList = emptyList()
                )
                getWorkoutsForWeek(currentActivityDate)
            }

            is AnalyzerEvent.OnChooseExerciseGraphOne -> {
                viewModelScope.launch {
                    // fetch exercise dates
                    val exerciseDates = storageService.getExerciseDate(event.exerciseName)

                    // fetch completed exercise info
                    val weightPointsData = mutableListOf<List<Point>>()
                    val repsPointsData = mutableListOf<List<Point>>()
                    exerciseDates.exerciseDates.forEach { date ->
                        storageService.getCompletedWorkoutByDate(date).forEach { completedWorkout ->
                            if(completedWorkout.exerciseName == event.exerciseName){
                                val currWeightList = mutableListOf<Point>()
                                completedWorkout.weight.forEachIndexed { index, s ->
                                    currWeightList.add(Point(index.toFloat(), s.toFloat()))
                                }
                                weightPointsData.add(currWeightList.toList())

                                val currRepsList = mutableListOf<Point>()
                                completedWorkout.reps.forEachIndexed { index, s ->
                                    currRepsList.add(Point(index.toFloat(), s.toFloat()))
                                }
                                repsPointsData.add(currRepsList.toList())
                            }
                        }
                    }
                    state = state.copy(
                        graph1_exerciseName = event.exerciseName,
                        graph1_weightPointsData = weightPointsData,
                        graph1_repsPointsData = repsPointsData
                    )
                    Log.println(Log.DEBUG, "pointsData", weightPointsData.toString())
                }
            }
        }
    }

    private fun getWorkoutsForWeek(startDate: LocalDate) {
        getWorkoutsForDateJob?.cancel()
        getWorkoutsForDateJob = viewModelScope.launch {
            for (i in 0 until 7) {
                val currentDate = startDate.plusDays(i.toLong())
                val currentDateCompletedWorkouts = storageService.getCompletedWorkoutByDate(currentDate.toString()).toMutableList()
                currentDateCompletedWorkouts.forEach { completedWorkout ->
                    coroutineScope {
                        var found = false
                        state = state.copy(
                            workoutList = state.workoutList.map {
                                if(it[0] == completedWorkout.workoutName){
                                    found = true
                                    listOf(it[0], (it[1].toInt() + 1).toString())
                                } else it
                            }
                        )
                        if(!found) {
                            if(completedWorkout.workoutName != ""){
                                val newWorkoutList = state.workoutList.toMutableList()
                                newWorkoutList.add(listOf(completedWorkout.workoutName, "1"))
                                state = state.copy(
                                    workoutList = newWorkoutList.toList()
                                )
                            } else{
                                if(!state.exerciseList.contains(completedWorkout.exerciseName)) {
                                    state = state.copy(
                                        exerciseList = state.exerciseList + completedWorkout.exerciseName
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}