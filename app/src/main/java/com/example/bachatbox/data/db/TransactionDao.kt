package com.example.bachatbox.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.bachatbox.data.model.Transaction

@Dao
interface TransactionDao {

    @Insert
    fun insert(transaction: Transaction)

    @Query("SELECT * FROM transaction_tb ORDER BY transactionId ASC")
    fun getAll(): LiveData<List<Transaction>>

    @Query("SELECT SUM(amount) FROM transaction_tb WHERE type = 'Earn'")
    fun getTotalEarn(): LiveData<Int>

    @Query("SELECT SUM(amount) FROM transaction_tb WHERE type = 'Spend'")
    fun getTotalSpend(): LiveData<Int>

    @Query("SELECT * FROM transaction_tb WHERE type = :type ORDER BY transactionId DESC")
    fun getTransactionsByType(type: String): LiveData<List<Transaction>>

    @Query("SELECT * FROM transaction_tb WHERE strftime('%m', transactionDate) = :month ORDER BY transactionDate DESC")
    fun getTransactionsByMonth(month: String): LiveData<List<Transaction>>

    @Query("SELECT * FROM transaction_tb WHERE type = :type AND strftime('%m', transactionDate) = :month ORDER BY transactionDate DESC")
    fun getTransactionsByTypeAndMonth(type: String, month: String): LiveData<List<Transaction>>
}
