package com.example.faraway.ui.data

data class Connection(
    val initials: String,
    val name: String,
    val location: String,
    val isRequest: Boolean = false
)