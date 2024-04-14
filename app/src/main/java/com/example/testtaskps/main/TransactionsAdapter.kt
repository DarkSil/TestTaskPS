package com.example.testtaskps.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
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
        if (holder is TransactionViewHolder) {
            when (item.transactionType) {
                Transaction.TransactionType.INCOME -> {
                    holder.binding.textStatus.text =
                        holder.itemView.context.getString(R.string.received).replace("{item}", item.fromTo.toString())
                    holder.binding.imageTransaction.setImageDrawable(
                        ContextCompat.getDrawable(holder.itemView.context, R.drawable.icon_arrow_income)
                    )
                    holder.binding.textAmount.text = "+${item.amount.toString()} ${item.currentAccount}"
                    holder.binding.textAmount.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.colorGreen))
                    holder.binding.textStatus.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.colorGreen))
                }
                Transaction.TransactionType.OUTCOME -> {
                    holder.binding.textStatus.text =
                        holder.itemView.context.getString(R.string.sent).replace("{item}", item.fromTo.toString())
                    holder.binding.imageTransaction.setImageDrawable(
                        ContextCompat.getDrawable(holder.itemView.context, R.drawable.icon_arrow_outcome)
                    )
                    holder.binding.textAmount.text = "-${item.amount.toString()} ${item.currentAccount}"
                    holder.binding.textAmount.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.colorText))
                    holder.binding.textStatus.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.colorText))
                }
                else -> {
                    holder.binding.textStatus.text =
                        holder.itemView.context.getString(R.string.failed).replace("{item}", item.fromTo.toString())
                    holder.binding.imageTransaction.setImageDrawable(
                        ContextCompat.getDrawable(holder.itemView.context, R.drawable.icon_cross_fail)
                    )
                    holder.binding.textAmount.text = "${item.amount.toString()} ${item.currentAccount}"
                    holder.binding.textAmount.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.colorRed))
                    holder.binding.textStatus.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.colorRed))
                }
            }
            holder.binding.textDate.text = getDate(item.date)
        } else if (holder is DateViewHolder) {
            holder.binding.root.text = getDayAndMonth(item.date)
        }
    }

}