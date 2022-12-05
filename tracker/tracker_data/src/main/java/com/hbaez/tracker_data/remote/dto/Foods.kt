package com.hbaez.tracker_data.remote.dto

import com.squareup.moshi.Json

data class Foods(
    val food: List<Food>
)

data class Food(
    @field:Json(name = "brand_name")
    val brandName: String?,
    @field:Json(name = "food_name")
    val name: String,
    @field:Json(name = "food_description")
    val nutrients: String,
    @field:Json(name = "food_url")
    val imageUrl: String?
)

data class Nutrients(
    @field:Json(name = "ENERC_KCAL")
    val calories: Float?,
    @field:Json(name = "PROCNT")
    val protein: Float?,
    @field:Json(name = "FAT")
    val fat: Float?,
    @field:Json(name = "CHOCDF")
    val carbs: Float?,
)

data class ServingSizes(
    @field:Json(name = "label")
    val label: String,
    @field:Json(name = "quantity")
    val quantity: Float,
    @field:Json(name = "servingsPerContainer")
    val servings: Int?,
)
