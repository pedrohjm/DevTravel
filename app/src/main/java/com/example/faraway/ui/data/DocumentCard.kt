package com.example.faraway.ui.data

data class DocumentItem(
    val title: String,
    val status: DocumentStatus,
    val date: String? = null,
    val size: String? = null,
    val reason: String? = null
)

enum class DocumentStatus {
    AUSENTE, REJEITADO, APROVADO
}
