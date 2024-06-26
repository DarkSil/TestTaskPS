package com.example.testtaskps.rules

import com.example.testtaskps.main.model.Transaction
import com.example.testtaskps.services.model.RatesData
import com.example.testtaskps.utils.AppDatabase
import javax.inject.Inject
import kotlin.math.floor

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

    override suspend fun isFeeRequired(account: String): Boolean {
        val count = if (COUNT_TRANSACTIONS_SEPARATELY) {
            db.transactionDao().getTransactionsCountByName(account)
        } else {
            db.transactionDao().getTransactionsCount()
        }

        return count >= FREE_TRANSACTIONS
    }

    override suspend fun calculateFee(transaction: Transaction) : Float {
        var amount = transaction.amount!!
        if (isFeeRequired(transaction.currentAccount!!)) {
            val fee = calculateFee(amount)
            amount += fee
            transaction.fee = fee
        }

        return amount
    }

    override fun calculateFee(amount: Float): Float {
        return floor((amount * FEE_PERCENT / 100) * 100).toFloat() / 100f
    }

}