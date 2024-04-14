package com.example.testtaskps.main

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.lifecycle.ViewModel
import com.example.testtaskps.main.model.Account
import com.example.testtaskps.main.model.Transaction
import com.example.testtaskps.modules.Qualifiers
import com.example.testtaskps.services.model.RatesData
import com.example.testtaskps.utils.AppDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    @Qualifiers.AccountsDataStore
    @Inject lateinit var dataStore: DataStore<Preferences>
    @Inject lateinit var ratesData: RatesData
    @Inject lateinit var db: AppDatabase

    fun interface FetchCallback {
        fun fetch(list: ArrayList<Account>)
    }

    fun interface FetchTransactionsCallback {
        fun fetch(list: ArrayList<Transaction>)
    }

    fun interface TransferCallback {
        fun transfer(transaction: Transaction)
    }

    suspend fun getAccounts(clickListener: AccountsAdapter.ItemClickListener, callback: FetchCallback) {
        val listOfAccounts = arrayListOf<Account>()

        val map = dataStore.data.first().asMap()
        if (map.isEmpty()) {
            dataStore.edit { preferences ->
                preferences[floatPreferencesKey(ratesData.getBaseCurrency())] = 1000f
                ratesData.getMapOfRates().filter { it.key != ratesData.getBaseCurrency() }.forEach { rate ->
                    preferences[floatPreferencesKey(rate.key)] = 0f
                }
            }
        }
        dataStore.data.first().asMap().forEach {
            listOfAccounts.add(Account(it.key.toString(), (it.value as Float), clickListener))
        }

        listOfAccounts.removeIf { !ratesData.getMapOfRates().containsKey(it.name) }

        val list = listOfAccounts.sortedWith(compareByDescending<Account> { it.amount }.thenBy { it.name })
        list.first().isSelected = true

        callback.fetch(ArrayList(list))
    }

    suspend fun getTransactions(transactionCategory: String, callback: FetchTransactionsCallback) {
        val listOfTransactions = arrayListOf<Transaction>()

        listOfTransactions.addAll(db.transactionDao().getTransactionsByName(transactionCategory))

        callback.fetch(listOfTransactions)
    }

    suspend fun transferMoney(amount: Float, to: String, from: String, callback: TransferCallback) {
        val currentBalance = dataStore.data.first().asMap()[floatPreferencesKey(from)] as Float
        if (amount > currentBalance) {
            return
        }
        val rateFrom = ratesData.getMapOfRates()[from]
        val rateTo = ratesData.getMapOfRates()[to]

        if (rateFrom != null && rateTo != null) {
            var recipientAmount = (amount / rateFrom * rateTo)
            recipientAmount = (recipientAmount * 100).roundToInt()/100f

            val date = System.currentTimeMillis()
            val transactionSent = Transaction(
                0,
                Transaction.ViewType.TRANSACTION,
                date,
                Transaction.TransactionType.OUTCOME,
                amount,
                to,
                from
            )

            val transactionReceived = Transaction(
                0,
                Transaction.ViewType.TRANSACTION,
                date,
                Transaction.TransactionType.INCOME,
                recipientAmount,
                from,
                to
            )

            dataStore.edit {
                it[floatPreferencesKey(from)] = currentBalance - amount
                it[floatPreferencesKey(to)] = (it[floatPreferencesKey(to)] as Float) + recipientAmount
            }
            db.transactionDao().insertAll(transactionSent, transactionReceived)
            callback.transfer(transactionSent)
        }
    }

}