package com.example.bachatbox.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.bachatbox.data.model.User

@Dao
interface UserDao {

    @Insert
    fun insertUser(user: User)

    @Update
    fun updateUser(user: User)

    @Query("SELECT * FROM user")
    fun getAllUser(): LiveData<List<User>>

    @Query("DELETE FROM user")
    fun logoutUser() // Not suspend, no Coroutine
}