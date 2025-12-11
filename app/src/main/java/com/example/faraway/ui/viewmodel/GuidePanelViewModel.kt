package com.example.faraway.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.faraway.ui.data.AuthRepository
import com.example.faraway.ui.data.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class FriendRequest(
    val requestId: String,
    val senderUid: String,
    val receiverUid: String,
    val status: String,
    val timestamp: Long,
    val senderUser: User? = null // Para exibir o nome e foto do remetente
)

class GuidePanelViewModel(private val repository: AuthRepository) : ViewModel() {

    private val _pendingRequests = MutableStateFlow<List<FriendRequest>>(emptyList())
    val pendingRequests: StateFlow<List<FriendRequest>> = _pendingRequests

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        fetchPendingRequests()
    }

    fun fetchPendingRequests() {
        val currentUid = repository.currentUser?.uid ?: return

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val rawRequests = repository.getPendingRequests(currentUid)
                val requestsWithUserData = rawRequests.mapNotNull { rawRequest ->
                    val senderUid = rawRequest["senderUid"] as? String ?: return@mapNotNull null
                    val requestId = "${senderUid}_to_${currentUid}"
                    val senderUser = repository.getUserData(senderUid) // Busca os dados do remetente

                    FriendRequest(
                        requestId = requestId,
                        senderUid = senderUid,
                        receiverUid = currentUid,
                        status = rawRequest["status"] as? String ?: "pending",
                        timestamp = rawRequest["timestamp"] as? Long ?: 0L,
                        senderUser = senderUser
                    )
                }
                _pendingRequests.value = requestsWithUserData
            } catch (e: Exception) {
                // Tratar erro
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateRequestStatus(requestId: String, newStatus: String) {
        viewModelScope.launch {
            try {
                repository.updateRequestStatus(requestId, newStatus)
                // Após a atualização, recarrega a lista
                fetchPendingRequests()
            } catch (e: Exception) {
                // Tratar erro
            }
        }
    }
}

class GuidePanelViewModelFactory(private val repository: AuthRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GuidePanelViewModel::class.java)) {
            return GuidePanelViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}