package com.example.bachatbox.di

import android.content.Context
import androidx.room.Room
import com.example.bachatbox.data.db.TransactionDao
import com.example.bachatbox.data.db.TransactionDatabase
import com.example.bachatbox.data.db.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TransactionModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): TransactionDatabase {
        return Room.databaseBuilder(context, TransactionDatabase::class.java, "transaction_db")
            .fallbackToDestructiveMigration().allowMainThreadQueries().build()
    }

    @Provides
    fun provideDao(database: TransactionDatabase) : TransactionDao {
        return database.transactionDao()
    }

    @Provides
    fun provideUserDao(database: TransactionDatabase) : UserDao {
        return database.userDao()
    }

}