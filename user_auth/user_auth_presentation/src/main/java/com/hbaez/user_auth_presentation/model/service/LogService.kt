package com.hbaez.user_auth_presentation.model.service

interface LogService {
    fun logNonFatalCrash(throwable: Throwable)
}