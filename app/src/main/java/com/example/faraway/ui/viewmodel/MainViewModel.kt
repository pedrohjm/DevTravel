package com.example.faraway.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.faraway.ui.data.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import android.util.Log
import com.example.faraway.ui.data.User

class MainViewModel(private val repository: AuthRepository) : ViewModel() {

    private val _guides = MutableStateFlow<List<User>>(emptyList())
    val guides: StateFlow<List<User>> = _guides.asStateFlow()

    private val _hosts = MutableStateFlow<List<User>>(emptyList())
    val hosts: StateFlow<List<User>> = _hosts.asStateFlow()

    private val _friends = MutableStateFlow<List<User>>(emptyList())
    val friends: StateFlow<List<User>> = _friends.asStateFlow()

    init {
        Log.d("MainViewModel", "Iniciando MainViewModel. Chamando fetchUsersByRole().")
        fetchUsersByRole()
    }

    private fun fetchUsersByRole() {
        viewModelScope.launch {
            try {
                Log.d("MainViewModel", "Iniciando busca de Guias no repositório.")
                _guides.value = repository.getUsersByRole("Guia")
                Log.d("MainViewModel", "Busca de Guias concluída. Encontrados: ${_guides.value.size}")

                Log.d("MainViewModel", "Iniciando busca de Anfitriões no repositório.")
                _hosts.value = repository.getUsersByRole("Anfitrião")
                Log.d("MainViewModel", "Busca de Anfitriões concluída. Encontrados: ${_hosts.value.size}")

                Log.d("MainViewModel", "Iniciando busca de Amigos no repositório.")
                _friends.value = repository.getUsersByRole("Amigo")
                Log.d("MainViewModel", "Busca de Amigos concluída. Encontrados: ${_friends.value.size}")
            } catch (e: Exception) {
                Log.e("MainViewModel", "Erro ao buscar usuários por role: ${e.message}", e)
            }
        }
    }
}

class MainViewModelFactory(private val repository: AuthRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
