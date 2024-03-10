package com.social.people_book.views.auth_screen

import android.content.Context
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

class AuthViewModel : ViewModel() {
    private val auth = Firebase.auth
    private val db = Firebase.firestore

    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var name by mutableStateOf("")

    var isLoading by mutableStateOf(false)
    var isShowPassword by mutableStateOf(false)
    var isEmailValid by mutableStateOf(true)
    var isPasswordValid by mutableStateOf(true)


    fun isValidEmail(email: String): Boolean {
        val emailRegex = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+".toRegex()
        isEmailValid = email.matches(emailRegex) || email.isEmpty()
        return isEmailValid
    }

    fun isValidPassword(password: String): Boolean {
        isPasswordValid = password.length > 7
        return isPasswordValid
    }

    fun signUp(navController: NavController, context: Context) {
        isLoading = true
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            isLoading = false
            if (task.isSuccessful) {
                saveUser(context)
                navController.navigate(Screens.HomeScreen.route)
            } else {
                Toast.makeText(context, task.exception?.message.toString(), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    fun login(navController: NavController, context: Context) {
        isLoading = true
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            isLoading = false
            if (task.isSuccessful) {
                navController.navigate(Screens.HomeScreen.route)
            } else {
                Toast.makeText(context, task.exception?.message.toString(), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }


    private fun saveUser(context: Context) {
        val userDoc = db.collection("users").document(auth.currentUser?.uid.toString())
        userDoc.set(
            mapOf(
                "username" to name,
                "email" to email
            )
        ).addOnSuccessListener {
            Toast.makeText(
                context,
                "Saving User successful",
                Toast.LENGTH_SHORT,
            ).show()
        }
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
}