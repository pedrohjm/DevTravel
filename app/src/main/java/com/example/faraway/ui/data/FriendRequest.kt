package com.example.faraway.ui.data

import com.google.firebase.firestore.DocumentId

data class FriendRequestDocument(
    @DocumentId
    val id: String = "",
    val receiverUid: String = "",
    val senderUid: String = "",
    val status: String = "pending",
    val timestamp: Long = 0,
    val location: String? = null,
    val date: String? = null,
    val time: String? = null,
    val price: String? = null,
    val duration: String? = null,
    val partnerName: String? = null,
    val guestName: String? = null,
    val tourType: String? = null,
    val tourName: String? = null,
    val participants: Int? = null
)

enum class RequestStatus {
    PENDING,
    ACCEPTED,
    REJECTED,
    CANCELED;

    companion object {
        fun fromString(status: String?): RequestStatus {
            return when (status?.lowercase()) {
                "accepted" -> ACCEPTED
                "rejected" -> REJECTED
                "canceled" -> CANCELED
                else -> PENDING
            }
        }
    }
}