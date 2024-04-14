package com.example.testtaskps.modules

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

private const val REFRESH_SERVICE = "refresh_service"

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Singleton
    @Provides
    fun provideDataStore(@ApplicationContext appContext: Context) : DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            ReplaceFileCorruptionHandler { emptyPreferences() },
            listOf(SharedPreferencesMigration(appContext, REFRESH_SERVICE)),
            CoroutineScope(Dispatchers.IO + SupervisorJob())
        ) { appContext.preferencesDataStoreFile(REFRESH_SERVICE) }
    }

}