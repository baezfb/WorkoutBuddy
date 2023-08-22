package com.hbaez.user_auth_presentation.model.service.impl

import android.util.Log
import com.hbaez.user_auth_presentation.model.Task
import com.hbaez.user_auth_presentation.model.service.AccountService
import com.hbaez.user_auth_presentation.model.service.StorageService
import com.hbaez.user_auth_presentation.model.service.trace
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.snapshots
import com.google.firebase.firestore.ktx.toObject
import com.hbaez.core.domain.model.ActivityLevel
import com.hbaez.core.domain.model.Gender
import com.hbaez.core.domain.model.GoalType
import com.hbaez.core.domain.model.UserInfo
import com.hbaez.user_auth_presentation.model.CalendarDates
import com.hbaez.user_auth_presentation.model.CompletedWorkout
import com.hbaez.user_auth_presentation.model.ExerciseTemplate
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
import java.time.LocalDate

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
                                id = it.id,
                                name = it.get("name").toString(),
                                exerciseName = it.get("exerciseName").toString(),
                                exerciseId = it.get("exerciseId").toString(),
                                sets = it.get("sets").toString().toInt(),
                                rest = it.get("rest").toString().removeSurrounding("[","]").split(",").map { elem -> elem.trim() },
                                reps = it.get("reps").toString().removeSurrounding("[","]").split(",").map { elem -> elem.trim() },
                                weight = it.get("weight").toString().removeSurrounding("[","]").split(",").map { elem -> elem.trim() },
                                rowId = it.get("rowId").toString().toInt(),
                                position = it.get("position").toString().toInt(),
                                lastUsedId = it.get("lastUsedId").toString().toInt(),
                                lastUsedDate = it.get("lastUsedDate").toString()
                            )
                        }
                    }
            }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val calendarDates: Flow<CalendarDates>
        get() =
            auth.currentUser.flatMapLatest { user ->
                calendarDateCollection(user.id).document(auth.currentUserId).snapshots().map { snapshot ->
                    if(snapshot.exists()){
                        CalendarDates(
                            calendarDates = (snapshot.get("calendarDates") as List<*>).filterIsInstance<String>()
                        )
                    } else {
                        CalendarDates(calendarDates = emptyList())
                    }
                }
            }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val exercises: Flow<List<ExerciseTemplate>>
        get() = auth.currentUser.flatMapLatest { user ->
            exerciseTemplateCollection(user.id)
                .snapshots()
                .map { snapshot ->
                    snapshot.documents.map {
                        ExerciseTemplate(
                            id = it.id,
                            name = it.get("name").toString(),
                            description = it.get("description").toString(),
                            muscle_name_main = it.get("muscle_name_main").toString(),
                            muscle_name_secondary = it.get("muscle_name_secondary").toString(),
                            image_url_main = it.get("image_url_main").toString(),
                            image_url_secondary = it.get("image_url_secondary").toString(),
                            image_url = it.get("image_url").toString(),
                            is_front = it.get("_front").toString(),
                            is_main = it.get("_main").toString(),
                            muscles = it.get("muscles").toString(),
                            muscles_secondary = it.get("muscles_secondary").toString()
                        )
                    }
                }
        }

    override suspend fun getTask(taskId: String): Task? =
        userInfoCollection(auth.currentUserId).document(taskId).get().await().toObject()

    override suspend fun getCompletedWorkoutByDate(date: String): List<CompletedWorkout> {
        val completedWorkouts = mutableListOf<CompletedWorkout>()
        completedWorkoutCollection(auth.currentUserId, date).get().addOnSuccessListener { snapshot ->
            for (document in snapshot.documents) {
                val completedWorkout = CompletedWorkout(
                    docId = document.id,
                    workoutName = document.get("workoutName").toString(),
                    workoutId = document.get("workoutId").toString().toInt(),
                    exerciseName = document.get("exerciseName").toString(),
                    exerciseId = document.get("exerciseId").toString(),
                    sets = document.get("sets").toString().toInt(),
                    rest = document.get("rest").toString().removeSurrounding("[","]").split(",").map { elem -> elem.trim() },
                    reps = document.get("reps").toString().removeSurrounding("[","]").split(",").map { elem -> elem.trim() },
                    weight = document.get("weight").toString().removeSurrounding("[","]").split(",").map { elem -> elem.trim() },
                    isCompleted = document.get("completed").toString().removeSurrounding("[","]").split(",").map { elem -> elem.trim() },
                    dayOfMonth = document.get("dayOfMonth").toString().toInt(),
                    month = document.get("month").toString().toInt(),
                    year = document.get("year").toString().toInt()
                )
                completedWorkouts.add(completedWorkout)
            }
        }
            .await()
        return completedWorkouts
    }

    override suspend fun saveUserInfo(userInfo: UserInfo): String =
        trace(SAVE_USER_INFO_TRACE) {
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
            age = doc.get("age").toString().toLong(),
            weight = doc.get("weight").toString().toFloat(),
            height = doc.get("height").toString().toInt(),
            activityLevel = ActivityLevel.fromString(doc.get("activityLevel").toString()),
            goalType = GoalType.fromString(doc.get("goalType").toString()),
            carbRatio = doc.get("carbRatio").toString().toFloat(),
            proteinRatio = doc.get("proteinRatio").toString().toFloat(),
            fatRatio = doc.get("fatRatio").toString().toFloat(),
            timerJump = doc.get("timerJump").toString().toInt(),
            timerSeconds = doc.get("timerSeconds").toString().toInt(),
        )
    }

    override suspend fun saveWorkoutTemplate(workoutTemplate: WorkoutTemplate): String =
        trace(SAVE_WORKOUT_TEMPLATE) {
            val documentRef = workoutTemplateCollection(auth.currentUserId).document()
            documentRef.set(workoutTemplate, SetOptions.merge())
            documentRef.id
        }

    override suspend fun updateWorkoutTemplate(workoutTemplate: WorkoutTemplate): String =
        trace(SAVE_WORKOUT_TEMPLATE) {
            val updateData = mutableMapOf<String, Any>()
            updateData["currentSet"] = workoutTemplate.currentSet
            if (workoutTemplate.exerciseId != null) {
                updateData["exerciseId"] = workoutTemplate.exerciseId
            }
            if (workoutTemplate.rowId != -1) {
                updateData["rowId"] = workoutTemplate.rowId
            }
            if (workoutTemplate.position != -1) {
                updateData["position"] = workoutTemplate.position
            }
            if (workoutTemplate.lastUsedDate != null && workoutTemplate.lastUsedDate != "null") {
                updateData["lastUsedDate"] = workoutTemplate.lastUsedDate
            }
            updateData["exerciseName"] = workoutTemplate.exerciseName
            updateData["lastUsedId"] = workoutTemplate.lastUsedId
            updateData["name"] = workoutTemplate.name
            updateData["reps"] = workoutTemplate.reps
            updateData["rest"] = workoutTemplate.rest
            updateData["sets"] = workoutTemplate.sets
            updateData["weight"] = workoutTemplate.weight
            workoutTemplateCollection(auth.currentUserId).document(workoutTemplate.id).update(updateData).await()
            return workoutTemplate.id
        }

    override suspend fun deleteWorkoutTemplate(workoutTemplate: WorkoutTemplate) {
        trace(DELETE_WORKOUT_TEMPLATE) {
            workoutTemplateCollection(auth.currentUserId).document(workoutTemplate.id).delete().await()
        }
    }

    override suspend fun saveCompletedWorkout(completedWorkout: CompletedWorkout, date: String): String =
        trace(SAVE_COMPLETED_WORKOUT) {
            val documentRef = completedWorkoutCollection(auth.currentUserId, date).document()
            documentRef.set(completedWorkout, SetOptions.merge())
            documentRef.id
        }

    override suspend fun updateCompletedWorkout(
        completedWorkout: CompletedWorkout,
        date: String
    ): String {
        TODO("Not yet implemented")
    }

    override suspend fun deleteCompletedWorkout(
        completedWorkout: CompletedWorkout,
        date: String
    ): Boolean = trace(DELETE_COMPLETED_WORKOUT) {
            completedWorkoutCollection(auth.currentUserId, date).document(completedWorkout.docId).delete().await()
            // check if collection is empty
            completedWorkoutCollection(auth.currentUserId, date).get().await().documents.isEmpty()
        }

    override suspend fun saveExerciseTemplate(exerciseTemplate: ExerciseTemplate): String =
        trace(SAVE_EXERCISE_TEMPLATE) {
            val documentRef = exerciseTemplateCollection(auth.currentUserId).document()
            documentRef.set(exerciseTemplate, SetOptions.merge())
//                .add(exerciseTemplate).await().id
            if(exerciseTemplate.id.isEmpty()){
                val updateData = mutableMapOf<String, Any?>()
                updateData["docId"] = documentRef.id
                exerciseTemplateCollection(auth.currentUserId).document(documentRef.id).update(updateData)
                return documentRef.id
            } else {
                return exerciseTemplate.id
            }
        }

    override suspend fun updateExerciseTemplate(exerciseTemplate: ExerciseTemplate): String =
        trace(SAVE_EXERCISE_TEMPLATE) {
            val updateData = mutableMapOf<String, Any?>()
            updateData["_front"] = exerciseTemplate.is_front
            updateData["_main"] = exerciseTemplate.is_main
            updateData["description"] = exerciseTemplate.description
            updateData["equipment"] = exerciseTemplate.equipment
            updateData["exerciseBase"] = exerciseTemplate.exerciseBase
            updateData["image_url"] = exerciseTemplate.image_url
            updateData["image_url_main"] = exerciseTemplate.image_url_main
            updateData["image_url_secondary"] = exerciseTemplate.image_url_secondary
            updateData["muscle_name_main"] = exerciseTemplate.muscle_name_main
            updateData["muscle_name_secondary"] = exerciseTemplate.muscle_name_secondary
            updateData["muscles"] = exerciseTemplate.muscles
            updateData["muscles_secondary"] = exerciseTemplate.muscles_secondary
            updateData["name"] = exerciseTemplate.name
            updateData["id"] = exerciseTemplate.id
            exerciseTemplateCollection(auth.currentUserId).document(exerciseTemplate.docId).update(updateData).await()
            return exerciseTemplate.id
        }

    override suspend fun saveCalendarDate(calendarDates: CalendarDates) {
        trace(SAVE_CALENDAR_DATE) {
            val documentRef = calendarDateCollection(auth.currentUserId).document(auth.currentUserId)
            documentRef.set(calendarDates, SetOptions.merge())
        }
    }

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

    private fun exerciseTemplateCollection(uid: String): CollectionReference =
        firestore.collection(USER_COLLECTION).document(uid).collection(EXERCISE_TEMPLATE)

    private fun calendarDateCollection(uid: String): CollectionReference =
        firestore.collection(USER_COLLECTION).document(uid).collection(CALENDAR_DATES)

    companion object {
        private const val USER_COLLECTION = "users"
        private const val TASK_COLLECTION = "tasks"
        private const val USER_INFO = "user_info"
        private const val SAVE_USER_INFO_TRACE = "saveUserInfo"
        private const val SAVE_TASK_TRACE = "saveTask"
        private const val SAVE_WORKOUT_TEMPLATE = "saveWorkoutTemplate"
        private const val SAVE_COMPLETED_WORKOUT = "saveCompletedWorkout"
        private const val SAVE_EXERCISE_TEMPLATE = "saveExerciseTemplate"
        private const val DELETE_WORKOUT_TEMPLATE = "deleteWorkoutTemplate"
        private const val DELETE_COMPLETED_WORKOUT = "deleteCompletedWorkout"
        private const val UPDATE_COMPLETED_WORKOUT = "updateCompletedWorkout"
        private const val SAVE_CALENDAR_DATE = "saveCalendarDate"
        private const val UPDATE_TASK_TRACE = "updateTask"
        private const val WORKOUT_TEMPLATE = "workouts"
        private const val COMPLETED_WORKOUT = "completed_workouts"
        private const val EXERCISE_TEMPLATE = "exercises"
        private const val CALENDAR_DATES = "calendarDates"
    }
}