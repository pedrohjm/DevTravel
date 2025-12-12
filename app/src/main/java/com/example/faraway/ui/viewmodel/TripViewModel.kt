package com.example.faraway.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.faraway.ui.data.Trip
import com.example.faraway.ui.data.toTrip
import com.example.faraway.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TripViewModel(
    private val requestRepository: FriendRequestRepository = FriendRequestRepository(FirebaseFirestore.getInstance()),
    private val userRepository: UserRepository = UserRepository(FirebaseFirestore.getInstance())
) : ViewModel() {

    private val _trips = MutableStateFlow<Resource<List<Trip>>>(Resource.Loading())
    val trips: StateFlow<Resource<List<Trip>>> = _trips

    // ID do usuário logado (Viajante)
    private val currentUserId: String? = FirebaseAuth.getInstance().currentUser?.uid

    init {
        fetchTrips()
    }

    fun fetchTrips() {
        if (currentUserId == null) {
            _trips.value = Resource.Error("Usuário não autenticado.")
            return
        }

        viewModelScope.launch {
            requestRepository.getFriendRequests().collect { result ->
                _trips.value = when (result) {
                    is Resource.Loading -> Resource.Loading()
                    is Resource.Error -> Resource.Error(result.message ?: "Erro desconhecido")
                    is Resource.Success -> {
                        // 1. Filtra as solicitações onde o usuário logado é o SENDER (Viajante)
                        val filteredRequests = result.data!!.filter { it.senderUid == currentUserId }

                        // 2. Busca os dados do perfil do Guia (Receiver) e mapeia para Trip
                        val tripsList = filteredRequests.mapNotNull { request ->
                            // Busca o perfil do Guia (Receiver)
                            val guideProfile = userRepository.getUser(request.receiverUid)

                            // Mapeia a solicitação para Trip, injetando os dados do perfil
                            request.toTrip().copy(
                                // CORREÇÃO: Usar apenas o nome do perfil ou um fallback genérico
                                partnerName = "${guideProfile?.firstName} ${guideProfile?.lastName}".trim(),
                                imageUrl = guideProfile?.profileImageUrl ?: ""
                            )
                        }

                        Resource.Success(tripsList)
                    }
                }
            }
        }
    }
}