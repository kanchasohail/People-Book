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
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firestore.v1.DocumentTransform
import com.social.people_book.navigation.Screens

class SettingsViewModel : ViewModel() {
    private val auth = Firebase.auth
    private val db = Firebase.firestore

    var password by mutableStateOf("")
    var isShowPassword by mutableStateOf(false)

    var isLoading by mutableStateOf(false)
    var showDialogState by mutableStateOf(false)
    var logoutDialogState by mutableStateOf(false)

    var email by mutableStateOf("example@email.com")
    var withPassword by mutableStateOf(false)


    fun getUserDetails(context: Context) {
        isLoading = true
//        db.collection("users").document(auth.currentUser?.uid.toString()).get()
//            .addOnSuccessListener { result ->
//                isLoading = false
//                email = result["email"].toString()
//
//            }.addOnFailureListener {
//                isLoading = false
//                Toast.makeText(context, "Failed to load details", Toast.LENGTH_SHORT).show()
//            }
        email = auth.currentUser?.email.toString()
        db.collection("users").document(auth.currentUser?.uid.toString()).get()
            .addOnSuccessListener { result ->
                withPassword = result["withPassword"].toString() == true.toString()
            }
        isLoading = false
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

    fun scheduleAccountDeletion(context: Context, navController: NavController) {
        isLoading = true
        val userDoc = db.collection("users").document(auth.currentUser?.uid.toString())
        val accountDeletionTimestamp = DocumentTransform.FieldTransform.ServerValue.REQUEST_TIME

        //Todo deleting the account immediately for now
        auth.currentUser?.delete()?.addOnSuccessListener {
            userDoc.update(
                mapOf(
                    "deletedAt" to accountDeletionTimestamp,
                    "isDeleted" to true
                )
            )

            auth.signOut()
            navController.popBackStack(Screens.HomeScreen.route, inclusive = true)
            navController.navigate(Screens.AuthScreen.route)

            Toast.makeText(context, "Account Deleted", Toast.LENGTH_SHORT).show()
        }?.addOnFailureListener {
            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
        }
        isLoading = false

    }

    fun verifyPasswordAndDelete(context: Context, navController: NavController) {
        if (password.isEmpty()) {
            Toast.makeText(context, "Please enter your password", Toast.LENGTH_SHORT).show()
            return
        }


        isLoading = true
        val user = auth.currentUser


        if (user != null && withPassword) {
            // Create an EmailAuthCredential with the current password
            val credential = EmailAuthProvider.getCredential(user.email!!, password)

            user.reauthenticateAndRetrieveData(credential).addOnSuccessListener { task ->
                scheduleAccountDeletion(context, navController)
                isLoading = false
            }.addOnFailureListener { e ->
                isLoading = false
                if (e.cause.toString() == "ERROR_WRONG_PASSWORD") {
                    Toast.makeText(context, "Incorrect Password", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

}