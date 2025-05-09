package com.example.bachatbox.data.repository

import androidx.lifecycle.LiveData
import com.example.bachatbox.data.db.TransactionDao
import com.example.bachatbox.data.model.Transaction
import javax.inject.Inject

class TransactionRepository @Inject constructor(private val transactionDao: TransactionDao) {

    val getAllTransaction: LiveData<List<Transaction>> =  transactionDao.getAll()

    val getTotalEarn: LiveData<Int> = transactionDao.getTotalEarn()
    val getTotalSpend: LiveData<Int> = transactionDao.getTotalSpend()

    fun insertTransaction(transaction: Transaction) = transactionDao.insert(transaction)

    fun getTransactionsByType(type: String): LiveData<List<Transaction>> {
        return transactionDao.getTransactionsByType(type)
    }
}
