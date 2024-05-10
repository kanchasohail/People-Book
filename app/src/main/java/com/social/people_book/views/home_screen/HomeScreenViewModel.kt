package com.social.people_book.views.home_screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.social.people_book.MainActivity
import com.social.people_book.model.room_database.Person
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeScreenViewModel : ViewModel() {

    private val db = Firebase.firestore
    private val auth = Firebase.auth
    private val personDao = MainActivity.db.personDao()

    // Search Field Logic
    private val _searchText = MutableStateFlow("")
    val searchBarText = _searchText.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()


    var selectedTagItem by mutableStateOf("All")

    var isLoading by mutableStateOf(false)
    var isTagExpanded by mutableStateOf(true)

    private val userDocumentAddress =
        db.collection("users").document(auth.currentUser?.uid.toString())

    var userName = ""

    fun loadUserName() {
        userDocumentAddress.get().addOnSuccessListener { result ->
            userName = result["username"].toString()
        }
    }


    val tags = listOf("All", "Friends", "Colleagues", "Cousin", "Co-workers", "Partners")

    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

    private val _persons = MutableStateFlow<List<Person>>(emptyList())
//    val persons: StateFlow<List<Person>> = _persons



    @OptIn(FlowPreview::class)
    val persons = searchBarText.debounce(500L).onEach { _isSearching.update { true } }
        .combine(_persons) { text, persons ->
            if (text.isBlank()) {
                persons
            } else {
                delay(1000L) //Just to simulate a delay
                persons.filter {
                    it.doesMatchSearchQuery(text)
                }
            }
        }.onEach { _isSearching.update { false } }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            _persons.value
        )


    init {
        viewModelScope.launch {
            personDao.getAll().collectLatest { personList ->
                _persons.value = personList
            }
        }
    }

    private fun loadPersons() : List<Person> {
        isLoading = true
        val dbItems = mutableStateListOf<Person>()
        userDocumentAddress.collection("persons")
            .get().addOnSuccessListener { result ->
                for (document in result) {
                    val thisPerson = Person(
                        id = document["person_id"].toString().toLong(),
                        name = document["name"].toString(),
                        number = document["number"].toString(),
                        email = document["email"].toString(),
                        about = null,
                        isDeleted = document["is_deleted"].toString() == "true",
                        image = null

                    )
                    dbItems.add(thisPerson)
                }
                isLoading = false
            }
        return dbItems
    }


//    private fun loadPersons(): List<Person> {
//        isLoading = true
//        val dbItems = mutableStateListOf<Person>()
//        viewModelScope.launch {
//            dbItems.addAll(personDao.getAllPersonList())
//        }
//
//        isLoading = false
//
//        return dbItems
//    }




}