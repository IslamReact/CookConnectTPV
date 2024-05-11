package com.islamelmrabet.cookconnect.utils

import android.content.Context
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.islamelmrabet.cookconnect.model.User
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class RealtimeManager(context: Context) {
    private val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference.child("contacts")
    private val authManager = AuthManager(context)

    fun addContact(contact: User) {
        val key = databaseReference.push().key
        if (key != null) {
            databaseReference.child(key).setValue(contact)
        }
    }

    fun deleteContact(contactId: String) {
        databaseReference.child(contactId).removeValue()
    }

    fun updateContact(contactId: String, updatedContact: User) {
        databaseReference.child(contactId).setValue(updatedContact)
    }

    fun getContactsFlow(): Flow<List<User>> {
        val idFilter = authManager.getCurrentUser()?.uid
        val flow = callbackFlow {
            val listener = databaseReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val contacts = snapshot.children.mapNotNull {  snapshot ->
                        val contact = snapshot.getValue(User::class.java)
                        snapshot.key?.let { contact?.copy(key = it) }
                    }
                    trySend(contacts.filter { it.uid == idFilter }).isSuccess
                }
                override fun onCancelled(error: DatabaseError) {
                    close(error.toException())
                }
            })
            awaitClose { databaseReference.removeEventListener(listener) }
        }
        return flow
    }
}