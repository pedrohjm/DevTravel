package com.example.faraway.ui.data

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.PropertyName
import com.google.firebase.storage.FirebaseStorage
import android.net.Uri
import kotlinx.coroutines.tasks.await
import android.util.Log
import java.util.Date

data class User(
    @get:PropertyName("uid") val uid: String = "",
    @get:PropertyName("email") val email: String = "",
    @get:PropertyName("role") val role: String = "",
    @get:PropertyName("firstName") val firstName: String = "",
    @get:PropertyName("lastName") val lastName: String = "",
    @get:PropertyName("cpf") val cpf: String = "",
    @get:PropertyName("phone") val phone: String = "",
    @get:PropertyName("birthDate") val birthDate: String = "",
    @get:PropertyName("gender") val gender: String = "",
    @get:PropertyName("otherGender") val otherGender: String? = null,
    @get:PropertyName("createdAt") val createdAt: Date? = null,
    @get:PropertyName("description") val description: String? = null,
    @get:PropertyName("location") val location: String? = null,
    @get:PropertyName("languages") val languages: List<String>? = null,
    @get:PropertyName("interests") val interests: List<String>? = null,
    @get:PropertyName("profileImageUrl") val profileImageUrl: String? = null
)

class AuthRepository {

    // Instâncias do Firebase
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val storage: FirebaseStorage = FirebaseStorage.getInstance()

    /**
     * Retorna o usuário atualmente logado, ou nulo se não houver ninguém.
     */
    val currentUser: FirebaseUser?
        get() = mAuth.currentUser

    /**
     * Tenta autenticar um usuário com email e senha.
     */
    suspend fun login(email: String, password: String): AuthResult {
        return mAuth.signInWithEmailAndPassword(email, password).await()
    }

    /**
     * Registra um novo usuário com email, senha e um papel (role).
     */
    suspend fun register(email: String, password: String, newUser: User) {
        val authResult = mAuth.createUserWithEmailAndPassword(email, password).await()
        val firebaseUser = authResult.user
            ?: throw IllegalStateException("Usuário nulo após o registro, isso não deveria acontecer.")

        val userToSave = newUser.copy(
            uid = firebaseUser.uid,
            email = email,
            interests = newUser.interests,
            profileImageUrl = newUser.profileImageUrl
        )

        db.collection("users").document(firebaseUser.uid).set(
            userToSave.copy(createdAt = null)
        ).await()
    }

    suspend fun getUserData(uid: String): User {
        val document = db.collection("users").document(uid).get().await()

        return if (document.exists()) {
            document.toObject(User::class.java)
                ?: throw IllegalStateException("Falha ao converter documento do Firestore para objeto User.")
        } else {
            throw IllegalStateException("Documento do usuário com UID $uid não encontrado no Firestore.")
        }
    }

    /**
     * Atualiza os dados do usuário no Firestore.
     */
    suspend fun updateUserData(uid: String, updatedUser: User) {
        db.collection("users").document(uid).set(updatedUser).await()
    }

    /**
     * Envia uma solicitação de amizade/conexão para outro usuário.
     */
    suspend fun sendFriendRequest(senderUid: String, receiverUid: String) {
        val requestData = hashMapOf(
            "senderUid" to senderUid,
            "receiverUid" to receiverUid,
            "status" to "pending",
            "timestamp" to System.currentTimeMillis()
        )
        // Usa um ID composto para garantir que não haja duplicatas
        val requestId = "${senderUid}_to_${receiverUid}"
        db.collection("friendRequests").document(requestId).set(requestData).await()
    }

    /**
     * Busca solicitações de amizade pendentes para um usuário.
     */
    suspend fun getPendingRequests(receiverUid: String): List<Map<String, Any>> {
        val querySnapshot = db.collection("friendRequests")
            .whereEqualTo("receiverUid", receiverUid)
            .whereEqualTo("status", "pending")
            .get()
            .await()

        return querySnapshot.documents.map { it.data ?: emptyMap() }
    }

    /**
     * Atualiza o status de uma solicitação de amizade.
     */
    suspend fun updateRequestStatus(requestId: String, newStatus: String) {
        db.collection("friendRequests").document(requestId).update("status", newStatus).await()
    }

    /**
     * Faz o upload da imagem de perfil para o Firebase Storage e retorna a URL de download.
     */
    suspend fun uploadProfileImage(uid: String, imageUri: Uri): String {
        // A referência do Storage deve incluir o bucket, se não for o padrão.
        // No entanto, o erro 404 geralmente indica que o bucket não está acessível ou não existe.
        // Vamos tentar usar a referência raiz e o caminho.
        val storageRef = storage.getReference("users/$uid/profile.jpg")

        // 1. Upload do arquivo
        val uploadTask = storageRef.putFile(imageUri).await()

        // 2. Obter a URL de download
        val downloadUrl = uploadTask.storage.downloadUrl.await()

        return downloadUrl.toString()
    }

    suspend fun getUsersByRole(role: String): List<User> {
        try {
            Log.d("AuthRepository", "Buscando usuários com role: $role")
            val querySnapshot = db.collection("users").whereEqualTo("role", role).get().await()
            Log.d("AuthRepository", "Consulta para role '$role' concluída. Documentos encontrados: ${querySnapshot.size()}")
            return querySnapshot.toObjects(User::class.java)
        } catch (e: Exception) {
            Log.e("AuthRepository", "Erro ao buscar usuários por role '$role': ${e.message}", e)
            throw e
        }
    }

    /**
     * Busca o role (papel) do usuário no Firestore.
     */
    suspend fun getUserRole(uid: String): String {
        val document = db.collection("users").document(uid).get().await()
        return if (document.exists()) {
            document.getString("role") ?: ""
        } else {
            throw IllegalStateException("Documento do usuário com UID $uid não encontrado no Firestore.")
        }
    }

    /**
     * Desconecta o usuário atual.
     */
    fun logout() {
        mAuth.signOut()
    }
}