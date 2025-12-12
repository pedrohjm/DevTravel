package com.example.faraway.ui.viewmodel

import com.example.faraway.ui.data.Tour
import com.example.faraway.utils.Resource
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class TourRepository(private val firestore: FirebaseFirestore) {

    fun getTours(): Flow<Resource<List<Tour>>> = flow {
        emit(Resource.Loading())
        try {
            val snapshot = firestore.collection("tours")
                .get()
                .await()

            val tours = snapshot.documents.mapNotNull { document ->
                document.toObject(Tour::class.java)?.copy(
                    id = document.id
                )
            }
            emit(Resource.Success(tours))
        } catch (e: Exception) {
            emit(Resource.Error("Falha ao buscar tours: ${e.localizedMessage}"))
        }
    }
}