package com.hbaez.tracker_domain.repository

import com.hbaez.core.domain.preferences.Preferences
import com.hbaez.tracker_domain.model.TrackableFood
import com.hbaez.tracker_domain.model.TrackedFood
import com.hbaez.tracker_domain.use_case.TrackAuthKey
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface TrackerRepository {

    suspend fun searchFood(
        query: String,
        page: Int,
        pageSize: Int,
        auth_key: String
    ): Result<List<TrackableFood>>

    suspend fun insertTrackedFood(food: TrackedFood)

    suspend fun deleteTrackedFood(food: TrackedFood)

    fun getFoodsForDate(localDate: LocalDate): Flow<List<TrackedFood>>

    suspend fun getAuthToken(preferences: Preferences): TrackAuthKey.AuthToken
}