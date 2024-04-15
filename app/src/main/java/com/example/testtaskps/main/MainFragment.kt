package com.example.testtaskps.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testtaskps.R
import com.example.testtaskps.databinding.FragmentMainBinding
import com.example.testtaskps.main.model.Account
import com.example.testtaskps.main.model.Transaction
import com.example.testtaskps.utils.DefaultErrorDialog
import com.example.testtaskps.utils.DialogUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainFragment : Fragment() {

    private val binding by lazy { FragmentMainBinding.inflate(layoutInflater) }
    private val viewModel: MainViewModel by viewModels()

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

        fetchTransactions()
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

        binding.recyclerTransactions.layoutManager = LinearLayoutManager(requireContext()).apply {
            stackFromEnd = true
            reverseLayout = true
        }
        binding.recyclerTransactions.adapter = adapterTransactions

        lifecycleScope.launch(Dispatchers.IO) {
            viewModel.getAccounts(clickListener) {
                launch(Dispatchers.Main) {
                    listOfAccounts.addAll(it)
                    adapterAccounts.notifyItemRangeInserted(0, it.size)

                    fetchTransactions()
                }
            }
        }

        binding.textTransfer.setOnClickListener {
            DialogUtil.prepareTransferDialog(
                listOfAccounts.first { it.isSelected }.name
            ) { transaction, status ->
                lifecycleScope.launch(Dispatchers.Main) {
                    handleTransaction(transaction, status)
                }
            }
                .show(parentFragmentManager, null)
        }
    }

    private fun handleTransaction(
        transaction: Transaction,
        status: MainViewModel.TransferCallback.STATUS
    ) {
        when (status) {
            is MainViewModel.TransferCallback.STATUS.PASSED -> {
                with(lifecycleScope) {
                    launch(Dispatchers.IO) {
                        viewModel.getAccounts(clickListener) {
                            launch(Dispatchers.Main) {
                                val size = listOfAccounts.size
                                val selectedPosition = listOfAccounts.indexOfFirst { it.isSelected }
                                listOfAccounts.clear()
                                adapterAccounts.notifyItemRangeRemoved(0, size)
                                listOfAccounts.addAll(it)
                                it.first { it.isSelected }.isSelected = false
                                it[selectedPosition].isSelected = true
                                adapterAccounts.notifyItemRangeInserted(0, it.size)
                                binding.recyclerTransactions.smoothScrollToPosition(it.size)
                            }
                        }
                    }
                    launch(Dispatchers.Main) {
                        listOfTransactions.add(transaction)
                        adapterTransactions.notifyItemInserted(listOfTransactions.size - 1)
                    }
                }
            }

            is MainViewModel.TransferCallback.STATUS.FAILED -> {
                val currency = transaction.currentAccount
                val description = getString(R.string.insufficientDescription)
                    .replace(
                        "{amount}",
                        "${transaction.amount.toString()} $currency"
                    )
                    .replace(
                        "{fee}",
                        "${transaction.fee ?: 0} $currency"
                    )
                    .replace(
                        "{balance}",
                        "${status.balance} $currency"
                    )

                DefaultErrorDialog()
                    .setTitle(getString(R.string.insufficientFunds))
                    .setDescription(description)
                    .setType(DefaultErrorDialog.DialogType.OK)
                    .show(parentFragmentManager, null)
            }
        }
    }

    private fun fetchTransactions() {
        lifecycleScope.launch(Dispatchers.IO) {
            viewModel.getTransactions(listOfAccounts.first { it.isSelected }.name) {
                launch(Dispatchers.Main) {
                    val size = listOfTransactions.size
                    listOfTransactions.clear()
                    adapterTransactions.notifyItemRangeRemoved(0, size)
                    listOfTransactions.addAll(it)
                    adapterTransactions.notifyItemRangeInserted(0, it.size)
                }
            }
        }
    }

}