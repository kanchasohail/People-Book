package com.social.people_book.views.person_details_screen

import android.Manifest
import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.social.people_book.MainActivity
import com.social.people_book.model.room_database.Person
import com.social.people_book.model.util.image_converters.getBitmapFromUri
import com.social.people_book.model.util.workers.DeleteTrashPersonWork
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class PersonDetailsViewModel : ViewModel() {
    private val db = Firebase.firestore
    private val auth = Firebase.auth
    private val storage = Firebase.storage
    private val personDao = MainActivity.db.personDao()

    var savedPerson = Person(null, "", "", "", "", null, false)


    var thisPerson by mutableStateOf(Person(null, "", "", "", "", null, false))
    var name by mutableStateOf("")
    var number by mutableStateOf("")
    var email by mutableStateOf("")
    var about by mutableStateOf("")

    var selectedImage by mutableStateOf<Uri?>(null)
//    var downloadedImage by mutableStateOf<ByteArray?>(null)


    var isLoading by mutableStateOf(false)
    var showDialogState by mutableStateOf(false)


    fun loadForEditing() {
        name = thisPerson.name
        number = thisPerson.number.toString()
        email = thisPerson.email.toString()
        about = thisPerson.about.toString()
    }

    fun isChanged(): Boolean {
        val p = Person(
            id = savedPerson.id,
            name = name,
            number = number,
            email = email,
            about = about,
            image = savedPerson.image,
            isDeleted = savedPerson.isDeleted
        )
        return savedPerson != p || selectedImage != null
    }


    fun makePhoneCall(context: Context, number: String) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CALL_PHONE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Permission already granted, make the call
            val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$number"))
            context.startActivity(intent)
        } else {
            // Permission not granted, request it
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(Manifest.permission.CALL_PHONE),
                0
            )
        }
    }


    fun openEmailComposer(context: Context, email: String, subject: String) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:") // Only email apps should handle this
            putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
            putExtra(Intent.EXTRA_SUBJECT, subject)
        }
        context.startActivity(intent)
    }

    fun copyToClipboard(context: Context, text: String, label: String) {
        val clipboardManager =
            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText(label, text)
        clipboardManager.setPrimaryClip(clipData)

        Toast.makeText(context, "$label Copied to Clipboard", Toast.LENGTH_SHORT).show()
    }


//    fun loadPerson(personId: String) {
//        isLoading = true
//
//        db.collection("users").document(auth.currentUser?.uid.toString()).collection("persons")
//            .document(personId).get().addOnSuccessListener { result ->
//                val dpPerson = Person(
//                    personId = result["person_id"].toString(),
//                    name = result["name"].toString(),
//                    email = result["email"].toString(),
//                    number = result["number"].toString(),
//                    about = result["about"].toString()
//                )
//                thisPerson = dpPerson
//                loadPersonImage(result["person_id"].toString())
//                isLoading = false
//            }.addOnFailureListener {
//                isLoading = false
//                Log.d("Loading person", "Failed to load")
//            }
//    }

//    private fun loadPersonImage(documentId: String) {
//        val imageRef =
//            storage.reference.child("images/${auth.currentUser?.uid.toString()}/$documentId/profile.jpg")
//
//        val ONE_MEGABYTE: Long = 1024 * 1024
//        imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener {
//            // Data for "images/island.jpg" is returned, use this as needed
//            downloadedImage = it
//            Log.d("Person Image", "Downloading Successful")
//
//        }.addOnFailureListener { e ->
//            Log.d("Person Image", "Downloading Failed")
//            Log.d("Person Image", e.message.toString())
//        }
//    }


    fun deletePerson(personId: Long, context: Context, navController: NavController) {
        viewModelScope.launch {
            personDao.deletePerson(personId)
        }
        scheduleDeleteWork(personId, context)
        navController.popBackStack()
        Toast.makeText(context, "Person Deleted Successfully", Toast.LENGTH_SHORT).show()
    }

    private fun scheduleDeleteWork(personId: Long, context: Context) {
        val inputData = Data.Builder()
            .putLong("person_id", personId)
            .build()

        // Calculate the delay in milliseconds (30 days)
        val delayInMillis = TimeUnit.DAYS.toMillis(30)

        // Create the OneTimeWorkRequest with a 30-day initial delay
        val workRequest = OneTimeWorkRequest.Builder(DeleteTrashPersonWork::class.java)
            .setInitialDelay(delayInMillis, TimeUnit.MILLISECONDS).setInputData(inputData)
            .build()

        // Enqueue the WorkRequest
        WorkManager.getInstance(context).enqueue(workRequest)
    }

//    fun deletePerson(personId: String, context: Context, navController: NavController) {
//        showDialogState = false
//        db.collection("users").document(auth.currentUser?.uid.toString()).collection("persons")
//            .document(personId).delete().addOnSuccessListener {
//                deletePersonImage(personId)
//                navController.popBackStack()
//                Toast.makeText(context, "Person Deleted Successfully", Toast.LENGTH_SHORT).show()
//            }.addOnFailureListener {
//                Toast.makeText(context, "Failed to delete person.", Toast.LENGTH_SHORT).show()
//            }
//    }

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

    fun updatePerson(context: Context, navController: NavController) {
        isLoading = true
        db.collection("users").document(auth.currentUser?.uid.toString()).collection("persons")
            .document(thisPerson.id.toString()).update(
                mapOf(
                    "person_id" to thisPerson.id,
                    "name" to name,
                    "number" to number,
                    "email" to email,
                    "about" to about
                )
            ).addOnSuccessListener {
                updatePersonInRoomDatabase(context)
                updatePersonImage(thisPerson.id.toString())
                isLoading = false
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

    private fun updatePersonInRoomDatabase(context: Context) {
        val person = Person(
            id = thisPerson.id,
            name = name,
            number = number,
            email = email,
            about = about,
            image = selectedImage?.let { getBitmapFromUri(it, context) },
            isDeleted = false
        )
        viewModelScope.launch {
            personDao.updatePerson(person)
        }
    }

}