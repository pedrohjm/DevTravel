package com.example.faraway

data class ChatMessage(
    val name: String,
    val message: String,
    val time: String,
    val unreadCount: Int = 0,
    val isOnline: Boolean = false
)
