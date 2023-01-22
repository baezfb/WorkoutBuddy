package com.hbaez.user_auth_presentation.model.service

import com.hbaez.core.domain.model.UserInfo
import com.hbaez.user_auth_presentation.model.Task
import kotlinx.coroutines.flow.Flow

interface StorageService {
    val userPrefs: Flow<UserInfo>
    val tasks: Flow<List<Task>>

    suspend fun getTask(taskId: String): Task?

    suspend fun saveUserInfo(userInfo: UserInfo): String
    suspend fun save(task: Task): String
    suspend fun update(task: Task)
    suspend fun delete(taskId: String)
    suspend fun deleteAllForUser(userId: String)
}