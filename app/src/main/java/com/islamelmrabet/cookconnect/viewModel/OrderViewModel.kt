package com.islamelmrabet.cookconnect.viewModel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class OrderViewModel : ViewModel(){
    val auth: FirebaseAuth by lazy { Firebase.auth }
    val uid: String? by lazy { auth.currentUser?.uid }



}