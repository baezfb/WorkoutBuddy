package com.hbaez.user_auth_presentation.model.service.impl

import android.util.Log
import com.hbaez.user_auth_presentation.model.Task
import com.hbaez.user_auth_presentation.model.service.AccountService
import com.hbaez.user_auth_presentation.model.service.StorageService
import com.hbaez.user_auth_presentation.model.service.trace
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.hbaez.core.domain.model.ActivityLevel
import com.hbaez.core.domain.model.Gender
import com.hbaez.core.domain.model.GoalType
import com.hbaez.core.domain.model.UserInfo
import javax.inject.Inject
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.tasks.asDeferred
import kotlinx.coroutines.tasks.await

class StorageServiceImpl @Inject
constructor(private val firestore: FirebaseFirestore, private val auth: AccountService):
    StorageService {

    override val userPrefs: Flow<UserInfo>
        get() = emptyFlow()

    override val tasks: Flow<List<Task>>
        get() = emptyFlow()

    override suspend fun getTask(taskId: String): Task? =
        currentCollection(auth.currentUserId).document(taskId).get().await().toObject()

    override suspend fun saveUserInfo(userInfo: UserInfo): String =
        trace(SAVE_USER_INFO_TRACE) {
            Log.println(Log.DEBUG, "auth.currentUserId", auth.currentUserId)
            if(userInfo.id == ""){
                currentCollection(auth.currentUserId).document(auth.currentUserId).set(userInfo).await()
                return auth.currentUserId
            } else {
                currentCollection(auth.currentUserId).document(userInfo.id).set(userInfo).await()
                return userInfo.id
            }
        }

    override suspend fun getUserInfo(): UserInfo? {
        val doc = currentCollection(auth.currentUserId).document(auth.currentUserId).get().await()
        Log.println(Log.DEBUG, "doc log", doc.get("age").toString())
        return UserInfo(
            id = doc.get("id").toString(),
            gender = Gender.fromString(doc.get("gender").toString()),
            age = doc.get("age").toString().toInt(),
            weight = doc.get("weight").toString().toFloat(),
            height = doc.get("height").toString().toInt(),
            activityLevel = ActivityLevel.fromString(doc.get("activityLevel").toString()),
            goalType = GoalType.fromString(doc.get("goalType").toString()),
            carbRatio = doc.get("carbRatio").toString().toFloat(),
            proteinRatio = doc.get("proteinRatio").toString().toFloat(),
            fatRatio = doc.get("fatRatio").toString().toFloat()
        )
    }

    override suspend fun save(task: Task): String =
        trace(SAVE_TASK_TRACE) { currentCollection(auth.currentUserId).add(task).await().id }

    override suspend fun update(task: Task): Unit =
        trace(UPDATE_TASK_TRACE) {
            currentCollection(auth.currentUserId).document(task.id).set(task).await()
        }

    override suspend fun delete(taskId: String) {
        currentCollection(auth.currentUserId).document(taskId).delete().await()
    }

    // TODO: It's not recommended to delete on the client:
    // https://firebase.google.com/docs/firestore/manage-data/delete-data#kotlin+ktx_2
    override suspend fun deleteAllForUser(userId: String) {
        val matchingTasks = currentCollection(userId).get().await()
        matchingTasks.map { it.reference.delete().asDeferred() }.awaitAll()
    }

    private fun currentCollection(uid: String): CollectionReference =
        firestore.collection(USER_COLLECTION).document(uid).collection(USER_INFO)

    companion object {
        private const val USER_COLLECTION = "users"
        private const val TASK_COLLECTION = "tasks"
        private const val USER_INFO = "user_info"
        private const val SAVE_USER_INFO_TRACE = "saveUserInfo"
        private const val SAVE_TASK_TRACE = "saveTask"
        private const val UPDATE_TASK_TRACE = "updateTask"
    }
}