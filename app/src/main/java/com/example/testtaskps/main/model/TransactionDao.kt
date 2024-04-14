package com.example.testtaskps.main.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TransactionDao {

    @Query("SELECT * FROM [transaction] WHERE currentAccount = :name")
    fun getTransactionsByName(name: String): List<Transaction>

    @Insert
    fun insertAll(vararg transaction: Transaction)

}