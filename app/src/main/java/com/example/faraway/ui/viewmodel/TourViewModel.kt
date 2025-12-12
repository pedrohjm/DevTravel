package com.example.faraway.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.faraway.ui.data.Tour
import com.example.faraway.ui.data.toTour
import com.example.faraway.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TourViewModel(
    private val requestRepository: FriendRequestRepository = FriendRequestRepository(FirebaseFirestore.getInstance()),
    private val userRepository: UserRepository = UserRepository(FirebaseFirestore.getInstance())
) : ViewModel() {

    private val _tours = MutableStateFlow<Resource<List<Tour>>>(Resource.Loading())
    val tours: StateFlow<Resource<List<Tour>>> = _tours

    // ID do usuário logado (Guia)
    private val currentUserId: String? = FirebaseAuth.getInstance().currentUser?.uid

    init {
        fetchTours()
    }

    fun fetchTours() {
        if (currentUserId == null) {
            _tours.value = Resource.Error("Usuário não autenticado.")
            return
        }

        viewModelScope.launch {
            requestRepository.getFriendRequests().collect { result ->
                _tours.value = when (result) {
                    is Resource.Loading -> Resource.Loading()
                    is Resource.Error -> Resource.Error(result.message ?: "Erro desconhecido")
                    is Resource.Success -> {
                        // 1. Filtra as solicitações onde o usuário logado é o RECEIVER (Guia)
                        val filteredRequests = result.data!!.filter { it.receiverUid == currentUserId }

                        // 2. Busca os dados do perfil do Viajante (Sender) e mapeia para Tour
                        val toursList = filteredRequests.mapNotNull { request ->
                            // Busca o perfil do Viajante (Sender)
                            val travelerProfile = userRepository.getUser(request.senderUid)

                            // Mapeia a solicitação para Tour, injetando os dados do perfil
                            request.toTour().copy(
                                guestName = "${travelerProfile?.firstName} ${travelerProfile?.lastName}".trim(),
                                imageUrl = travelerProfile?.profileImageUrl ?: ""
                            )
                        }

                        Resource.Success(toursList)
                    }
                }
            }
        }
    }
}