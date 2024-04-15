package com.example.testtaskps.modules

import com.example.testtaskps.rules.ExchangeProvider
import com.example.testtaskps.rules.ExchangeProviderImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ExchangeModule {

    @Singleton
    @Binds
    abstract fun bindExchangeProvider(
        exchangeProviderImpl: ExchangeProviderImpl
    ) : ExchangeProvider

}