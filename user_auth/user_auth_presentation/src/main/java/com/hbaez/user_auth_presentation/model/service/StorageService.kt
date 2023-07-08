package com.hbaez.user_auth_presentation.model.service

import com.hbaez.core.domain.model.UserInfo
import com.hbaez.user_auth_presentation.model.CompletedWorkout
import com.hbaez.user_auth_presentation.model.ExerciseTemplate
import com.hbaez.user_auth_presentation.model.Task
import com.hbaez.user_auth_presentation.model.WorkoutTemplate
import kotlinx.coroutines.flow.Flow

interface StorageService {
    val userPrefs: Flow<UserInfo>
    val tasks: Flow<List<Task>>
    val workouts: Flow<List<WorkoutTemplate>>
    val exercises: Flow<List<ExerciseTemplate>>

    suspend fun getTask(taskId: String): Task?
    suspend fun getCompletedWorkoutByDate(date: String): List<CompletedWorkout>
    suspend fun saveUserInfo(userInfo: UserInfo): String
    suspend fun getUserInfo(): UserInfo?
    suspend fun saveWorkoutTemplate(workoutTemplate: WorkoutTemplate): String
    suspend fun updateWorkoutTemplate(workoutTemplate: WorkoutTemplate): String
    suspend fun saveCompletedWorkout(completedWorkout: CompletedWorkout, date: String): String
    suspend fun saveExerciseTemplate(exerciseTemplate: ExerciseTemplate): String
    suspend fun updateExerciseTemplate(exerciseTemplate: ExerciseTemplate): String
    suspend fun save(task: Task): String
    suspend fun update(task: Task)
    suspend fun delete(taskId: String)
    suspend fun deleteAllForUser(userId: String)
}