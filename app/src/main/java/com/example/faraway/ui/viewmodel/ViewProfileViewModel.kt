package com.example.faraway.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.faraway.ui.data.AuthRepository
import com.example.faraway.ui.data.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ViewProfileViewModel(private val repository: AuthRepository) : ViewModel() {

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _requestStatus = MutableStateFlow<String?>(null)
    val requestStatus: StateFlow<String?> = _requestStatus

    fun fetchUser(userId: String) {
        if (userId.isBlank()) return

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val fetchedUser = repository.getUserData(userId)
                _user.value = fetchedUser
            } catch (e: Exception) {
                // Tratar erro de busca
                _user.value = null
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun sendFriendRequest(receiverUid: String) {
        val senderUid = repository.currentUser?.uid ?: return // Não envia se o usuário logado for nulo

        viewModelScope.launch {
            _isLoading.value = true
            _requestStatus.value = "sending"
            try {
                repository.sendFriendRequest(senderUid, receiverUid)
                _requestStatus.value = "sent"
            } catch (e: Exception) {
                _requestStatus.value = "error"
            } finally {
                _isLoading.value = false
            }
        }
    }
}

class ViewProfileViewModelFactory(private val repository: AuthRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ViewProfileViewModel::class.java)) {
            return ViewProfileViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}