package com.hbaez.tracker_data.remote.repository

import com.hbaez.core.domain.preferences.Preferences
import com.hbaez.tracker_data.local.TrackerDao
import com.hbaez.tracker_data.mapper.toTrackableFood
import com.hbaez.tracker_data.mapper.toTrackedFood
import com.hbaez.tracker_data.mapper.toTrackedFoodEntity
import com.hbaez.tracker_data.remote.OpenFoodApi
import com.hbaez.tracker_domain.model.TrackableFood
import com.hbaez.tracker_domain.model.TrackedFood
import com.hbaez.tracker_domain.repository.TrackerRepository
import com.hbaez.tracker_domain.use_case.TrackAuthKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

class TrackerRepositoryImpl(
    private val dao: TrackerDao,
    private val api: OpenFoodApi
): TrackerRepository {

    override suspend fun searchFood(
        query: String,
        page: Int,
        pageSize: Int,
        auth_key: String
    ): Result<List<TrackableFood>> {
        return try {
            val searchDto = api.searchFood(
                search_expression = query,
                auth_token = auth_key
            )
            Result.success(
                searchDto.foods.food
//                    .filter {
//                        val calculatedCalories = it.nutriments.carbohydrates100g * 4f +
//                                it.nutriments.proteins100g * 4f +
//                                it.nutriments.fat100g * 9f
//                        val lowerBound = calculatedCalories * 0.99f
//                        val upperBound = calculatedCalories * 1.01f
//                        it.nutriments.energyKcal100g in (lowerBound..upperBound)
//                    }
                    .mapNotNull { it.toTrackableFood() }
            )
        } catch(e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override suspend fun insertTrackedFood(food: TrackedFood) {
        dao.insertTrackedFood(food.toTrackedFoodEntity())
    }

    override suspend fun deleteTrackedFood(food: TrackedFood) {
        dao.deleteTrackedFood(food.toTrackedFoodEntity())
    }

    override fun getFoodsForDate(localDate: LocalDate): Flow<List<TrackedFood>> {
        return dao.getFoodsForDate(
            day = localDate.dayOfMonth,
            month = localDate.monthValue,
            year = localDate.year
        ).map { entities ->
            entities.map { it.toTrackedFood() }
        }
    }

    override suspend fun getAuthToken(preferences: Preferences): TrackAuthKey.AuthToken {
        var expiration: Long
        var auth_key: String

        val current = System.currentTimeMillis() / 1000
        val shouldCreateAuthKey = preferences.shouldGenAuthToken()
        if (shouldCreateAuthKey) {
            expiration = current + 82800 // request new token every 12 hours
            val authToken = api.createAuthKey()
            Result.success(
                preferences.saveAuthKey(authToken.access_token, expiration)
            )
            auth_key = authToken.access_token
        } else {
            val authTokenInfo = preferences.loadAuthTokenInfo()
            expiration = authTokenInfo.auth_expiration
            if (current > expiration){
                expiration = current + 82800 // request new token every 12 hours
                val authToken = api.createAuthKey()
                Result.success(
                    preferences.saveAuthKey(authToken.access_token, expiration)
                )
                auth_key = authToken.access_token
            } else {
                auth_key = authTokenInfo.auth_token
            }
        }
        preferences.saveShouldGenAuthToken(false)
        return TrackAuthKey.AuthToken(
            auth_key,
            expiration
        )
    }
}