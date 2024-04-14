package com.example.testtaskps.utils

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.testtaskps.main.model.Transaction
import com.example.testtaskps.main.model.TransactionDao

@Database(entities = [Transaction::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
}