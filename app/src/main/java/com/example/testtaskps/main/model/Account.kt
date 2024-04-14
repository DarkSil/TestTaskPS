package com.example.testtaskps.main.model

import com.example.testtaskps.main.AccountsAdapter

data class Account(
    val name: String,
    var amount: Float,
    val clickListener: AccountsAdapter.ItemClickListener,
    var isSelected: Boolean = false
)
