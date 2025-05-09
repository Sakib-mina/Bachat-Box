package com.example.bachatbox.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.bachatbox.data.db.TransactionDao
import com.example.bachatbox.data.db.UserDao
import com.example.bachatbox.data.model.Transaction
import com.example.bachatbox.data.model.User

@Database(entities = [Transaction::class, User::class], version = 3, exportSchema = false)
abstract class TransactionDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun userDao(): UserDao
}