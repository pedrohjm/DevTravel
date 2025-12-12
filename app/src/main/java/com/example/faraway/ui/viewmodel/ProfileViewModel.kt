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
import android.net.Uri
import com.example.faraway.ui.data.User

class ProfileViewModel(private val repository: AuthRepository) : ViewModel() {

    // Estados para os dados do usuário (usados para inicializar a tela de edição)
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user.asStateFlow()

    // Estados derivados para facilitar o uso na tela de edição
    val userDescription = _user.value?.description
    val userLocation = _user.value?.location // NOVO CAMPO
    val userInterests = _user.value?.interests
    val userLanguages = _user.value?.languages
    val profileImageUrl = MutableStateFlow<String?>(null) // Para a URL da foto

    init {
        fetchCurrentUserProfile()
    }

    fun fetchCurrentUserProfile() { // Alterado de private para public
        viewModelScope.launch {
            val uid = repository.currentUser?.uid
            if (uid != null) {
                try {
                    val currentUser = repository.getUserData(uid)
                    _user.value = currentUser
                    profileImageUrl.value = currentUser.profileImageUrl
                    Log.d("ProfileViewModel", "Perfil do usuário carregado com sucesso.")
                } catch (e: Exception) {
                    Log.e("ProfileViewModel", "Erro ao carregar perfil: ${e.message}", e)
                }
            } else {
                Log.w("ProfileViewModel", "Usuário não autenticado. Não é possível carregar o perfil.")
            }
        }
    }

    fun saveProfile(
        description: String,
        location: String, // NOVO CAMPO
        interests: List<String>,
        languages: List<String>,
        // NOVO: Adiciona a função de callback para notificar o AuthViewModel
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            val uid = repository.currentUser?.uid
            val currentUser = _user.value

            if (uid != null && currentUser != null) {
                try {
                    // 1. Cria um novo objeto User com os dados atualizados
                    val updatedUser = currentUser.copy(
                        description = description,
                        location = location, // NOVO CAMPO
                        interests = interests,
                        languages = languages,
                        profileImageUrl = profileImageUrl.value // Mantém a URL da foto atual
                    )

                    // 2. Salva o documento no Firestore
                    repository.updateUserData(uid, updatedUser)
                    _user.value = updatedUser // Atualiza o estado local
                    Log.d("ProfileViewModel", "Perfil atualizado com sucesso no Firestore.")
                    onSuccess() // Chama o callback de sucesso
                } catch (e: Exception) {
                    Log.e("ProfileViewModel", "Erro ao salvar perfil: ${e.message}", e)
                }
            } else {
                Log.w("ProfileViewModel", "Não foi possível salvar. Usuário não autenticado ou dados ausentes.")
            }
        }
    }

    /**
     * Faz o upload da imagem de perfil e atualiza a URL no Firestore.
     * @param imageUri A URI local da imagem a ser carregada.
     */
    fun uploadProfileImage(imageUri: android.net.Uri, onSuccess: () -> Unit) {
        viewModelScope.launch {
            val uid = repository.currentUser?.uid
            val currentUser = _user.value

            if (uid != null && currentUser != null) {
                try {
                    // 1. Upload para o Firebase Storage e obtenção da URL
                    val newImageUrl = repository.uploadProfileImage(uid, imageUri)

                    // 2. Atualiza o objeto User local e no Firestore
                    val updatedUser = currentUser.copy(profileImageUrl = newImageUrl)
                    repository.updateUserData(uid, updatedUser)

                    // 3. Atualiza os estados reativos
                    _user.value = updatedUser
                    profileImageUrl.value = newImageUrl
                    Log.d("ProfileViewModel", "Foto de perfil atualizada com sucesso: $newImageUrl")
                    onSuccess()

                } catch (e: Exception) {
                    Log.e("ProfileViewModel", "Erro ao fazer upload da foto: ${e.message}", e)
                }
            } else {
                Log.w("ProfileViewModel", "Não foi possível fazer upload. Usuário não autenticado ou dados ausentes.")
            }
        }
    }
}

class ProfileViewModelFactory(private val repository: AuthRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProfileViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}