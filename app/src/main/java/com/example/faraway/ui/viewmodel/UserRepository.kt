package com.example.faraway.ui.viewmodel

import com.example.faraway.ui.data.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserRepository(private val firestore: FirebaseFirestore) {

    // Busca um único usuário pelo UID
    suspend fun getUser(uid: String): User? {
        return try {
            val document = firestore.collection("users")
                .document(uid)
                .get()
                .await()

            // Assumindo que sua classe User tem os campos corretos para o Firebase
            document.toObject(User::class.java)?.copy(uid = document.id)
        } catch (e: Exception) {
            // Em caso de erro, retorna null para não quebrar a aplicação
            e.printStackTrace()
            null
        }
    }
}