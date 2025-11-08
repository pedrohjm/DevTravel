package com.example.faraway.ui.data

data class Tour(
    val guestName: String,
    val tourType: String,
    val location: String,
    val date: String,
    val time: String,
    val tourName: String,
    val price: String,
    val status: TourStatus,
    val imageRes: Int,
    val participants: Int
)

enum class TourStatus {
    CONFIRMED,
    PENDING,
    CANCELED
}