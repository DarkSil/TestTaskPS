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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    @Qualifiers.AccountsDataStore
    @Inject lateinit var dataStore: DataStore<Preferences>
    @Inject lateinit var ratesData: RatesData

    fun interface FetchCallback {
        fun fetch(list: ArrayList<Account>)
    }

    fun interface FetchTransactions {
        fun fetch(list: ArrayList<Transaction>)
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

        listOfAccounts.sortByDescending { it.amount }
        listOfAccounts.first().isSelected = true

        callback.fetch(listOfAccounts)
    }

    suspend fun getTransactions(transactionCategory: String, callback: FetchTransactions) {
        val listOfTransactions = arrayListOf<Transaction>()
        listOfTransactions.add(Transaction(Transaction.ViewType.DATE, System.currentTimeMillis()))
        listOfTransactions.add(Transaction(
            Transaction.ViewType.TRANSACTION,
            System.currentTimeMillis(),
            Transaction.TransactionType.INCOME,
            213.0f,
            "USD",
            "EUR"
        ))
        listOfTransactions.add(Transaction(
            Transaction.ViewType.TRANSACTION,
            System.currentTimeMillis(),
            Transaction.TransactionType.OUTCOME,
            213.0f,
            "USD",
            "EUR"
        ))
        listOfTransactions.add(Transaction(
            Transaction.ViewType.TRANSACTION,
            System.currentTimeMillis(),
            Transaction.TransactionType.FAIL,
            213.0f,
            "USD",
            "EUR"
        ))
        listOfTransactions.add(Transaction(
            Transaction.ViewType.TRANSACTION,
            System.currentTimeMillis(),
            Transaction.TransactionType.FAIL,
            213.0f,
            "USD",
            "EUR"
        ))
        listOfTransactions.add(Transaction(
            Transaction.ViewType.TRANSACTION,
            System.currentTimeMillis(),
            Transaction.TransactionType.FAIL,
            213.0f,
            "USD",
            "EUR"
        ))
        listOfTransactions.add(Transaction(
            Transaction.ViewType.TRANSACTION,
            System.currentTimeMillis(),
            Transaction.TransactionType.FAIL,
            213.0f,
            "USD",
            "EUR"
        ))
        listOfTransactions.add(Transaction(
            Transaction.ViewType.TRANSACTION,
            System.currentTimeMillis(),
            Transaction.TransactionType.FAIL,
            213.0f,
            "USD",
            "EUR"
        ))
        listOfTransactions.add(Transaction(
            Transaction.ViewType.TRANSACTION,
            System.currentTimeMillis(),
            Transaction.TransactionType.FAIL,
            213.0f,
            "USD",
            "EUR"
        ))
        callback.fetch(listOfTransactions)
    }

}