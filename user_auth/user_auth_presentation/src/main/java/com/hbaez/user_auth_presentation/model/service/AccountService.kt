package com.hbaez.user_auth_presentation.model.service

import com.hbaez.core.domain.model.UserInfo
import com.hbaez.core.domain.preferences.Preferences
import com.hbaez.user_auth_presentation.model.User
import kotlinx.coroutines.flow.Flow

interface AccountService {
    val currentUserId: String
    val hasUser: Boolean

    val currentUser: Flow<User>

    suspend fun authenticate(email: String, password: String)
    suspend fun sendRecoveryEmail(email: String)
    suspend fun createAnonymousAccount()
    suspend fun linkAccount(email: String, password: String)
    suspend fun deleteAccount()
    suspend fun signOut()
}