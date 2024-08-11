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
import com.social.people_book.model.repositories.LocalFileStorageRepository
import com.social.people_book.model.room_database.Person
import com.social.people_book.model.util.image_converters.getBitmapFromUri
import com.social.people_book.model.util.workers.DeleteTrashPersonWork
import kotlinx.coroutines.launch
import java.sql.Date
import java.util.Calendar
import java.util.concurrent.TimeUnit

class PersonDetailsViewModel : ViewModel() {
    private val db = Firebase.firestore
    private val auth = Firebase.auth
    private val storage = Firebase.storage
    private val personDao = MainActivity.db.personDao()
//    val tagsList = Tag.values()
    val tagsList = MainActivity.tagsRepository.tagsList.value

    var savedPerson = Person(null, "", "", "", "", null, null, false, false, null)


    var thisPerson by mutableStateOf(
        Person(
            null,
            "",
            "",
            "",
            "",
            null,
            null,
            false,
            false,
            null
        )
    )
    var name by mutableStateOf("")
    var number by mutableStateOf("")
    var email by mutableStateOf("")
    var about by mutableStateOf("")
    var isFavorite by mutableStateOf(false)

    var selectedTag by mutableStateOf<String?>(null)

    var selectedImage by mutableStateOf<Uri?>(null)

    //    var downloadedImage by mutableStateOf<ByteArray?>(null)
    var imageBitmap by mutableStateOf<android.graphics.Bitmap?>(null)

    var isLoading by mutableStateOf(false)
    var showDialogState by mutableStateOf(false)
    var isDropDownOpen by mutableStateOf(false)


    fun loadForEditing() {
        name = thisPerson.name
        number = thisPerson.number.toString()
        email = thisPerson.email.toString()
        about = thisPerson.about.toString()
        isFavorite = thisPerson.isFavorite
        selectedTag = thisPerson.tag
    }

    fun isChanged(): Boolean {
        val p = Person(
            id = savedPerson.id,
            name = name,
            number = number,
            email = email,
            about = about,
            image = savedPerson.image,
            isDeleted = savedPerson.isDeleted,
            isFavorite = savedPerson.isFavorite,
            tag = savedPerson.tag,
            deletedAt = savedPerson.deletedAt
        )
        return savedPerson != p || selectedImage != null
    }

//    fun markFavorite(isFavorite: Boolean) {
//        isLoading = true
//        viewModelScope.launch {
//            runBlocking {
//                personDao.updatePerson(
//                    Person(
//                        thisPerson.id,
//                        thisPerson.name,
//                        thisPerson.number,
//                        thisPerson.email,
//                        thisPerson.about,
//                        thisPerson.tag,
//                        thisPerson.image,
//                        isFavorite,
//                        thisPerson.isDeleted
//                    )
//                )
//                isLoading = false
//            }
//        }
//    }

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

    fun isValidEmail(email: String): Boolean {
        val emailRegex = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+".toRegex()
        return email.matches(emailRegex)
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
            val thisDate = Calendar.getInstance().timeInMillis
            personDao.deletePerson(personId, deletedAt = Date(thisDate))
            deletePerson(personId)
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

    private fun deletePerson(personId: Long) {
        db.collection("users").document(auth.currentUser?.uid.toString()).collection("persons")
            .document(personId.toString()).update("is_deleted", true).addOnSuccessListener {

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

    fun updatePerson(context: Context, navController: NavController) {
        if (name.isEmpty() && number.isEmpty() && email.isEmpty() && (selectedImage == null && imageBitmap == null)) {
            thisPerson.id?.let { deletePerson(it, context, navController) }
            navController.popBackStack()
            return
        }
        isLoading = true
        db.collection("users").document(auth.currentUser?.uid.toString()).collection("persons")
            .document(thisPerson.id.toString()).update(
                mapOf(
                    "person_id" to thisPerson.id,
                    "name" to name,
                    "number" to number,
                    "email" to email,
                    "about" to about,
                    "tag" to selectedTag,
//                    "image" to "profile_${thisPerson.id}",
                    "image" to if (selectedImage != null || imageBitmap != null) "profile_${thisPerson.id}" else null,
                    "is_favorite" to isFavorite,
                    "is_deleted" to false,
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
//            image = selectedImage?.let { getBitmapFromUri(it, context) },
            image = if (selectedImage != null || imageBitmap != null) "profile_${thisPerson.id}" else null,
            isDeleted = false,
            isFavorite = isFavorite,
            tag = selectedTag,
            deletedAt = null
        )
        viewModelScope.launch {
            personDao.updatePerson(person)
            if (selectedImage != null) {
                val localFileStorage = LocalFileStorageRepository(context)
                val bitmap = getBitmapFromUri(selectedImage!!, context)
                bitmap?.let {
                    localFileStorage.saveImageToInternalStorage(
                        filename = "profile_${thisPerson.id}",
                        bitmap = it
                    )
                }
            }
        }
    }

}