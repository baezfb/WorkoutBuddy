package com.hbaez.core.domain.preferences

import com.hbaez.core.domain.model.ActivityLevel
import com.hbaez.core.domain.model.AuthTokenInfo
import com.hbaez.core.domain.model.Gender
import com.hbaez.core.domain.model.GoalType
import com.hbaez.core.domain.model.LoginInfo
import com.hbaez.core.domain.model.TrackedExercise
import com.hbaez.core.domain.model.UserInfo

interface Preferences {
    fun saveGender(gender: Gender)
    fun saveAge(age: Int)
    fun saveWeight(weight: Float)
    fun saveHeight(height: Int)
    fun saveActivityLevel(level: ActivityLevel)
    fun saveGoalType(type: GoalType)
    fun saveCarbRatio(ratio: Float)
    fun saveProteinRatio(ratio: Float)
    fun saveFatRatio(ratio: Float)
    fun saveFirebaseUserInfoId(id: String)

    fun saveLoginInfo(username: String, email: String)
    fun loadLoginInfo(): LoginInfo
    fun loadUserInfo(): UserInfo
    fun updateUserInfo(userInfo: UserInfo, userID: String)

    fun saveShouldShowOnboarding(shouldShow: Boolean)
    fun loadShouldShowOnboarding(): Boolean

    fun saveAuthKey(auth_key: String, auth_key_exp: Long)
    fun loadShouldCreateAuthKey(): Boolean
    fun loadAuthTokenInfo(): AuthTokenInfo
    fun saveShouldGenAuthToken(shouldGenAuthToken: Boolean)
    fun shouldGenAuthToken(): Boolean


    fun newTrackedExercise(trackedExercise: TrackedExercise)

    fun loadTrackedExercise(): TrackedExercise

    fun removeTrackedExercise()

    companion object {
        const val KEY_GENDER = "gender"
        const val KEY_AGE = "age"
        const val KEY_WEIGHT = "weight"
        const val KEY_HEIGHT = "height"
        const val KEY_ACTIVITY_LEVEL = "activity_level"
        const val KEY_GOAL_TYPE = "goal_type"
        const val KEY_CARB_RATIO = "carb_ratio"
        const val KEY_PROTEIN_RATIO = "protein_ratio"
        const val KEY_FAT_RATIO = "fat_ratio"
        const val KEY_SHOULD_SHOW_ONBOARDING = "should_show_onboarding"
        const val KEY_AUTH = "auth_key"
        const val KEY_SHOULD_CREATE_AUTH_KEY = "should_create_auth_key"
        const val KEY_AUTH_KEY_EXP = "auth_key_exp"
        const val KEY_FIREBASE_USER_INFO_ID = "key_firebase_user_info_id"

        const val KEY_USERNAME="username"
        const val KEY_EMAIL="email"

        const val KEY_ROW_ID = "row_id"
        const val KEY_EXERCISE_ID = "exercise_id"
        const val KEY_EXERCISE_NAME = "exercise_name"
        const val KEY_EXERCISE_BASE = "exercise_base"
        const val KEY_EXERCISE_DESCRIPTION = "exercise_description"
        const val KEY_EXERCISE_MUSCLES = "exercise_muscles"
        const val KEY_EXERCISE_MUSCLES_SECONDARY = "exercise_muscles_secondary"
        const val KEY_EXERCISE_EQUIPMENT = "exercise_equipment"
        const val KEY_EXERCISE_IMAGE_URL = "exercise_image_url"
        const val KEY_EXERCISE_IS_MAIN = "exercise_is_main"
        const val KEY_EXERCISE_IS_FRONT = "exercise_is_front"
        const val KEY_EXERCISE_MUSCLE_NAME_MAIN = "exercise_muscle_name_main"
        const val KEY_EXERCISE_IMAGE_URL_MAIN = "exercise_image_url_main"
        const val KEY_EXERCISE_IMAGE_URL_SECONDARY = "exercise_image_url_secondary"
        const val KEY_EXERCISE_MUSCLE_NAME_SECONDARY = "exercise_muscle_name_secondary"


    }
}