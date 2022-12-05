package com.hbaez.tracker_domain.use_case

import com.hbaez.tracker_domain.model.MealType
import com.hbaez.tracker_domain.model.TrackableFood
import com.hbaez.tracker_domain.model.TrackedFood
import com.hbaez.tracker_domain.repository.TrackerRepository
import java.time.LocalDate
import kotlin.math.roundToInt

class TrackFood(
    private val repository: TrackerRepository
) {

    suspend operator fun invoke(
        food: TrackableFood,
        amount: Float,
        mealType: MealType,
        date: LocalDate,
    ) {
        repository.insertTrackedFood(
            TrackedFood(
                name = food.name,
                carbs = (food.carbs * amount).roundToInt(),
                protein = (food.protein * amount).roundToInt(),
                fat = (food.fat * amount).roundToInt(),
                calories = (food.calories * amount).roundToInt(),
                imageUrl = food.image,
                mealType = mealType,
                amount = amount,
                date = date,
            )
        )
    }
}