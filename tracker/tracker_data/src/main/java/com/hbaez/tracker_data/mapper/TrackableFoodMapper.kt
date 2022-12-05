package com.hbaez.tracker_data.mapper

import com.hbaez.tracker_data.remote.dto.Food
import com.hbaez.tracker_domain.model.TrackableFood

fun Food.toTrackableFood(): TrackableFood? {
    var carbs = 0
    var protein = 0
    var fat = 0
    var calories = 0

    var nutrientsArray = nutrients.split("-").toTypedArray()[1]
    carbs = nutrientsArray.split('|').toTypedArray()[2].replace(Regex("[^0-9.]"), "").toFloat().toInt()
    protein = nutrientsArray.split('|').toTypedArray()[3].replace(Regex("[^0-9.]"), "").toFloat().toInt()
    fat = nutrientsArray.split('|').toTypedArray()[1].replace(Regex("[^0-9.]"), "").toFloat().toInt()
    calories = nutrientsArray.split('|').toTypedArray()[0].replace(Regex("[^0-9.]"), "").toFloat().toInt()

    val quantity = nutrients.split("-").toTypedArray()[0]

    return TrackableFood(
        name = if (this.brandName != null) this.brandName + " - " + this.name else this.name,
        carbs = carbs,
        protein = protein,
        fat = fat,
        calories = calories,
        image = imageUrl,
        quantity = quantity
    )
}