package com.example.testtaskps.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testtaskps.databinding.FragmentMainBinding
import com.example.testtaskps.main.model.Transaction

class MainFragment : Fragment() {

    private val binding by lazy { FragmentMainBinding.inflate(layoutInflater) }
    private val listOfTransactions = arrayListOf<Transaction>()
    private val adapter by lazy { TransactionsAdapter(listOfTransactions) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerTransactions.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerTransactions.adapter = adapter

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
        adapter.notifyItemRangeInserted(0, listOfTransactions.size)
    }

}