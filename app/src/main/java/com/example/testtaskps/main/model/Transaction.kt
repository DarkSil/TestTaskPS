package com.example.testtaskps.main.model

data class Transaction(
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
