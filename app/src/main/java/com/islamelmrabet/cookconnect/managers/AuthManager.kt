package com.islamelmrabet.cookconnect.managers

import android.content.Context
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.islamelmrabet.cookconnect.model.firebaseModels.Worker
import kotlinx.coroutines.tasks.await

/**
 * Sealed class for the result.
 *
 * @param T
 */
sealed class AuthRes<out T> {
    data class Success<T>(val data: T) : AuthRes<T>()
    data class Error(val errorMessage: String) : AuthRes<Nothing>()
}

/**
 * Class AuthManager.
 *
 * Description: This class provides all the methods to signIn, SignUp, and create workers.
 *
 * @property context
 */
class AuthManager(private val context: Context) {
    private val auth: FirebaseAuth by lazy { Firebase.auth }
    private val collectionReference = FirebaseFirestore.getInstance().collection("workers")

    /**
     * Create user
     *
     * @param email
     * @param password
     * @param worker
     * @return
     */
    suspend fun createUserWithEmailAndPassword(
        email: String,
        password: String,
        worker: Worker
    ): AuthRes<FirebaseUser?> {
        return try {
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            createWorker(worker)
            AuthRes.Success(authResult.user)
        } catch (e: Exception) {
            AuthRes.Error(e.message ?: "Error al crear el usuario")
        }
    }

    /**
     * SignIn with FirebaseAuth with email and password.
     *
     * @param email
     * @param password
     * @return
     */
    suspend fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): AuthRes<FirebaseUser?> {
        return try {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            updateLastLoginDate(authResult.user?.uid)
            AuthRes.Success(authResult.user)
        } catch (e: Exception) {
            AuthRes.Error(e.message ?: "Error al iniciar sesión")
        }
    }

    /**
     * Reset password using the email.
     *
     *
     * @param email
     * @return
     */
    suspend fun resetPassword(email: String): AuthRes<Unit> {
        return try {
            auth.sendPasswordResetEmail(email).await()
            AuthRes.Success(Unit)
        } catch (e: Exception) {
            AuthRes.Error(e.message ?: "Error al restablecer la contraseña")
        }
    }

    /**
     * Sign out from Firebase
     *
     */
    fun signOut() {
        auth.signOut()
    }

    /**
     * Create a new worker
     *
     * @param worker
     */
    private fun createWorker(worker: Worker) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            FirebaseFirestore.getInstance().collection("workers")
                .document(userId)
                .set(worker)
                .addOnSuccessListener {
                    Log.d("Worker", "Worker created successfully")
                }.addOnFailureListener { e ->
                    Log.d("Worker", "Error creating worker: ${e.message}")
                }
        } else {
            Log.d("Worker", "Current user is null, unable to create worker")
        }
    }

    /**
     * Update the last worker login in the app
     *
     * @param userId
     */
    private suspend fun updateLastLoginDate(userId: String?) {
        try {
            userId?.let { uid ->
                val userRef = FirebaseFirestore.getInstance().collection("workers").document(uid)
                val userSnapshot = userRef.get().await()
                if (userSnapshot.exists()) {
                    userRef.update("lastLogin", FieldValue.serverTimestamp()).await()
                    Log.d("AuthManager", "Last login date updated successfully")
                } else {
                    Log.e("AuthManager", "User document does not exist for UID: $uid")
                }
            }
        } catch (e: Exception) {
            Log.e("AuthManager", "Error updating last login date: ${e.message}")
        }
    }

    /**
     * Get worker document by the email.
     *
     * @param workerEmail
     * @return WorkerDocument
     */
    suspend fun getWorkerDocumentIdByEmail(workerEmail: String): String? {
        return try {
            val querySnapshot = FirebaseFirestore.getInstance().collection("workers")
                .whereEqualTo("email", workerEmail)
                .get().await()
            if (!querySnapshot.isEmpty) {
                querySnapshot.documents.firstOrNull()?.id
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("EmailManager", "Error getting worker document ID: ${e.message}")
            null
        }
    }

    /**
     * Update password of the worker created in Firebase.
     *
     * @param workerId
     * @param password
     * @return TableRes<Unit>
     */
    fun updatePassword(workerId: String, password: String): TableRes<Unit> {
        return try {
            collectionReference
                .document(workerId)
                .update("password", password)
                .addOnSuccessListener {
                    Log.d("Password", "Password updated successfully")
                }
            TableRes.Success(Unit)
        } catch (e: Exception) {
            TableRes.Error(e.message ?: "Error updating password")
        }
    }

    /**
     * Update the field isANewPassword of the worker created in Firebase in case if he is change it
     *
     * @param workerId
     * @param isNewPassword
     * @return TableRes<Unit>
     */
    fun updateIsANewPasswordWorker(workerId: String, isNewPassword: Boolean): TableRes<Unit> {
        return try {
            collectionReference
                .document(workerId)
                .update("anewPassword", isNewPassword)
                .addOnSuccessListener {
                    Log.d("isANewPassword", "isANewPassword updated successfully")
                }
            TableRes.Success(Unit)
        } catch (e: Exception) {
            TableRes.Error(e.message ?: "Error updating isANewPassword")
        }
    }
}
