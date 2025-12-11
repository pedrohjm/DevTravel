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
        // Tenta buscar os dados do usuário logado assim que o ViewModel é criado
        repository.currentUser?.uid?.let { uid ->
            Log.d("AuthViewModel", "Usuário encontrado no init, buscando dados para UID: $uid")
            fetchUserData(uid)
        }
    }

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

    // Estado do Objeto User (para exibir nome, etc.)
    private val _userData = MutableStateFlow<User?>(null)
    val userData: StateFlow<User?> = _userData.asStateFlow()

    /**
     * Função de Cadastro
     */
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
                    // A função 'getUserRole' também é suspend.
                    _userData.value = null // Limpa os dados do usuário anterior
                    // NOVO: Chama a busca dos dados completos do usuário
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
                // A função 'login' também é suspend.
                repository.login(email, password)

                val user: FirebaseUser? = repository.currentUser
                if (user != null) {
                    _userData.value = null // Limpa os dados do usuário anterior
                    // NOVO: Chama a busca dos dados completos do usuário
                    fetchUserData(user.uid)
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
                    // Tenta buscar os dados completos do usuário ao verificar o status
                    fetchUserData(user.uid)
                } catch (e: Exception) {
                    // O usuário está logado no Auth, mas não tem documento no Firestore.
                    // É um estado inconsistente, então deslogamos para forçar um novo login.
                    repository.logout()
                    _userRole.value = null
                    _userData.value = null // NOVO: Limpa os dados do usuário no caso de inconsistência
                }
            }
        }
    }

    fun logout() {
        repository.logout()
        _userRole.value = null
        _userData.value = null // Limpa os dados do usuário no logout
        _authState.value = AuthState.Idle
    }

    /**
     * Busca os dados completos do usuário logado.
     */
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
                // NOVO: Adiciona log de erro para diagnóstico
                Log.e("AuthViewModel", "Erro ao buscar dados do usuário com UID $uid: ${e.message}")
                _userData.value = null
            }
        }
    }
}
