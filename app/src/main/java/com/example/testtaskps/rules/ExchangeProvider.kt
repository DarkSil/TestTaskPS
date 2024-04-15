package com.example.testtaskps.rules

import com.example.testtaskps.main.model.Transaction

interface ExchangeProvider {
    fun calculateFee(amount: Float): Float
    suspend fun calculateFee(transaction: Transaction): Float
    suspend fun provideTransaction(transaction: Transaction, balance: Float): Boolean
}