package com.example.faraway.ui.data

data class Reservation(
    val name: String,
    val type: String,
    val location: String,
    val checkIn: String,
    val checkOut: String,
    val price: String,
    val nights: String,
    val status: ReservationStatus,
    val imageRes: Int,
    val guests: Int
)

enum class ReservationStatus {
    CONFIRMED,
    PENDING,
    CANCELED
}