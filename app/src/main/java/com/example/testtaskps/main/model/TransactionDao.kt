package com.example.testtaskps.main.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TransactionDao {

    @Query("SELECT * FROM [transaction] WHERE currentAccount = :name ORDER BY date ASC")
    fun getTransactionsByName(name: String): List<Transaction>

    @Query("SELECT COUNT(*) FROM [transaction] WHERE transactionType = 'OUTCOME'")
    fun getTransactionsCount(): Int

    @Query("SELECT COUNT(*) FROM [transaction] WHERE currentAccount = :name AND transactionType = 'OUTCOME'")
    fun getTransactionsCountByName(name: String): Int

    @Insert
    fun insertAll(vararg transaction: Transaction)

}