package com.example.faraway.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.faraway.ui.data.AuthRepository

/**
 * Factory para criar uma instância de AuthViewModel com um AuthRepository.
 *
 * A responsabilidade desta classe é permitir a injeção de dependência no AuthViewModel.
 * Ela ensina ao sistema como construir um AuthViewModel que precisa de um AuthRepository.
 */
@Suppress("UNCHECKED_CAST")
class AuthViewModelFactory(
    private val repository: AuthRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Verifica se a classe que o sistema está tentando criar é a AuthViewModel
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            // Se for, cria e retorna uma nova instância de AuthViewModel,
            // passando o repositório que recebemos no construtor.
            return AuthViewModel(repository) as T
        }
        // Se o sistema tentar usar esta factory para criar um ViewModel diferente,
        // lança uma exceção para indicar que isso é um erro de programação.
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
