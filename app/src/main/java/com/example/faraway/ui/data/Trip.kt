package com.example.faraway.ui.data

data class Trip(
    val partnerName: String,
    val location: String,
    val date: String,
    val time: String,
    val price: String,
    val duration: String,
    val status: TripStatus,
    val imageUrl: String,
    val details: String? = null
)

enum class TripStatus {
    CONFIRMED,
    PENDING,
    CANCELED,
    COMPLETED
}