package com.hbaez.tracker_data.remote.dto

import com.squareup.moshi.Json

data class AuthToken(
    @field:Json(name = "access_token")
    val access_token: String,
    @field:Json(name = "expires_in")
    val expires_in: Int
)
