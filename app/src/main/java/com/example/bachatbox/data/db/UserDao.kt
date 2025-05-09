package com.example.bachatbox.data.db

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

    @Query("DELETE FROM user")
    fun logoutUser()
}