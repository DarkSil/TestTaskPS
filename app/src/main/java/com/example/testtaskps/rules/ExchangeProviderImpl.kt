package com.example.testtaskps.rules

import com.example.testtaskps.main.model.Transaction
import com.example.testtaskps.services.model.RatesData
import com.example.testtaskps.utils.AppDatabase
import javax.inject.Inject

class ExchangeProviderImpl @Inject constructor(
    var ratesData: RatesData,
    var db: AppDatabase
) : ExchangeProvider {

    // All the data required to calculate any fee formulas. Feel free to expand

    private val FEE_PERCENT = 0.7
    private val FREE_TRANSACTIONS = 5
    private val COUNT_TRANSACTIONS_SEPARATELY = false

    override suspend fun provideTransaction(transaction: Transaction, balance: Float) : Boolean {
        val amount = calculateFee(transaction)
        return amount <= balance
    }

    override suspend fun calculateFee(transaction: Transaction) : Float {
        val count = if (COUNT_TRANSACTIONS_SEPARATELY) {
            db.transactionDao().getTransactionsCountByName(transaction.currentAccount!!)
        } else {
            db.transactionDao().getTransactionsCount()
        }

        var amount = transaction.amount!!
        if (count >= FREE_TRANSACTIONS) {
            val fee = calculateFee(amount)
            amount += fee
            transaction.fee = fee
        }

        return amount
    }

    override fun calculateFee(amount: Float): Float {
        return amount * FEE_PERCENT.toFloat() / 100
    }

}