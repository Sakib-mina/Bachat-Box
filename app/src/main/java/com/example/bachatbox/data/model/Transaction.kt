package com.example.bachatbox.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "transaction_tb")
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    val transactionId: Int = 0,
    val category: String,           // e.g. "Food", "Shopping"
    val name: String,               // e.g. "Burger King"
    val transactionDate: String,    // e.g. "2025-04-25"
    val amount: Double,             // e.g. 500.0
    val type: String,

): Serializable