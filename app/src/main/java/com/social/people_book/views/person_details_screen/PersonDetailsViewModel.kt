package com.social.people_book.views.person_details_screen

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.social.people_book.model.data_models.Person

class PersonDetailsViewModel : ViewModel() {
    private val db = Firebase.firestore
    private val auth = Firebase.auth

    var thisPerson by mutableStateOf(Person("", "", "", "", ""))

    var isLoading by mutableStateOf(false)
    var showDialogState by mutableStateOf(false)


    fun loadPerson(personId: String) {
        isLoading = true

        db.collection("users").document(auth.currentUser?.uid.toString()).collection("persons")
            .document(personId).get().addOnSuccessListener { result ->
                val dpPerson = Person(
                    personId = result["person_id"].toString(),
                    name = result["name"].toString(),
                    email = result["email"].toString(),
                    number = result["number"].toString(),
                    about = result["about"].toString()
                )
                thisPerson = dpPerson
                isLoading = false
            }.addOnFailureListener {
                isLoading = false
                Log.d("Loading person", "Failed to load")
            }
    }


    fun deletePerson(personId: String, context: Context, navController: NavController) {
        showDialogState = false
        db.collection("users").document(auth.currentUser?.uid.toString()).collection("persons")
            .document(personId).delete().addOnSuccessListener {
                navController.popBackStack()
                Toast.makeText(context, "Person Deleted Successfully", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(context, "Failed to delete person.", Toast.LENGTH_SHORT).show()
            }
    }

}