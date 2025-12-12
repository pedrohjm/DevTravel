package com.example.faraway.ui.viewmodel

import com.example.faraway.ui.data.Trip
import com.example.faraway.utils.Resource
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class TripRepository(private val firestore: FirebaseFirestore) {

    fun getTrips(): Flow<Resource<List<Trip>>> = flow {
        emit(Resource.Loading())
        try {
            val snapshot = firestore.collection("trips")
                .get()
                .await()

            val trips = snapshot.documents.mapNotNull { document ->
                document.toObject(Trip::class.java)?.copy(
                    id = document.id
                )
            }
            emit(Resource.Success(trips))
        } catch (e: Exception) {
            emit(Resource.Error("Falha ao buscar viagens: ${e.localizedMessage}"))
        }
    }
}