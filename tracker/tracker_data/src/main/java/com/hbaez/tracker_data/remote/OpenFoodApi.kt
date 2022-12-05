package com.hbaez.tracker_data.remote

import com.hbaez.tracker_data.remote.dto.AuthToken
import com.hbaez.tracker_data.remote.dto.SearchDto
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenFoodApi {

//    @GET("cgi/search.pl?search_simple=1&json=1&action=process&fields=product_name,nutriments,image_front_thumb_url")
//    suspend fun searchFood(
//        @Query("search_terms") query: String,
//        @Query("page") page: Int,
//        @Query("page_size") pageSize: Int
//    ): SearchDto
//    @GET("search?api_key=&requireAllWords=true")
//    suspend fun searchFood(
//        @Query("query") query: String,
//        @Query("pageNumber") page: Int,
//        @Query("pageSize") pageSize: Int
//    ): SearchDto
//    @GET("parser?app_id=10888af6&app_key=4Bcd3fGh&nutrition-type=logging")
//    suspend fun searchFood(
//        @Query("ingr") query: String
//    ): SearchDto

    @GET("/search_food/?")
    suspend fun searchFood(
        @Query("search_expression") search_expression: String,
        @Query("auth_token") auth_token: String
    ): SearchDto

    @GET("/gen_token")
    suspend fun createAuthKey(): AuthToken



    companion object {
//        const val BASE_URL = "https://us.openfoodfacts.org/"
//        const val BASE_URL = "https://api.nal.usda.gov/fdc/v1/foods/"
//        const val BASE_URL = "https://api.edamam.com/api/food-database/v2/"
        const val BASE_URL = "https://us-central1-workout-buddy-364617.cloudfunctions.net"
    }
}