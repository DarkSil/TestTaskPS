package com.example.testtaskps.main.transfer

import android.os.Bundle
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.testtaskps.R
import com.example.testtaskps.databinding.DialogTransferBinding
import com.example.testtaskps.main.MainViewModel
import com.example.testtaskps.rules.ExchangeProvider
import com.example.testtaskps.services.model.RatesData
import com.example.testtaskps.utils.Event
import com.example.testtaskps.utils.ServiceUtil.launchRefreshService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class TransferDialog : DialogFragment() {

    private val binding by lazy { DialogTransferBinding.inflate(layoutInflater) }
    private val viewModel: MainViewModel by viewModels()

    private var callback: MainViewModel.TransferCallback? = null
    private var account: String? = null

    @Inject lateinit var ratesData: RatesData
    @Inject lateinit var exchangeProvider: ExchangeProvider

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
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

        account?.let { account ->

            var amount = 0f
            var to = ""

            binding.textTitle.text = getString(R.string.transferAccount)
                .replace("{account}", account)

            val filters = arrayListOf(
                InputFilter { source, start, end, dest, dstart, dend ->
                    if (dest.length > dest.indexOf(".") + 2 && dest.contains(".")) {
                        return@InputFilter ""
                    } else {
                        null
                    }
                }
            )
            filters.addAll(binding.amountEditText.filters)
            binding.amountEditText.filters = filters.toTypedArray()

            val textFee = getString(R.string.totalWithFee)
            binding.totalText.text = textFee.replace("{amount}", "0 $account")
            binding.amountEditText.addTextChangedListener {
                binding.amountEditText.error = null
                val sum = binding.amountEditText.text.toString().toFloatOrNull() ?: 0f
                val fee = exchangeProvider.calculateFee(sum)
                binding.totalText.text = textFee.replace(
                    "{amount}",
                    "${sum + fee} $account"
                )
            }

            val list = ArrayList(ratesData.getMapOfRates().keys)
            list.removeIf { it == account }
            list.sortBy { it }
            binding.spinnerCurrencies.adapter = ArrayAdapter(
                requireContext(),
                R.layout.item_spinner,
                list
            )

            ratesData.liveData.observe(viewLifecycleOwner, object: Observer<Event<Boolean>>{
                override fun onChanged(value: Event<Boolean>) {
                    if (value.getValueIfNotHandled() == true) {
                        requireActivity().lifecycleScope.launch(Dispatchers.IO) {
                            callback?.let { viewModel.transferMoney(amount, to, account, it) }
                        }
                        ratesData.liveData.removeObserver(this)
                        dismiss()
                    } else if (!value.value) {
                        ratesData.liveData.removeObserver(this)
                        dismiss()
                    }
                }
            })

            binding.textTransfer.setOnClickListener {
                val textAmount = binding.amountEditText.text.toString().toFloatOrNull()
                if (textAmount != null && textAmount > 0) {
                    amount = textAmount
                    to = binding.spinnerCurrencies.selectedItem.toString()
                    binding.textTransfer.isClickable = false
                    requireContext().launchRefreshService(true)
                } else {
                    binding.amountEditText.error = getString(R.string.fieldNotEmty)
                }
            }
        }
    }

    fun setCallback(refreshCallback: MainViewModel.TransferCallback) : TransferDialog {
        callback = refreshCallback
        return this
    }

    fun setCurrentAccount(account: String) : TransferDialog {
        this.account = account
        return this
    }
}