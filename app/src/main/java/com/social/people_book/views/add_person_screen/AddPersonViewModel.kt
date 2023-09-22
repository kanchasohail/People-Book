package com.social.people_book.views.add_person_screen

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AddPersonViewModel : ViewModel() {
    private val db = Firebase.firestore

    var name by mutableStateOf("")
    var number by mutableStateOf("")
    var email by mutableStateOf("")
    var about by mutableStateOf("")


    fun addPerson() {
        // Create a new user with a first and last name
        val person = mapOf(
            "name" to name,
            "number" to number,
            "email" to email,
            "detail" to about
        )

        // Add a new document with a generated ID
        db.collection("users")
            .add(person)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }

    }

}