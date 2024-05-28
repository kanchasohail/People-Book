package com.social.people_book.views.add_person_screen

import android.content.ContentValues.TAG
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.social.people_book.MainActivity
import com.social.people_book.model.LocalFileStorageRepository
import com.social.people_book.model.room_database.Person
import com.social.people_book.model.room_database.Tag
import com.social.people_book.model.util.image_converters.getBitmapFromUri
import com.social.people_book.navigation.Screens
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class AddPersonViewModel : ViewModel() {
    private val db = Firebase.firestore
    private val auth = Firebase.auth
    private val storage = Firebase.storage

    private val personDao = MainActivity.db.personDao()

    val tagsList = Tag.values()

    var name by mutableStateOf("")
    var number by mutableStateOf("")
    var email by mutableStateOf("")
    var about by mutableStateOf("")
    var isFavorite by mutableStateOf(false)
    var selectedTag by mutableStateOf(Tag.None)

    var selectedImage by mutableStateOf<Uri?>(null)

    var isLoading by mutableStateOf(false)

    var showDialogState by mutableStateOf(false)

    var isDropDownOpen by mutableStateOf(false)


    suspend fun addPerson(context: Context, navController: NavController) {
        if (name == "" && number == "" && email == "" && about == "" && selectedImage == null) {
            Toast.makeText(
                context,
                "Please enter something to save this person!",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val roomPerson = Person(
            id = null,
            name = name,
            number = number,
            about = about,
            email = email,
            isDeleted = false,
            isFavorite = isFavorite,
            tag = selectedTag,
//            image = selectedImage?.let { getBitmapFromUri(it, context) },
            image = null,
            deletedAt = null,
        )
        viewModelScope.launch {
            val personId = personDao.addPerson(roomPerson)

            if (selectedImage != null) {
                val localFileStorage = LocalFileStorageRepository(context)
                val bitmap = getBitmapFromUri(selectedImage!!, context)
                val isSaved = bitmap?.let {
                    localFileStorage.saveImageToInternalStorage(
                        filename = "profile_$personId",
                        bitmap = it
                    )
                }
                if (isSaved == true) personDao.updatePerson(
                    roomPerson.copy(
                        id = personId,
                        image = "profile_$personId"
                    )
                )
            }
            addPersonInFirebase(context, navController, personId)
        }
    }

    fun isChanged(): Boolean {
        return name != "" || number != "" || email != "" || about != "" || selectedImage != null
    }

    private fun addPersonInFirebase(
        context: Context,
        navController: NavController,
        personId: Long
    ) {
        isLoading = true
        runBlocking {
            val thisDocument = db.collection("users")
                .document(auth.currentUser?.uid.toString())
                .collection("persons").document(personId.toString())

            // Set the data in tha document
            thisDocument.set(
                // Create a map of data to be saved
                mapOf(
                    "person_id" to thisDocument.id,
                    "name" to name,
                    "number" to number,
                    "email" to email,
                    "about" to about,
                    "tag" to selectedTag,
                    "is_favorite" to isFavorite,
//                    "image" to "profile_$personId"
                    "image" to if (selectedImage != null) "profile_$personId" else null
                )
            ).addOnSuccessListener {
                saveImage(thisDocument.id)
//                clearFields()
                isLoading = false
                if (navController.currentDestination?.route == Screens.AddPersonScreen.route) {
                    navController.popBackStack()
                }
                Toast.makeText(context, "Person added successfully!", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener { e ->
                isLoading = false
                Toast.makeText(context, "Failed to add Person!", Toast.LENGTH_SHORT).show()
                Log.w(TAG, "Error adding document", e)
            }
        }


    }


    private fun saveImage(documentId: String) {
        val imageRef =
            storage.reference.child("images/${auth.currentUser?.uid.toString()}/$documentId/profile.jpg")

        if (selectedImage != null) {
            imageRef.putFile(selectedImage!!).addOnSuccessListener {
                Log.d("Person Image", "Saving successful")
            }.addOnFailureListener { e ->
                Log.d("Person Image", "Saving Failed")
                Log.d("Person Image", e.message.toString())

            }
        }
    }


//    private fun clearFields() {
//        name = ""
//        number = ""
//        email = ""
//        about = ""
//        selectedImage = null
//    }

}