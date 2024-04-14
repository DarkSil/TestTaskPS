package com.example.testtaskps.main.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Transaction(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val viewType: ViewType,
    val date: Long,
    val transactionType: TransactionType? = null,
    val amount: Float? = null,
    val fromTo: String? = null,
    val currentAccount: String? = null
) {
    enum class ViewType {
        DATE,
        TRANSACTION
    }

    enum class TransactionType {
        INCOME,
        OUTCOME,
        FAIL
    }
}
