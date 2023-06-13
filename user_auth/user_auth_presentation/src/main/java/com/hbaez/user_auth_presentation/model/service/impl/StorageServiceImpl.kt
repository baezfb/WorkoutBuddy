package com.hbaez.user_auth_presentation.model.service.impl

import android.util.Log
import com.hbaez.user_auth_presentation.model.Task
import com.hbaez.user_auth_presentation.model.service.AccountService
import com.hbaez.user_auth_presentation.model.service.StorageService
import com.hbaez.user_auth_presentation.model.service.trace
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.snapshots
import com.google.firebase.firestore.ktx.toObject
import com.hbaez.core.domain.model.ActivityLevel
import com.hbaez.core.domain.model.Gender
import com.hbaez.core.domain.model.GoalType
import com.hbaez.core.domain.model.UserInfo
import com.hbaez.user_auth_presentation.model.CompletedWorkout
import com.hbaez.user_auth_presentation.model.WorkoutTemplate
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.asDeferred
import kotlinx.coroutines.tasks.await

class StorageServiceImpl @Inject
constructor(private val firestore: FirebaseFirestore, private val auth: AccountService):
    StorageService {

    override val userPrefs: Flow<UserInfo>
        get() = emptyFlow()

    override val tasks: Flow<List<Task>>
        get() = emptyFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    override val workouts: Flow<List<WorkoutTemplate>>
        get() =
            auth.currentUser.flatMapLatest { user ->
                workoutTemplateCollection(user.id)
                    .snapshots()
                    .map { snapshot -> //snapshot.toObjects(WorkoutTemplate::class.java)
                        snapshot.documents.map {
                            WorkoutTemplate(
                                id = it.get("id").toString(),
                                name = it.get("name").toString(),
                                exerciseName = it.get("exerciseName").toString(),
                                exerciseId = it.get("exerciseId").toString().toInt(),
                                sets = it.get("sets").toString().toInt(),
                                rest = it.get("rest").toString().removeSurrounding("[","]").split(",").map { elem -> elem.trim() },
                                reps = it.get("reps").toString().removeSurrounding("[","]").split(",").map { elem -> elem.trim() },
                                weight = it.get("weight").toString().removeSurrounding("[","]").split(",").map { elem -> elem.trim() },
                                rowId = it.get("rowId").toString().toInt(),
                                lastUsedId = it.get("lastUsedId").toString().toInt(),
                            )
                        }
                    }
            }

    override suspend fun getTask(taskId: String): Task? =
        userInfoCollection(auth.currentUserId).document(taskId).get().await().toObject()

    override suspend fun getCompletedWorkoutByDate(date: String): List<CompletedWorkout> {
        val completedWorkouts = completedWorkoutCollection(auth.currentUserId, date).snapshots().map { snapshot ->
            return@map snapshot.documents.map {
                CompletedWorkout(
                    workoutName = it.get("workoutName").toString(),
                    workoutId = it.get("workoutId").toString().toInt(),
                    exerciseName = it.get("exerciseName").toString(),
                    exerciseId = it.get("exerciseId").toString().toInt(),
                    sets = it.get("sets").toString().toInt(),
                    rest = it.get("rest").toString().removeSurrounding("[","]").split(",").map { elem -> elem.trim() },
                    reps = it.get("reps").toString().removeSurrounding("[","]").split(",").map { elem -> elem.trim() },
                    weight = it.get("weight").toString().removeSurrounding("[","]").split(",").map { elem -> elem.trim() },
                    dayOfMonth = it.get("dayOfMonth").toString().toInt(),
                    month = it.get("month").toString().toInt(),
                    year = it.get("year").toString().toInt()
                )
            }
        }
        return completedWorkouts.first()
    }
    override suspend fun saveUserInfo(userInfo: UserInfo): String =
        trace(SAVE_USER_INFO_TRACE) {
            Log.println(Log.DEBUG, "auth.currentUserId", auth.currentUserId)
            if(userInfo.id == ""){
                userInfoCollection(auth.currentUserId).document(auth.currentUserId).set(userInfo).await()
                return auth.currentUserId
            } else {
                userInfoCollection(auth.currentUserId).document(userInfo.id).set(userInfo).await()
                return userInfo.id
            }
        }

    override suspend fun getUserInfo(): UserInfo {
        val doc = userInfoCollection(auth.currentUserId).document(auth.currentUserId).get().await()
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

    override suspend fun saveWorkoutTemplate(workoutTemplate: WorkoutTemplate): String =
        trace(SAVE_WORKOUT_TEMPLATE) { workoutTemplateCollection(auth.currentUserId).add(workoutTemplate).await().id }

    override suspend fun saveCompletedWorkout(completedWorkout: CompletedWorkout, date: String): String =
        trace(SAVE_COMPLETED_WORKOUT) { completedWorkoutCollection(auth.currentUserId, date).add(completedWorkout).await().id }

    override suspend fun save(task: Task): String =
        trace(SAVE_TASK_TRACE) { userInfoCollection(auth.currentUserId).add(task).await().id }

    override suspend fun update(task: Task): Unit =
        trace(UPDATE_TASK_TRACE) {
            userInfoCollection(auth.currentUserId).document(task.id).set(task).await()
        }

    override suspend fun delete(taskId: String) {
        userInfoCollection(auth.currentUserId).document(taskId).delete().await()
    }

    // TODO: It's not recommended to delete on the client:
    // https://firebase.google.com/docs/firestore/manage-data/delete-data#kotlin+ktx_2
    override suspend fun deleteAllForUser(userId: String) {
        val matchingTasks = userInfoCollection(userId).get().await()
        matchingTasks.map { it.reference.delete().asDeferred() }.awaitAll()
    }

    private fun userInfoCollection(uid: String): CollectionReference =
        firestore.collection(USER_COLLECTION).document(uid).collection(USER_INFO)

    private fun workoutTemplateCollection(uid: String): CollectionReference =
        firestore.collection(USER_COLLECTION).document(uid).collection(WORKOUT_TEMPLATE)

    private fun completedWorkoutCollection(uid: String, date: String): CollectionReference =
        firestore.collection(USER_COLLECTION).document(uid).collection(COMPLETED_WORKOUT).document(date).collection(COMPLETED_WORKOUT)

    companion object {
        private const val USER_COLLECTION = "users"
        private const val TASK_COLLECTION = "tasks"
        private const val USER_INFO = "user_info"
        private const val SAVE_USER_INFO_TRACE = "saveUserInfo"
        private const val SAVE_TASK_TRACE = "saveTask"
        private const val SAVE_WORKOUT_TEMPLATE = "saveWorkoutTemplate"
        private const val SAVE_COMPLETED_WORKOUT = "saveCompletedWorkout"
        private const val UPDATE_TASK_TRACE = "updateTask"
        private const val WORKOUT_TEMPLATE = "workouts"
        private const val COMPLETED_WORKOUT = "completed_workouts"
    }
}