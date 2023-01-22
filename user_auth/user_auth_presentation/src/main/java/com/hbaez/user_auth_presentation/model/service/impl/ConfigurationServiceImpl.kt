package com.hbaez.user_auth_presentation.model.service.impl

import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.hbaez.user_auth_presentation.model.service.ConfigurationService
import com.hbaez.user_auth_presentation.R.xml as AppConfig
import javax.inject.Inject

class ConfigurationServiceImpl @Inject constructor(): ConfigurationService {
    private val remoteConfig
        get() = Firebase.remoteConfig

    init {

        remoteConfig.setDefaultsAsync(AppConfig.remote_config_defaults)
    }

    override suspend fun fetchConfiguration(): Boolean = true

    override val isShowTaskEditButtonConfig: Boolean
        get() = true

    companion object {
        private const val SHOW_TASK_EDIT_BUTTON_KEY = "show_task_edit_button"
        private const val FETCH_CONFIG_TRACE = "fetchConfig"
    }
}