package com.example.testtaskps.main

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.testtaskps.R
import com.example.testtaskps.databinding.ItemAccountBinding
import com.example.testtaskps.main.model.Account
import kotlin.math.roundToInt

class AccountsAdapter (
    private val list : ArrayList<Account>
) : Adapter<AccountsAdapter.AccountViewHolder>() {

    fun interface ItemClickListener {
        fun itemClick(position: Int)
    }

    class AccountViewHolder(val binding: ItemAccountBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountViewHolder {
        return AccountViewHolder(
            ItemAccountBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: AccountViewHolder, position: Int) {
        holder.binding.textTitle.text = list[position].name
        val amount = (list[position].amount * 100f).roundToInt()/100f
        holder.binding.textAmount.text = amount.toString()
        if (list[position].isSelected) {
            holder.binding.linearRoot.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(holder.itemView.context, R.color.white)
            )
        } else {
            holder.binding.linearRoot.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(holder.itemView.context, R.color.white50)
            )
        }

        holder.binding.root.setOnClickListener {
            if (!list[position].isSelected) {
                list[position].clickListener.itemClick(position)
            }
        }
    }

}