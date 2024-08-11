package com.social.people_book.views.add_tag_screen

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.social.people_book.MainActivity
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class AddTagScreenViewModel : ViewModel() {
    private val tagsRepository = MainActivity.tagsRepository
    private val db = MainActivity.db

    var tagName by mutableStateOf("")

    val tagsList = tagsRepository.tagsList


    var isLoading by mutableStateOf(false)
    var isDeleting by mutableStateOf(false)


    fun saveTag(context: Context) {
        if (tagName.isEmpty() || tagName.length < 2) {
            Toast.makeText(context, "Please enter a valid tag name", Toast.LENGTH_SHORT).show()
            return
        }
        isLoading = true
        runBlocking {
            MainActivity.tagsRepository.addTag(tagName)
        }
        Toast.makeText(context, "Tag Saved Successfully", Toast.LENGTH_SHORT).show()
        tagName = ""
        isLoading = false
    }

    fun deleteTag(tag: String) {
        isDeleting = true
        tagsRepository.deleteTag(tag)
        viewModelScope.launch {
            db.personDao().deleteTag(tag)
        }
        isDeleting = false
    }
}