package com.example.bachatbox.data.repository

import com.example.bachatbox.data.db.UserDao
import com.example.bachatbox.data.model.User
import javax.inject.Inject

class UserRepository @Inject constructor(private val userDao: UserDao) {

    fun insert(user: User) = userDao.insertUser(user)

    fun update(user: User) = userDao.updateUser(user)

    fun logoutUser() {
        userDao.logoutUser()
    }
}