package com.hbaez.tracker_domain.use_case

import com.hbaez.core.domain.preferences.Preferences
import com.hbaez.tracker_domain.repository.TrackerRepository

class TrackAuthKey(
    private val repository: TrackerRepository,
    private val preferences: Preferences
) {

    suspend operator fun invoke(): AuthToken{
        return repository.getAuthToken(preferences)
    }

    data class AuthToken(
        val authKey: String,
        val expiration: Long
    )
}