package com.social.people_book.views.trash_screen

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.firebase.Firebase
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.ktx.storage
import com.social.people_book.MainActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TrashScreenViewModel : ViewModel() {
    private val personDao = MainActivity.db.personDao()
    private val db = Firebase.firestore
    private val auth = com.google.firebase.ktx.Firebase.auth
    private val storage = com.google.firebase.ktx.Firebase.storage


    var showDialogState by mutableStateOf(false)
    var isLoading by mutableStateOf(false)


    fun emptyTrash(context: Context, navController: NavController) {
        viewModelScope.launch {
            isLoading = true

            personDao.getDeletedIds().also {
                deleteFromFirebase(it)
            }

            personDao.emptyTrash()
            isLoading = false
            navController.popBackStack()
            Toast.makeText(context, "Trash Cleared Successfully", Toast.LENGTH_SHORT).show()
        }
    }

    private fun deleteFromFirebase(idsList: List<Long>) {
        val collection =
            db.collection("users").document(auth.currentUser?.uid.toString()).collection("persons")
        idsList.forEach { id ->
            collection.document(id.toString()).delete().addOnSuccessListener {
                deletePersonImage(id.toString())
            }
        }
    }

    private fun deletePersonImage(documentId: String) {
        val imageRef =
            storage.reference.child("images/${auth.currentUser?.uid.toString()}/$documentId/profile.jpg")

        imageRef.delete().addOnSuccessListener {
            Log.d("Person Image", "Deleting successful")
        }.addOnFailureListener { e ->
            Log.d("Person Image", "Deleting Failed")
            Log.d("Person Image", e.message.toString())

        }
    }


}

