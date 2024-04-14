package com.example.testtaskps.modules

import com.example.testtaskps.services.model.RatesData
import com.example.testtaskps.services.model.RatesDataImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RatesDataModule {

    @Singleton
    @Provides
    fun bindRatesData() : RatesData {
        return RatesDataImpl()
    }

}