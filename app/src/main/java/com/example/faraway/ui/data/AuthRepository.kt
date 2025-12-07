package com.example.faraway.ui.data

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.PropertyName
import kotlinx.coroutines.tasks.await
import java.util.Date

/**
 * Repositório para lidar com todas as operações de autenticação e dados do usuário.
 *
 * É a única fonte de verdade para os dados de autenticação, comunicando-se com o Firebase.
 * As funções de rede (login, register, getUserRole) são 'suspend' para serem chamadas
 * de forma segura a partir de uma Coroutine.
 */

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
    @get:PropertyName("createdAt") val createdAt: Date? = null
)

class AuthRepository {

    // Instância do Firebase Authentication. 'private' para não ser acessível de fora.
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    // Instância do Cloud Firestore para salvar/ler dados do usuário (como a 'role').
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    /**
     * Retorna o usuário atualmente logado, ou nulo se não houver ninguém.
     */
    val currentUser: FirebaseUser?
        get() = mAuth.currentUser

    /**
     * Tenta autenticar um usuário com email e senha.
     * A função é 'suspend' porque a chamada de rede pode demorar.
     * @return AuthResult do Firebase em caso de sucesso.
     * @throws Exception em caso de falha (senha errada, usuário não existe, etc.).
     */
    suspend fun login(email: String, password: String): AuthResult {
        return mAuth.signInWithEmailAndPassword(email, password).await()
    }

    /**
     * Registra um novo usuário com email, senha e um papel (role).
     * Se a criação do usuário no Auth for bem-sucedida, salva os dados dele
     * (incluindo a 'role') em um novo documento no Firestore.
     * @throws Exception em caso de falha.
     */
    suspend fun register(email: String, password: String, newUser: User) {
        // 1. Cria o usuário no Firebase Authentication
        val authResult = mAuth.createUserWithEmailAndPassword(email, password).await()
        val firebaseUser = authResult.user
            ?: throw IllegalStateException("Usuário nulo após o registro, isso não deveria acontecer.")

        // 2. Cria o objeto User completo para salvar no Firestore
        val userToSave = newUser.copy(
            uid = firebaseUser.uid,
            email = email,

        )

        // 3. Salva o documento no Firestore na coleção "users" com o ID do usuário
        db.collection("users").document(firebaseUser.uid).set(
            userToSave.copy(createdAt = null) // Garante que o objeto não tenha FieldValue
        ).await()
    }

    /**
     * Busca o papel ('role') de um usuário no Firestore.
     * @param uid O ID único do usuário.
     * @return A string da 'role' (ex: "Guia", "Anfitrião").
     * @throws Exception se o documento do usuário não for encontrado ou não tiver o campo 'role'.
     */
    suspend fun getUserRole(uid: String): String {
        val document = db.collection("users").document(uid).get().await()

        return if (document.exists()) {
            document.getString("role")
                ?: throw IllegalStateException("Usuário existe, mas o campo 'role' não foi encontrado no Firestore.")
        } else {
            throw IllegalStateException("Documento do usuário com UID $uid não encontrado no Firestore.")
        }
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
     * Desconecta o usuário atual.
     */
    fun logout() {
        mAuth.signOut()
    }
}
