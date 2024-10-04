package com.social.people_book.views.auth_screen


import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.social.people_book.MainActivity
import com.social.people_book.model.repositories.LocalFileStorageRepository
import com.social.people_book.navigation.Screens


class AuthViewModel : ViewModel() {
    private val auth = Firebase.auth
    private val db = Firebase.firestore
    private val storage = Firebase.storage
    private val personDao = MainActivity.db.personDao()

    var email by mutableStateOf("")
    var password by mutableStateOf("")

    var isGoogleButtonLoading by mutableStateOf(false)
    var isLoginButtonLoading by mutableStateOf(false)
    var isShowPassword by mutableStateOf(false)
    var isEmailValid by mutableStateOf(true)
    var isPasswordValid by mutableStateOf(true)

    var showDialogState by mutableStateOf(false)


    private fun fetch(context: Context) {

        auth.currentUser?.uid?.let {
            fetchAndSaveDataFromFirebase(
                db = db,
                storage = storage,
                personDao = personDao,
                viewModelScope = viewModelScope,
                localFileStorageRepository = LocalFileStorageRepository(context),
                userId = it,
            )
        }
    }


    fun isValidEmail(email: String): Boolean {
        val emailRegex = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+".toRegex()
        isEmailValid = email.matches(emailRegex) || (email.isEmpty() || email.length < 6)
        return isEmailValid
    }

    fun isValidPassword(password: String): Boolean {
        isPasswordValid = password.length > 7
        return isPasswordValid
    }


    fun loginWithGoogle(idToken: String, context: Context, navController: NavController) {
        isGoogleButtonLoading = true
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    saveUserAndNavigate(context, navController)
                    fetch(context)
//                    navController.navigate(Screens.HomeScreen.route)
                }
            }
        isGoogleButtonLoading = false
    }

    fun singUpWithGoogle(idToken: String, context: Context, navController: NavController) {
        isGoogleButtonLoading = true
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    fetch(context)
                    saveUserAndNavigate(context, navController)
//                    navController.navigate(Screens.HomeScreen.route)
                }
            }
        isGoogleButtonLoading = false
    }


    fun signUp(navController: NavController, context: Context) {
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(context, "Invalid Email or Password", Toast.LENGTH_SHORT).show()
            return
        }
        isLoginButtonLoading = true
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            isLoginButtonLoading = false
            if (task.isSuccessful) {
                fetch(context)
                saveUserAndNavigate(context, navController, true)
//                navController.navigate(Screens.HomeScreen.route)
            } else {
                Toast.makeText(context, task.exception?.message.toString(), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    fun login(navController: NavController, context: Context) {
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(context, "Please enter the email and password", Toast.LENGTH_SHORT)
                .show()
            return
        }
        isLoginButtonLoading = true
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            isLoginButtonLoading = false
            if (task.isSuccessful) {
                saveUserAndNavigate(context, navController, true)
                fetch(context)
//                navController.navigate(Screens.HomeScreen.route)
            } else {
                Toast.makeText(context, task.exception?.message.toString(), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }


    private fun saveUserAndNavigate(
        context: Context,
        navController: NavController,
        withPassword: Boolean? = null,
    ) {
        val userDoc = db.collection("users").document(auth.currentUser?.uid.toString())

        userDoc.get().addOnSuccessListener { result ->
            val isDeleted = result["isDeleted"] == true

            if (isDeleted) {
                showDialogState = true
            } else {
                navController.navigate(Screens.HomeScreen.route) {
                    popUpTo(Screens.AuthScreen.route) {
                        inclusive = true
                    }
                }
            }

            // Save the user info and set isDeleted to false
            userDoc.update(
                mapOf(
                    "email" to email,
                    "withPassword" to withPassword,
                    "isDeleted" to false
                )
            ).addOnSuccessListener {
                Toast.makeText(
                    context,
                    "Saving User successful",
                    Toast.LENGTH_SHORT,
                ).show()
            }
        }

    }


    fun sendPasswordResetEmail(context: Context) {
        val emailRegex = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+".toRegex()
        if (!emailRegex.matches(email)) {
            Toast.makeText(context, "Please enter a valid email", Toast.LENGTH_SHORT).show()
            return
        }
        isLoginButtonLoading = true
        auth.sendPasswordResetEmail(email).addOnSuccessListener {
            isLoginButtonLoading = false
            Toast.makeText(context, "Password reset link sent to your email", Toast.LENGTH_LONG)
                .show()
        }.addOnFailureListener {
            isLoginButtonLoading = false
            Toast.makeText(context, "Something went wrong please try again", Toast.LENGTH_LONG)
                .show()
        }
    }
}
