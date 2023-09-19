package com.social.people_book.views.home_screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class HomeScreenViewModel : ViewModel() {


    var selectedTagItem by mutableStateOf("All")


    var isTagExpanded by mutableStateOf(true)


    // Create a list of items to display in the grid
    val items = (1..50).map { "Item $it" }

    val tags = listOf("All", "Friends", "colleagues", "Cousin", "Girlfriends", "Call Girls")

}