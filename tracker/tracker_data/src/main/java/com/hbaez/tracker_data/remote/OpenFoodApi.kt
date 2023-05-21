package com.hbaez.tracker_data.remote

import com.hbaez.tracker_data.remote.dto.AuthToken
import com.hbaez.tracker_data.remote.dto.SearchDto
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenFoodApi {

    @GET("${SEARCH_FOOD_URL}/?")
    suspend fun searchFood(
        @Query("search_expression") search_expression: String,
        @Query("auth_token") auth_token: String
    ): SearchDto

    @GET(TOKEN_URL)
    suspend fun createAuthKey(): AuthToken


    companion object {
        const val TOKEN_URL = "https://refresh-token-f7zbsbirla-uc.a.run.app"
        const val SEARCH_FOOD_URL = "https://search-food-f7zbsbirla-uc.a.run.app"
        const val BASE_URL = "https://search-food-f7zbsbirla-uc.a.run.app"
    }
}