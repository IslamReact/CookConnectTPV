package com.islamelmrabet.cookconnect.viewModel

import androidx.lifecycle.ViewModel

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AuthViewModel : ViewModel() {

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


}
