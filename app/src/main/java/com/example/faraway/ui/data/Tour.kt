package com.example.faraway.ui.data

import com.google.firebase.firestore.DocumentId

data class Tour(
    @DocumentId
    val id: String = "", // ID da solicitação
    val guestName: String = "", // Nome do Viajante (injetado no ViewModel)
    val tourType: String = "",
    val location: String = "",
    val date: String = "",
    val time: String = "",
    val tourName: String = "",
    val price: String = "",
    val status: TourStatus = TourStatus.PENDING,
    val imageRes: Int = 0,
    val participants: Int = 0,
    val imageUrl: String = "" // URL da foto do Viajante (injetado no ViewModel)
)

enum class TourStatus {
    CONFIRMED,
    PENDING,
    CANCELED,
    COMPLETED
}

// Função de mapeamento para GuideToursScreen (Guia)
fun FriendRequestDocument.toTour(): Tour {
    val requestStatus = RequestStatus.fromString(this.status)
    val tourStatus = when (requestStatus) {
        RequestStatus.ACCEPTED -> TourStatus.CONFIRMED
        RequestStatus.REJECTED, RequestStatus.CANCELED -> TourStatus.CANCELED
        else -> TourStatus.PENDING
    }

    return Tour(
        id = this.id,
        guestName = this.guestName ?: "Viajante Desconhecido", // Valor temporário
        tourType = this.tourType ?: "",
        location = this.location ?: "",
        date = this.date ?: "",
        time = this.time ?: "",
        tourName = this.tourName ?: "",
        price = this.price ?: "",
        status = tourStatus,
        imageRes = this.participants ?: 0,
        participants = this.participants ?: 1,
        imageUrl = "" // Valor temporário
    )
}