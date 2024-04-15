package com.example.testtaskps.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.testtaskps.R
import com.example.testtaskps.databinding.ItemDateBinding
import com.example.testtaskps.databinding.ItemTransactionBinding
import com.example.testtaskps.main.model.Transaction
import com.example.testtaskps.utils.DateUtil.getDate
import com.example.testtaskps.utils.DateUtil.getDayAndMonth

class TransactionsAdapter (
    private val list: List<Transaction>
) : Adapter<ViewHolder>() {

    fun interface ItemClickListener {
        fun itemClick(account: String)
    }

    class TransactionViewHolder(val binding: ItemTransactionBinding) : ViewHolder(binding.root)
    class DateViewHolder(val binding: ItemDateBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            Transaction.ViewType.DATE.ordinal -> DateViewHolder(
                ItemDateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
            else -> TransactionViewHolder(
                ItemTransactionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (list[position].viewType) {
            Transaction.ViewType.DATE -> Transaction.ViewType.DATE.ordinal
            Transaction.ViewType.TRANSACTION -> Transaction.ViewType.TRANSACTION.ordinal
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        val context = holder.itemView.context
        if (holder is TransactionViewHolder) {
            holder.binding.root.setOnClickListener { itemClickListener?.itemClick(item.fromTo.toString()) }
            holder.binding.textFee.isVisible = false
            when (item.transactionType) {
                Transaction.TransactionType.INCOME -> {
                    holder.binding.textStatus.text = context.getString(R.string.received)
                            .replace("{item}", item.fromTo.toString())

                    holder.binding.imageTransaction.setImageDrawable(
                        ContextCompat.getDrawable(context, R.drawable.icon_arrow_income)
                    )

                    holder.binding.textAmount.text = "+${item.amount} ${item.currentAccount}"
                    holder.binding.textAmount
                        .setTextColor(ContextCompat.getColor(context, R.color.colorGreen))
                    holder.binding.textStatus
                        .setTextColor(ContextCompat.getColor(context, R.color.colorGreen))
                }
                Transaction.TransactionType.OUTCOME -> {
                    holder.binding.textStatus.text =
                        context.getString(R.string.sent).replace("{item}", item.fromTo.toString())

                    holder.binding.imageTransaction.setImageDrawable(
                        ContextCompat.getDrawable(context, R.drawable.icon_arrow_outcome)
                    )

                    holder.binding.textFee.isVisible = item.fee != null
                    holder.binding.textFee.text = context.getString(R.string.feeText)
                            .replace("{fee}", "${item.fee ?: 0f} ${item.currentAccount}")

                    holder.binding.textAmount.text =
                        "-${(item.amount ?: 0f) + (item.fee ?: 0f)} ${item.currentAccount}"
                    holder.binding.textAmount
                        .setTextColor(ContextCompat.getColor(context, R.color.colorText))
                    holder.binding.textStatus
                        .setTextColor(ContextCompat.getColor(context, R.color.colorText))
                }
                else -> {
                    holder.binding.textStatus.text = context.getString(R.string.failed)
                        .replace("{item}", item.fromTo.toString())

                    holder.binding.imageTransaction.setImageDrawable(
                        ContextCompat.getDrawable(context, R.drawable.icon_cross_fail)
                    )

                    holder.binding.textAmount.text = "${item.amount} ${item.currentAccount}"
                    holder.binding.textAmount
                        .setTextColor(ContextCompat.getColor(context, R.color.colorRed))
                    holder.binding.textStatus
                        .setTextColor(ContextCompat.getColor(context, R.color.colorRed))
                }
            }
            holder.binding.textDate.text = getDate(item.date)
        } else if (holder is DateViewHolder) {
            holder.binding.root.text = getDayAndMonth(item.date)
        }
    }

    private var itemClickListener: ItemClickListener? = null

    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListener = itemClickListener
    }
}