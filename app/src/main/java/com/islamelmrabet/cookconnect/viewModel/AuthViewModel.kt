package com.islamelmrabet.cookconnect.viewModel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.islamelmrabet.cookconnect.utils.AuthManager
import com.islamelmrabet.cookconnect.utils.TableRes
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

/**
 * Class AuthViewModel
 *
 */
class AuthViewModel : ViewModel() {
    val auth: FirebaseAuth by lazy { Firebase.auth }

    /**
     *  Gets password using the email
     *
     * @param email
     * @return
     */
    suspend fun getPasswordByEmail(email: String): String? {
        val usersRef = FirebaseFirestore.getInstance().collection("workers")
        val querySnapshot = usersRef.whereEqualTo("email", email).get().await()
        if (!querySnapshot.isEmpty) {
            return querySnapshot.documents.firstOrNull()?.getString("password")
        }
        return null
    }

    /**
     * Gets the value of the field anewPassword
     *
     * @param email
     * @return Boolean?
     */
    suspend fun getIsANewPassword(email: String): Boolean? {
        val usersRef = FirebaseFirestore.getInstance().collection("workers")
        val querySnapshot = usersRef.whereEqualTo("email", email).get().await()
        if (!querySnapshot.isEmpty) {
            return querySnapshot.documents.firstOrNull()?.getBoolean("anewPassword")
        }
        return null
    }

    /**
     * Gets role using the email.
     *
     * @param email
     * @return String?
     */
    suspend fun getRoleByEmail(email: String): String? {
        val usersRef = FirebaseFirestore.getInstance().collection("workers")
        val querySnapshot = usersRef.whereEqualTo("email", email).get().await()
        if (!querySnapshot.isEmpty) {
            return querySnapshot.documents.firstOrNull()?.getString("userRole")
        }
        return null
    }

    /**
     * Gets last login date
     *
     * @return String?
     */
    suspend fun getLastLoginDate(): String? {

        val userId = auth.currentUser?.uid
        if (userId != null) {
            val usersRef = FirebaseFirestore.getInstance().collection("workers")
            val documentSnapshot = usersRef.document(userId).get().await()
            if (documentSnapshot.exists()) {
                return documentSnapshot.getDate("lastLogin").toString()
            }
        }
        return null
    }

    /**
     * Gets name of the worker
     *
     * @return String?
     */
    suspend fun getUsername(): String? {

        val userId = auth.currentUser?.uid
        if (userId != null) {
            val usersRef = FirebaseFirestore.getInstance().collection("workers")
            val documentSnapshot = usersRef.document(userId).get().await()
            if (documentSnapshot.exists()) {
                return documentSnapshot.getString("name")
            }
        }
        return null
    }

    /**
     *  Gets the email of the worker
     *
     * @return String?
     */
    suspend fun getEmail(): String? {

        val userId = auth.currentUser?.uid
        if (userId != null) {
            val usersRef = FirebaseFirestore.getInstance().collection("workers")
            val documentSnapshot = usersRef.document(userId).get().await()
            if (documentSnapshot.exists()) {
                return documentSnapshot.getString("email")
            }
        }
        return null
    }

    /**
     * Gets the user id
     *
     * @return String?
     */
    fun getUserID(): String? {
        return auth.currentUser?.uid
    }

    /**
     * Gets the role of the user.
     *
     * @return
     */
    suspend fun getRole(): String? {

        val userId = auth.currentUser?.uid
        if (userId != null) {
            val usersRef = FirebaseFirestore.getInstance().collection("workers")
            val documentSnapshot = usersRef.document(userId).get().await()
            if (documentSnapshot.exists()) {
                return documentSnapshot.getString("userRole")
            }
        }
        return null
    }

    /**
     * Update the new password
     *
     * @param email
     * @param authManager
     * @param context
     * @param password
     */
    fun updatePassword(
        email: String,
        authManager: AuthManager,
        context: Context,
        password: String
    ) {
        viewModelScope.launch {
            val workerId = authManager.getWorkerDocumentIdByEmail(email)
            if (workerId != null) {
                val result = authManager.updatePassword(workerId, password)
                if (result is TableRes.Success) {
                    Toast.makeText(context, "Product updated successfully", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(context, "Error updating product", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Product not found", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Update the field isANewPassword
     *
     * @param email
     * @param authManager
     * @param isANewPassword
     */
    fun updateIsANewPassword(
        email: String,
        authManager: AuthManager,
        isANewPassword: Boolean
    ) {
        viewModelScope.launch {
            val workerId = authManager.getWorkerDocumentIdByEmail(email)
            if (workerId != null) {
                val result = authManager.updateIsANewPasswordWorker(workerId, isANewPassword)
            }
        }
    }
}
