package com.social.people_book.views.add_person_screen

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AddPersonViewModel : ViewModel() {
    private val db = Firebase.firestore
    private val auth = Firebase.auth

    var name by mutableStateOf("")
    var number by mutableStateOf("")
    var email by mutableStateOf("")
    var about by mutableStateOf("")

    var isLoading by mutableStateOf(false)


    fun addPerson(context: Context) {
        isLoading = true

        // Add a new document with a generated ID
        val thisDocument = db.collection("users")
            .document(auth.currentUser?.uid.toString())
            .collection("persons").document()

        // Set the data in tha document
        thisDocument.set(
            // Create a map of data to be saved
            mapOf(
                "person_id" to thisDocument.id,
                "name" to name,
                "number" to number,
                "email" to email,
                "detail" to about
            )
        ).addOnSuccessListener {
            isLoading = false
            Toast.makeText(context, "Person added successfully!", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener { e ->
            isLoading = false
            Toast.makeText(context, "Failed to add Person!", Toast.LENGTH_SHORT).show()
            Log.w(TAG, "Error adding document", e)
        }


    }

}