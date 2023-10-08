package com.social.people_book.views.person_details_screen

import android.content.Context
import android.net.Uri
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
import com.google.firebase.storage.ktx.storage
import com.social.people_book.model.data_models.Person

class PersonDetailsViewModel : ViewModel() {
    private val db = Firebase.firestore
    private val auth = Firebase.auth
    private val storage = Firebase.storage


    var thisPerson by mutableStateOf(Person("", "", "", "", ""))
    var name by mutableStateOf("")
    var number by mutableStateOf("")
    var email by mutableStateOf("")
    var about by mutableStateOf("")

    var selectedImage by mutableStateOf<Uri?>(null)
    var downloadedImage by mutableStateOf<ByteArray?>(null)


    var isLoading by mutableStateOf(false)
    var showDialogState by mutableStateOf(false)


    fun loadForEditing() {
        name = thisPerson.name
        number = thisPerson.number.toString()
        email = thisPerson.email.toString()
        about = thisPerson.about.toString()

    }

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
                loadPersonImage(result["person_id"].toString())
                isLoading = false
            }.addOnFailureListener {
                isLoading = false
                Log.d("Loading person", "Failed to load")
            }
    }

    private fun loadPersonImage(documentId: String) {
        val imageRef =
            storage.reference.child("images/${auth.currentUser?.uid.toString()}/$documentId/profile.jpg")

        val ONE_MEGABYTE: Long = 1024 * 1024
        imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener {
            // Data for "images/island.jpg" is returned, use this as needed
            downloadedImage = it
            Log.d("Person Image", "Downloading Successful")

        }.addOnFailureListener {e ->
            Log.d("Person Image", "Downloading Failed")
            Log.d("Person Image", e.message.toString())
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

    fun updatePerson(context: Context, navController: NavController) {
        isLoading = true
        db.collection("users").document(auth.currentUser?.uid.toString()).collection("persons")
            .document(thisPerson.personId).update(
                mapOf(
                    "person_id" to thisPerson.personId,
                    "name" to name,
                    "number" to number,
                    "email" to email,
                    "about" to about
                )
            ).addOnSuccessListener {
                isLoading = false
                updatePersonImage(thisPerson.personId)
                Toast.makeText(context, "Updated Successfully", Toast.LENGTH_SHORT).show()
                navController.popBackStack()
            }.addOnFailureListener {
                isLoading = false
                Toast.makeText(context, "Failed to Update!", Toast.LENGTH_SHORT).show()
            }
    }

    private fun updatePersonImage(documentId: String) {
        val imageRef =
            storage.reference.child("images/${auth.currentUser?.uid.toString()}/$documentId/profile.jpg")

        if (selectedImage != null) {
            imageRef.putFile(selectedImage!!).addOnSuccessListener {
                Log.d("Person Image", "Updating successful")
            }.addOnFailureListener { e ->
                Log.d("Person Image", "Updating Failed")
                Log.d("Person Image", e.message.toString())

            }
        }
    }

}