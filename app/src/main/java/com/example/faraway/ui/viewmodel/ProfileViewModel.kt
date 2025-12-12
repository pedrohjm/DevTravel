package com.example.faraway.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.faraway.ui.data.AuthRepository
import com.example.faraway.ui.data.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import android.util.Log
import android.net.Uri

class ProfileViewModel(private val repository: AuthRepository) : ViewModel() {

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user.asStateFlow()

    val userDescription = _user.value?.description
    val userLocation = _user.value?.location
    val userInterests = _user.value?.interests
    val userLanguages = _user.value?.languages
    val profileImageUrl = MutableStateFlow<String?>(null)

    init {
        fetchCurrentUserProfile()
    }

    fun fetchCurrentUserProfile() {
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
        location: String,
        interests: List<String>,
        languages: List<String>,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            val uid = repository.currentUser?.uid
            val currentUser = _user.value

            if (uid != null && currentUser != null) {
                try {
                    val updatedUser = currentUser.copy(
                        description = description,
                        location = location,
                        interests = interests,
                        languages = languages,
                        profileImageUrl = profileImageUrl.value
                    )

                    repository.updateUserData(uid, updatedUser)
                    _user.value = updatedUser
                    Log.d("ProfileViewModel", "Perfil atualizado com sucesso no Firestore.")
                    onSuccess()
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
                    val newImageUrl = repository.uploadProfileImage(uid, imageUri)

                    val updatedUser = currentUser.copy(profileImageUrl = newImageUrl)
                    repository.updateUserData(uid, updatedUser)

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