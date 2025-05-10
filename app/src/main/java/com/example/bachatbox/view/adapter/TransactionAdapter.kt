package com.example.bachatbox.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bachatbox.R
import com.example.bachatbox.data.model.Transaction
import com.example.bachatbox.databinding.ItemDesignBinding

class TransactionAdapter(private var transactions: List<Transaction>) :
    RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newTransactions: List<Transaction>) {
        this.transactions = newTransactions
        notifyDataSetChanged()
    }

    inner class TransactionViewHolder(val binding: ItemDesignBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TransactionViewHolder {
        val binding = ItemDesignBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TransactionViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: TransactionViewHolder,
        position: Int
    ) {
        val transaction = transactions[position]
        val b = holder.binding

        b.category.text = transaction.category
        b.date.text = transaction.transactionDate
        b.amount.text =
            if (transaction.type == "Spend") "-$${transaction.amount}" else "+$${transaction.amount}"
        b.iconCate.setImageResource(getCategoryImage(transaction.category))
    }

    override fun getItemCount(): Int = transactions.size

    private fun getCategoryImage(category: String): Int {
        return when (category) {
            "Travel" -> R.drawable.travel
            "Entertainment" -> R.drawable.entertainment
            "Real-Estate" -> R.drawable.real_estate
            "Shopping" -> R.drawable.shopping
            "Food" -> R.drawable.food
            "Transport" -> R.drawable.transport
            "Health" -> R.drawable.health
            "Salary" -> R.drawable.salary
            "Gift" -> R.drawable.gift
            else -> R.drawable.other
        }
    }
}