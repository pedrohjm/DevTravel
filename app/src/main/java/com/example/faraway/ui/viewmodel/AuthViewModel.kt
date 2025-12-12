package com.example.faraway.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.example.faraway.ui.data.User
import com.example.faraway.ui.data.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.lang.Exception
import android.util.Log // NOVO: Import para Logcat

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {

    init {
        Log.d("AuthViewModel", "AuthViewModel inicializado.")
        repository.currentUser?.uid?.let { uid ->
            Log.d("AuthViewModel", "Usuário encontrado no init, buscando dados para UID: $uid")
            fetchUserData(uid)
        }
    }

    sealed class AuthState {
        object Idle : AuthState()
        object Loading : AuthState()
        data class Success(val role: String) : AuthState()
        data class Error(val message: String) : AuthState()
    }

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val _userRole = MutableStateFlow<String?>(null)
    val userRole: StateFlow<String?> = _userRole.asStateFlow()

    private val _userData = MutableStateFlow<User?>(null)
    val userData: StateFlow<User?> = _userData.asStateFlow()

    fun register(
        email: String,
        password: String,
        role: String,
        firstName: String,
        lastName: String,
        cpf: String,
        phone: String,
        birthDate: String,
        gender: String,
        otherGender: String?
    ) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val newUser = User(
                    role = role,
                    firstName = firstName,
                    lastName = lastName,
                    cpf = cpf,
                    phone = phone,
                    birthDate = birthDate,
                    gender = gender,
                    otherGender = otherGender
                )

                repository.register(email, password, newUser)

                val user: FirebaseUser? = repository.currentUser
                if (user != null) {
                    _userData.value = null
                    fetchUserData(user.uid)
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
                repository.login(email, password)

                val user: FirebaseUser? = repository.currentUser
                if (user != null) {
                    _userData.value = null
                    fetchUserData(user.uid)
                    val fetchedRole = repository.getUserRole(user.uid)
                    _authState.value = AuthState.Success(fetchedRole)
                } else {
                    _authState.value = AuthState.Error("Falha na autenticação. Usuário não encontrado após login.")
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "E-mail ou senha inválidos")
            }
        }
    }


    fun checkAuthStatus() {
        val user: FirebaseUser? = repository.currentUser
        if (user != null) {
            viewModelScope.launch {
                try {
                    val fetchedRole = repository.getUserRole(user.uid)
                    _userRole.value = fetchedRole
                    fetchUserData(user.uid)
                } catch (e: Exception) {
                    repository.logout()
                    _userRole.value = null
                    _userData.value = null
                }
            }
        }
    }

    fun logout() {
        repository.logout()
        _userRole.value = null
        _userData.value = null
        _authState.value = AuthState.Idle
    }

    fun fetchUserData(uid: String? = repository.currentUser?.uid) {
        if (uid == null) {
            Log.w("AuthViewModel", "fetchUserData chamado com UID nulo")
            return
        }

        Log.d("AuthViewModel", "Iniciando busca de dados para UID: $uid")
        viewModelScope.launch {
            try {
                val user = repository.getUserData(uid)
                _userData.value = user
                Log.d("AuthViewModel", "Dados do usuário carregados com sucesso: ${user.firstName} ${user.lastName}")
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Erro ao buscar dados do usuário com UID $uid: ${e.message}")
                _userData.value = null
            }
        }
    }
}