package com.hbaez.user_auth_presentation.model.service.module

import com.hbaez.user_auth_presentation.model.service.AccountService
import com.hbaez.user_auth_presentation.model.service.ConfigurationService
import com.hbaez.user_auth_presentation.model.service.LogService
import com.hbaez.user_auth_presentation.model.service.StorageService
import com.hbaez.user_auth_presentation.model.service.impl.AccountServiceImpl
import com.hbaez.user_auth_presentation.model.service.impl.LogServiceImpl
import com.hbaez.user_auth_presentation.model.service.impl.StorageServiceImpl
import com.hbaez.user_auth_presentation.model.service.impl.ConfigurationServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ServiceModule {
    @Binds
    abstract fun provideAccountService(impl: AccountServiceImpl): AccountService

    @Binds abstract fun provideLogService(impl: LogServiceImpl): LogService

    @Binds abstract fun provideStorageService(impl: StorageServiceImpl): StorageService

    @Binds
    abstract fun provideConfigurationService(impl: ConfigurationServiceImpl): ConfigurationService
}