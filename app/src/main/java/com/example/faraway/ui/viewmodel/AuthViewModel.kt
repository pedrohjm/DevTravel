package com.example.faraway.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.example.faraway.ui.data.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.lang.Exception

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {

    // Enum para o estado de autenticação (usado pela UI)
    sealed class AuthState {
        object Idle : AuthState()
        object Loading : AuthState()
        data class Success(val role: String) : AuthState() // Expõe o papel do usuário
        data class Error(val message: String) : AuthState()
    }

    // Estado da UI: Carregando, Sucesso, Erro
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    // Estado do Papel do Usuário (para uso em outras partes do app)
    private val _userRole = MutableStateFlow<String?>(null)
    val userRole: StateFlow<String?> = _userRole.asStateFlow()

    /**
     * Função de Cadastro
     */
    fun register(email: String, password: String, role: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                // A função 'register' já é suspend, então a coroutine espera ela terminar.
                repository.register(email, password, role)

                val user: FirebaseUser? = repository.currentUser
                if (user != null) {
                    // A função 'getUserRole' também é suspend.
                    val fetchedRole = repository.getUserRole(user.uid)
                    _authState.value = AuthState.Success(fetchedRole)
                } else {
                    _authState.value = AuthState.Error("Falha na autenticação. Usuário não encontrado após registro.")
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Erro desconhecido no cadastro")
            }
        }
    }

    /**
     * Função de Login
     */
    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                // A função 'login' também é suspend.
                repository.login(email, password)

                val user: FirebaseUser? = repository.currentUser
                if (user != null) {
                    val fetchedRole = repository.getUserRole(user.uid)
                    _authState.value = AuthState.Success(fetchedRole)
                } else {
                    _authState.value = AuthState.Error("Falha na autenticação. Usuário não encontrado após login.")
                }
            } catch (e: Exception) {
                // Captura erros como senha incorreta, usuário não existe, etc.
                _authState.value = AuthState.Error(e.message ?: "E-mail ou senha inválidos")
            }
        }
    }

    /**
     * Função para verificar o estado inicial (usada na MainActivity)
     */
    fun checkAuthStatus() {
        val user: FirebaseUser? = repository.currentUser
        if (user != null) {
            viewModelScope.launch {
                try {
                    val fetchedRole = repository.getUserRole(user.uid)
                    _userRole.value = fetchedRole
                } catch (e: Exception) {
                    // O usuário está logado no Auth, mas não tem documento no Firestore.
                    // É um estado inconsistente, então deslogamos para forçar um novo login.
                    repository.logout()
                    _userRole.value = null
                }
            }
        }
    }
}
