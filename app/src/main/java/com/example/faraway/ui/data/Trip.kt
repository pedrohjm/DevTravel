package com.example.faraway.ui.data

import com.google.firebase.firestore.DocumentId

data class Trip(
    @DocumentId
    val id: String = "", // ID da solicitação
    val partnerName: String = "", // Nome do Guia (injetado no ViewModel)
    val location: String = "",
    val date: String = "",
    val time: String = "",
    val price: String = "",
    val duration: String = "",
    val status: TripStatus = TripStatus.PENDING,
    val imageUrl: String = "", // URL da foto do Guia (injetado no ViewModel)
    val details: String? = null
)

enum class TripStatus {
    CONFIRMED,
    PENDING,
    CANCELED,
    COMPLETED
}

// Função de mapeamento para TripScreen (Viajante)
fun FriendRequestDocument.toTrip(): Trip {
    val requestStatus = RequestStatus.fromString(this.status)
    val tripStatus = when (requestStatus) {
        RequestStatus.ACCEPTED -> TripStatus.CONFIRMED
        RequestStatus.REJECTED, RequestStatus.CANCELED -> TripStatus.CANCELED
        else -> TripStatus.PENDING
    }

    return Trip(
        id = this.id,
        partnerName = this.partnerName ?: "Guia Desconhecido",
        location = this.location ?: "",
        date = this.date ?: "",
        time = this.time ?: "",
        price = this.price ?: "",
        duration = this.duration ?: "",
        status = tripStatus,
        imageUrl = "", // Valor temporário
        details = null
    )
}