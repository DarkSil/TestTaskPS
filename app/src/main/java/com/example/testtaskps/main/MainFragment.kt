package com.example.testtaskps.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testtaskps.databinding.FragmentMainBinding
import com.example.testtaskps.main.model.Account
import com.example.testtaskps.main.model.Transaction
import com.example.testtaskps.services.model.RatesData
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : Fragment() {

    @Inject lateinit var ratesData: RatesData

    private val binding by lazy { FragmentMainBinding.inflate(layoutInflater) }
    private val listOfTransactions = arrayListOf<Transaction>()
    private val listOfAccounts by lazy { arrayListOf<Account>() }
    private val adapterTransactions by lazy { TransactionsAdapter(listOfTransactions) }
    private val adapterAccounts by lazy { AccountsAdapter(listOfAccounts) }

    private val clickListener = AccountsAdapter.ItemClickListener {
        listOfAccounts.filterIndexed { index, account -> account.isSelected || index == it }.forEach {
            val position = listOfAccounts.indexOf(it)
            it.isSelected = !it.isSelected
            adapterAccounts.notifyItemChanged(position)
        }

        //TODO Get list of transactions
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerAccounts.layoutManager = LinearLayoutManager(requireContext()).apply {
            orientation = LinearLayoutManager.HORIZONTAL
        }
        binding.recyclerAccounts.adapter = adapterAccounts

        listOfAccounts.add(Account(ratesData.getBaseCurrency(), 1000.123f, clickListener, true))
        ratesData.getMapOfRates().forEach {
            listOfAccounts.add(Account(it.key, 0f, clickListener))
        }
        listOfAccounts.sortByDescending { it.amount }

        binding.recyclerTransactions.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerTransactions.adapter = adapterTransactions

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
        adapterTransactions.notifyItemRangeInserted(0, listOfTransactions.size)
    }

}