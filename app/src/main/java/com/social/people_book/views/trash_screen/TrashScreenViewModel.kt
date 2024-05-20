package com.social.people_book.views.trash_screen

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
import com.google.firebase.Firebase
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.ktx.storage
import com.social.people_book.MainActivity
import com.social.people_book.model.room_database.Person
import com.social.people_book.model.room_database.Tag
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.sql.Date
import java.util.Calendar
import java.util.concurrent.TimeUnit
import kotlin.math.abs

class TrashScreenViewModel : ViewModel() {
    private val personDao = MainActivity.db.personDao()
    private val db = Firebase.firestore
    private val auth = com.google.firebase.ktx.Firebase.auth
    private val storage = com.google.firebase.ktx.Firebase.storage


    var showDialogState by mutableStateOf(false)
    var isLoading by mutableStateOf(false)


    var thisPerson by mutableStateOf(
        Person(
            null,
            "",
            "",
            "",
            "",
            Tag.None,
            null,
            false,
            false,
            null
        )
    )

    @OptIn(DelicateCoroutinesApi::class)
    fun loadPerson(personId: Long) {
        GlobalScope.launch(Dispatchers.IO) {
            thisPerson = personDao.getPersonById(personId)
        }
    }


    fun restorePerson(personId: Long, context: Context, navController: NavController) {
        isLoading = true
        viewModelScope.launch {
            personDao.restorePerson(personId)
        }.also {
            isLoading = false
            Toast.makeText(context, "Restored Successfully", Toast.LENGTH_SHORT).show()
            navController.popBackStack()
        }
    }

    fun getRemainingDays(startDate: Date?): String {
        if (startDate == null) {
            return "0 days"
        }
        val thirtyDaysLater = Date(startDate.time + TimeUnit.DAYS.toMillis(30))
        val today = java.util.Date()
        val diffTime = abs(thirtyDaysLater.time - today.time)
        val diffDays = TimeUnit.MILLISECONDS.toDays(diffTime).toInt()

        return when {
            diffDays == 1 -> "$diffDays day"
            else -> "$diffDays days"
        }
    }


    fun deletePersonFromTrash(personId: Long, context: Context, navController: NavController) {
        viewModelScope.launch {
            personDao.deletePersonFromTrash(personId)
            deleteFromFirebase(listOf(personId))
        }
        showDialogState = false
        navController.popBackStack()
        Toast.makeText(context, "Person Deleted Successfully", Toast.LENGTH_SHORT).show()
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

