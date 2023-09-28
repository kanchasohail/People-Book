package com.social.people_book.views.home_screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.social.people_book.model.data_models.Person

class HomeScreenViewModel : ViewModel() {

    private val db = Firebase.firestore
    private val auth = Firebase.auth


    var selectedTagItem by mutableStateOf("All")


    var isTagExpanded by mutableStateOf(true)

    private val userDocumentAddress = db.collection("users").document(auth.currentUser?.uid.toString())

    var userName = ""

    fun loadUserName(){
        userDocumentAddress.get().addOnSuccessListener {result ->
            userName = result["username"].toString()
        }
    }
    // Create a list of items to display in the grid
    var items = mutableStateListOf<Person>()

    fun loadPerson() {
        userDocumentAddress.collection("persons")
            .get().addOnSuccessListener { result ->
                val dbItems = mutableStateListOf<Person>()
                for (document in result) {
                    val thisPerson = Person(
                        personId = document["person_id"].toString(),
                        name = document["name"].toString(),
                        number = document["number"].toString(),
                        email = document["email"].toString(),
                        about = null
                    )
                    dbItems.add(thisPerson)
                }
                items = dbItems
            }
    }

    val tags = listOf("All", "Friends", "Colleagues", "Cousin", "Co-workers", "Partners")

}