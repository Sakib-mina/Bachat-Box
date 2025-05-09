package com.example.bachatbox.view.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.bachatbox.data.model.User
import com.example.bachatbox.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.concurrent.Executors
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private val repository: UserRepository) : ViewModel(){

    val getAllUser: LiveData<List<User>> = repository.getAllUser

    fun insert(user: User) = repository.insert(user)

    fun update(user: User) = repository.update(user)

    fun logoutUser() {
        Executors.newSingleThreadExecutor().execute {
            repository.logoutUser()
        }
    }
}