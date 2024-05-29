package com.social.people_book.views.settings_screen

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.social.people_book.navigation.Screens

class SettingsViewModel : ViewModel() {
    private val auth = Firebase.auth
    private val db = Firebase.firestore

    var isLoading by mutableStateOf(false)
    var showDialogState by mutableStateOf(false)
    var logoutDialogState by mutableStateOf(false)

    var email by mutableStateOf("example@email.com")


    fun getUserDetails(context: Context) {
        isLoading = true
        db.collection("users").document(auth.currentUser?.uid.toString()).get()
            .addOnSuccessListener { result ->
                isLoading = false
                email = result["email"].toString()

            }.addOnFailureListener {
                isLoading = false
                Toast.makeText(context, "Failed to load details", Toast.LENGTH_SHORT).show()
            }
    }

    fun contactUsEmail(context: Context) {
        val email = "thegeniuscoder100@gmail.com"
        val subject = ""
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:") // Only email apps should handle this
            putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
            putExtra(Intent.EXTRA_SUBJECT, subject)
        }
        context.startActivity(intent)
    }

    fun sendPasswordResetEmail(context: Context) {
        isLoading = true
        auth.sendPasswordResetEmail(email).addOnSuccessListener {
            isLoading = false
            Toast.makeText(context, "Password reset link sent to your email", Toast.LENGTH_LONG)
                .show()
        }.addOnFailureListener {
            isLoading = false
            Toast.makeText(context, "Something went wrong please try again", Toast.LENGTH_LONG)
                .show()
        }
    }

    fun logOut(navController: NavController) {
        isLoading = true
        auth.signOut()
        navController.popBackStack(Screens.HomeScreen.route, inclusive = true)
        navController.navigate(Screens.AuthScreen.route)
    }

}