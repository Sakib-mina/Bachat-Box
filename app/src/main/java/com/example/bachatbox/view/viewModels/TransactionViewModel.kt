package com.example.bachatbox.view.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.example.bachatbox.data.model.Transaction
import com.example.bachatbox.data.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(private val repository: TransactionRepository) : ViewModel() {

    val getAllTransaction: LiveData<List<Transaction>> = repository.getAllTransaction

    val totalEarn: LiveData<Int> = repository.getTotalEarn
    val totalSpend: LiveData<Int> = repository.getTotalSpend

    fun insertTransaction(transaction: Transaction) {
        repository.insertTransaction(transaction)
    }

    val balance = MediatorLiveData<Int>().apply {
        var currentEarn = 0
        var currentSpend = 0

        addSource(totalEarn) { earn ->
            currentEarn = earn ?: 0
            value = currentEarn - currentSpend
        }

        addSource(totalSpend) { spend ->
            currentSpend = spend ?: 0
            value = currentEarn - currentSpend
        }
    }

    fun getTransactionsByType(type: String): LiveData<List<Transaction>> {
        return repository.getTransactionsByType(type)
    }

    fun getTransactionsByMonth(monthIndex: Int): LiveData<List<Transaction>> {
        return repository.getTransactionsByMonth(monthIndex)
    }
}
