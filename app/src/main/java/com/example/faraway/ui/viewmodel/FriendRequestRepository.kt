package com.example.faraway.ui.viewmodel

import com.example.faraway.ui.data.FriendRequestDocument
import com.example.faraway.utils.Resource
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class FriendRequestRepository(private val firestore: FirebaseFirestore) {

    fun getFriendRequests(): Flow<Resource<List<FriendRequestDocument>>> = flow {
        emit(Resource.Loading())
        try {
            val snapshot = firestore.collection("friendRequests")
                .get()
                .await()

            val requests = snapshot.documents.mapNotNull { document ->
                document.toObject(FriendRequestDocument::class.java)?.copy(
                    id = document.id
                )
            }
            emit(Resource.Success(requests))
        } catch (e: Exception) {
            emit(Resource.Error("Falha ao buscar solicitações: ${e.localizedMessage}"))
        }
    }
}