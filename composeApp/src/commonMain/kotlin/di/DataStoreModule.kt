package com.trivaris.votechain.di

import com.trivaris.votechain.models.datastore.DataStoreRepository
import com.trivaris.votechain.viewmodels.SettingsViewModel
import org.koin.dsl.module

val dataStoreModule = module {
    single { DataStoreRepository() }
    factory { SettingsViewModel(get()) }
}