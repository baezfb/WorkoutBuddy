package com.hbaez.tracker_domain.model

data class TrackableFood(
    val name: String,
    val image: String?,
    val calories: Int,
    val carbs: Int,
    val protein: Int,
    val fat: Int,
    val quantity: String
)
