package com.islamelmrabet.cookconnect.viewModel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class AuthViewModel : ViewModel() {
    val auth: FirebaseAuth by lazy { Firebase.auth }

    suspend fun getPasswordByEmail(email: String): String? {
        val usersRef = FirebaseFirestore.getInstance().collection("workers")
        val querySnapshot = usersRef.whereEqualTo("email", email).get().await()
        if (!querySnapshot.isEmpty) {
            return querySnapshot.documents.firstOrNull()?.getString("password")
        }
        return null
    }

    suspend fun getRoleByEmail(email: String): String? {
        val usersRef = FirebaseFirestore.getInstance().collection("workers")
        val querySnapshot = usersRef.whereEqualTo("email", email).get().await()
        if (!querySnapshot.isEmpty) {
            return querySnapshot.documents.firstOrNull()?.getString("userRole")
        }
        return null
    }

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

    suspend fun getUserID(): String? {
        val userId = auth.currentUser?.uid
        return userId
    }
}
