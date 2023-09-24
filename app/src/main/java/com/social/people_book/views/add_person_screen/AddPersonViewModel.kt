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
        // Create a new user with a first and last name
        val person = mapOf(
            "name" to name,
            "number" to number,
            "email" to email,
            "detail" to about
        )

        // Add a new document with a generated ID
        db.collection("users")
            .document(auth.currentUser?.uid.toString())
            .collection("persons")
            .add(person)
            .addOnSuccessListener { documentReference ->
                isLoading = false
                Toast.makeText(context, "Person added successfully!", Toast.LENGTH_SHORT).show()
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                isLoading = false
                Toast.makeText(context, "Failed to add Person!", Toast.LENGTH_SHORT).show()
                Log.w(TAG, "Error adding document", e)
            }


    }

}