package com.hbaez.core.data.preferences

import android.content.SharedPreferences
import com.hbaez.core.domain.model.ActivityLevel
import com.hbaez.core.domain.model.AuthTokenInfo
import com.hbaez.core.domain.model.Gender
import com.hbaez.core.domain.model.GoalType
import com.hbaez.core.domain.model.LoginInfo
import com.hbaez.core.domain.model.TrackedExercise
import com.hbaez.core.domain.model.UserInfo
import com.hbaez.core.domain.preferences.Preferences

class DefaultPreferences(
    private val sharedPref: SharedPreferences
): Preferences {
    override fun saveGender(gender: Gender) {
        sharedPref.edit()
            .putString(Preferences.KEY_GENDER, gender.name)
            .apply()
    }

    override fun saveAge(age: Int) {
        sharedPref.edit()
            .putInt(Preferences.KEY_AGE, age)
            .apply()
    }

    override fun saveWeight(weight: Float) {
        sharedPref.edit()
            .putFloat(Preferences.KEY_WEIGHT, weight)
            .apply()
    }

    override fun saveHeight(height: Int) {
        sharedPref.edit()
            .putInt(Preferences.KEY_HEIGHT, height)
            .apply()
    }

    override fun saveActivityLevel(level: ActivityLevel) {
        sharedPref.edit()
            .putString(Preferences.KEY_ACTIVITY_LEVEL, level.name)
            .apply()
    }

    override fun saveGoalType(type: GoalType) {
        sharedPref.edit()
            .putString(Preferences.KEY_GOAL_TYPE, type.name)
            .apply()
    }

    override fun saveCarbRatio(ratio: Float) {
        sharedPref.edit()
            .putFloat(Preferences.KEY_CARB_RATIO, ratio)
            .apply()
    }

    override fun saveProteinRatio(ratio: Float) {
        sharedPref.edit()
            .putFloat(Preferences.KEY_PROTEIN_RATIO, ratio)
            .apply()
    }

    override fun saveFatRatio(ratio: Float) {
        sharedPref.edit()
            .putFloat(Preferences.KEY_FAT_RATIO, ratio)
            .apply()
    }

    override fun saveFirebaseUserInfoId(id: String) {
        sharedPref.edit()
            .putString(Preferences.KEY_FIREBASE_USER_INFO_ID, id)
            .apply()
    }

    override fun saveLoginInfo(username: String, email: String) {
        sharedPref.edit()
            .putString(Preferences.KEY_USERNAME, username)
            .putString(Preferences.KEY_EMAIL, email)
            .apply()
    }

    override fun loadLoginInfo(): LoginInfo {
        val username = sharedPref.getString(Preferences.KEY_USERNAME, "")
        val email = sharedPref.getString(Preferences.KEY_EMAIL, "")

        return LoginInfo(
            username = username,
            email = email
        )
    }

    override fun loadUserInfo(): UserInfo {
        val age = sharedPref.getInt(Preferences.KEY_AGE, -1)
        val height = sharedPref.getInt(Preferences.KEY_HEIGHT, -1)
        val weight = sharedPref.getFloat(Preferences.KEY_WEIGHT, -1f)
        val genderString = sharedPref.getString(Preferences.KEY_GENDER, null)
        val activityLevelString = sharedPref
            .getString(Preferences.KEY_ACTIVITY_LEVEL, null)
        val goalType = sharedPref.getString(Preferences.KEY_GOAL_TYPE, null)
        val carbRatio = sharedPref.getFloat(Preferences.KEY_CARB_RATIO, -1f)
        val proteinRatio = sharedPref.getFloat(Preferences.KEY_PROTEIN_RATIO, -1f)
        val fatRatio = sharedPref.getFloat(Preferences.KEY_FAT_RATIO, -1f)
        val firebaseUserInfoID = sharedPref.getString(Preferences.KEY_FIREBASE_USER_INFO_ID, "")

        return UserInfo(
            id = firebaseUserInfoID!!,
            gender = Gender.fromString(genderString ?: "male"),
            age = age,
            weight = weight,
            height = height,
            activityLevel = ActivityLevel.fromString(activityLevelString ?: "medium"),
            goalType = GoalType.fromString(goalType ?: "keep_weight"),
            carbRatio = carbRatio,
            proteinRatio = proteinRatio,
            fatRatio = fatRatio
        )
    }

    override fun updateUserInfo(userInfo: UserInfo, userID: String) {
        sharedPref.edit()
            .putString(Preferences.KEY_GENDER, userInfo.gender.name)
            .putInt(Preferences.KEY_AGE, userInfo.age)
            .putFloat(Preferences.KEY_WEIGHT, userInfo.weight)
            .putInt(Preferences.KEY_HEIGHT, userInfo.height)
            .putString(Preferences.KEY_ACTIVITY_LEVEL, userInfo.activityLevel.name)
            .putString(Preferences.KEY_GOAL_TYPE, userInfo.goalType.name)
            .putFloat(Preferences.KEY_CARB_RATIO, userInfo.carbRatio)
            .putFloat(Preferences.KEY_PROTEIN_RATIO, userInfo.proteinRatio)
            .putFloat(Preferences.KEY_FAT_RATIO, userInfo.fatRatio)
            .putString(Preferences.KEY_FIREBASE_USER_INFO_ID, userID)
            .apply()

    }

    override fun saveAuthKey(auth_key: String, auth_key_exp: Long) {
        sharedPref.edit()
            .putString(Preferences.KEY_AUTH, auth_key)
            .putLong(Preferences.KEY_AUTH_KEY_EXP, auth_key_exp)
            .apply()
    }

    override fun loadShouldCreateAuthKey(): Boolean {
        return sharedPref.getBoolean(
            Preferences.KEY_SHOULD_CREATE_AUTH_KEY,
            true
        )
    }

    override fun loadAuthTokenInfo(): AuthTokenInfo {
        val auth_key = sharedPref.getString(Preferences.KEY_AUTH, null)
        val auth_key_exp = sharedPref.getLong(Preferences.KEY_AUTH_KEY_EXP, -1)
        return AuthTokenInfo(
            auth_key!!,
            auth_key_exp
        )
    }

    override fun saveShouldGenAuthToken(shouldGenAuthToken: Boolean) {
        sharedPref.edit()
            .putBoolean(Preferences.KEY_SHOULD_CREATE_AUTH_KEY, shouldGenAuthToken)
            .apply()
    }

    override fun shouldGenAuthToken(): Boolean {
        return sharedPref.getBoolean(
            Preferences.KEY_SHOULD_CREATE_AUTH_KEY,
            true
        )
    }

    override fun saveShouldShowOnboarding(shouldShow: Boolean) {
        sharedPref.edit()
            .putBoolean(Preferences.KEY_SHOULD_SHOW_ONBOARDING, shouldShow)
            .apply()
    }

    override fun loadShouldShowOnboarding(): Boolean {
        return sharedPref.getBoolean(
            Preferences.KEY_SHOULD_SHOW_ONBOARDING,
            true
        )
    }

    override fun newTrackedExercise(trackedExercise: TrackedExercise) {
        sharedPref.edit()
            .putInt(Preferences.KEY_ROW_ID, trackedExercise.rowId)
            .putString(Preferences.KEY_EXERCISE_ID, trackedExercise.id!!)
            .putString(Preferences.KEY_EXERCISE_NAME, trackedExercise.name)
            .putInt(Preferences.KEY_EXERCISE_BASE, trackedExercise.exerciseBase)
            .putString(Preferences.KEY_EXERCISE_DESCRIPTION, trackedExercise.description)
            .putString(Preferences.KEY_EXERCISE_MUSCLES, trackedExercise.muscles)
            .putString(Preferences.KEY_EXERCISE_MUSCLES_SECONDARY, trackedExercise.muscles_secondary)
            .putString(Preferences.KEY_EXERCISE_EQUIPMENT, trackedExercise.equipment)
            .putStringSet(Preferences.KEY_EXERCISE_IMAGE_URL, trackedExercise.image_url)
            .putString(Preferences.KEY_EXERCISE_IS_MAIN, trackedExercise.is_main)
            .putString(Preferences.KEY_EXERCISE_IS_FRONT, trackedExercise.is_front)
            .putString(Preferences.KEY_EXERCISE_MUSCLE_NAME_MAIN, trackedExercise.muscle_name_main)
            .putStringSet(Preferences.KEY_EXERCISE_IMAGE_URL_MAIN, trackedExercise.image_url_main)
            .putStringSet(Preferences.KEY_EXERCISE_IMAGE_URL_SECONDARY, trackedExercise.image_url_secondary)
            .putString(Preferences.KEY_EXERCISE_MUSCLE_NAME_SECONDARY, trackedExercise.muscle_name_secondary)
            .apply()
    }

    override fun loadTrackedExercise(): TrackedExercise {
        val rowId = sharedPref.getInt(Preferences.KEY_ROW_ID, -1)
        val id = sharedPref.getString(Preferences.KEY_EXERCISE_ID, "")
        val name = sharedPref.getString(Preferences.KEY_EXERCISE_NAME, "")
        val exerciseBase = sharedPref.getInt(Preferences.KEY_EXERCISE_BASE, -1)
        val description = sharedPref.getString(Preferences.KEY_EXERCISE_DESCRIPTION, "")
        val muscles = sharedPref.getString(Preferences.KEY_EXERCISE_MUSCLES, "")
        val muscles_secondary = sharedPref.getString(Preferences.KEY_EXERCISE_MUSCLES_SECONDARY, "")
        val equipment = sharedPref.getString(Preferences.KEY_EXERCISE_EQUIPMENT, "")
        val image_url = sharedPref.getStringSet(Preferences.KEY_EXERCISE_IMAGE_URL, emptySet())
        val is_main = sharedPref.getString(Preferences.KEY_EXERCISE_IS_MAIN, "")
        val is_front = sharedPref.getString(Preferences.KEY_EXERCISE_IS_FRONT, "")
        val muscle_name_main = sharedPref.getString(Preferences.KEY_EXERCISE_MUSCLE_NAME_MAIN, "")
        val image_url_main = sharedPref.getStringSet(Preferences.KEY_EXERCISE_IMAGE_URL_MAIN, emptySet())
        val image_url_secondary = sharedPref.getStringSet(Preferences.KEY_EXERCISE_IMAGE_URL_SECONDARY, emptySet())
        val muscle_name_secondary = sharedPref.getString(Preferences.KEY_EXERCISE_MUSCLE_NAME_SECONDARY, "")

        return TrackedExercise(
            rowId = rowId,
            id = id,
            name = name!!,
            exerciseBase = exerciseBase,
            description = description,
            muscles = muscles,
            muscles_secondary = muscles_secondary,
            equipment = equipment,
            image_url = image_url?.toSet() ?: emptySet(),
            is_main = is_main,
            is_front = is_front,
            muscle_name_main = muscle_name_main,
            image_url_main = image_url_main?.toSet() ?: emptySet(),
            image_url_secondary = image_url_secondary?.toSet() ?: emptySet(),
            muscle_name_secondary = muscle_name_secondary
        )
    }

    override fun removeTrackedExercise() {
        sharedPref.edit()
            .remove(Preferences.KEY_ROW_ID)
            .remove(Preferences.KEY_EXERCISE_ID)
            .remove(Preferences.KEY_EXERCISE_NAME)
            .remove(Preferences.KEY_EXERCISE_BASE)
            .remove(Preferences.KEY_EXERCISE_DESCRIPTION)
            .remove(Preferences.KEY_EXERCISE_MUSCLES)
            .remove(Preferences.KEY_EXERCISE_MUSCLES_SECONDARY)
            .remove(Preferences.KEY_EXERCISE_EQUIPMENT)
            .remove(Preferences.KEY_EXERCISE_IMAGE_URL)
            .remove(Preferences.KEY_EXERCISE_IS_MAIN)
            .remove(Preferences.KEY_EXERCISE_IS_FRONT)
            .remove(Preferences.KEY_EXERCISE_MUSCLE_NAME_MAIN)
            .remove(Preferences.KEY_EXERCISE_IMAGE_URL_MAIN)
            .remove(Preferences.KEY_EXERCISE_IMAGE_URL_SECONDARY)
            .remove(Preferences.KEY_EXERCISE_MUSCLE_NAME_SECONDARY)
            .apply()
    }
}