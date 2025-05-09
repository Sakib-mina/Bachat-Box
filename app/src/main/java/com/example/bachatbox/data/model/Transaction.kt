package com.example.bachatbox.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "transaction_tb")
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    val transactionId: Int = 0,
    val category: String,
    val name: String,
    val transactionDate: String,
    val amount: Double,
    val type: String,
): Serializable