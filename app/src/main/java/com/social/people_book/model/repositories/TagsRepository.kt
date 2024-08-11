package com.social.people_book.model.repositories

import android.content.Context
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class TagsRepository(context: Context) {

    private val TAGS_LIST_KEY = "tags_list"
    private val prefs = context.getSharedPreferences("tags", Context.MODE_PRIVATE)

    val defaultTags = listOf("Family", "Friend", "Work", "Teacher")

    private val _tagsList = MutableStateFlow(getTags())
    val tagsList = _tagsList.asStateFlow()

    private fun getTags(): List<String> {
        val tagsList = prefs.getStringSet(TAGS_LIST_KEY, emptySet())?.toList()
        return if (tagsList?.isEmpty() == true || tagsList == null) {
            defaultTags
        } else {
            tagsList
        }
    }


    fun addTag(tag: String) {
        if (tagsList.value.isEmpty()) {
            prefs.edit().putStringSet(TAGS_LIST_KEY, defaultTags.toSet() + tag).apply()
        } else {
            prefs.edit().putStringSet(TAGS_LIST_KEY, tagsList.value.toSet() + tag).apply()
        }
        addTagToFirebase((defaultTags + tagsList.value + tag).toSet().toList())
        _tagsList.value = tagsList.value + tag
    }

    private fun addTagToFirebase(list: List<String>) {
        val db = Firebase.firestore
        val auth = Firebase.auth
        val user = auth.currentUser

        val docRef = db.collection("users").document(user?.uid.toString())
        docRef.update(hashMapOf("tags" to list) as Map<String, Any>)

    }


    fun deleteTag(tag: String) {
        val updatedList = tagsList.value.toMutableList().minus(tag)

        prefs.edit().putStringSet(TAGS_LIST_KEY, updatedList.toSet()).apply()
        deleteTagFromFirebase(updatedList)

        _tagsList.value = updatedList
    }

    private fun deleteTagFromFirebase(list: List<String>) {
        val db = Firebase.firestore
        val auth = Firebase.auth
        val user = auth.currentUser

        val docRef = db.collection("users").document(user?.uid.toString())
        docRef.update(hashMapOf("tags" to list) as Map<String, Any>)
    }


    fun saveTagsOnLogin() {
        val db = Firebase.firestore
        val auth = Firebase.auth
        val user = auth.currentUser

        val docRef = db.collection("users").document(user?.uid.toString())
        docRef.get().addOnSuccessListener { document ->

            if (document != null) {
                val tagsItem = document.data?.get("tags") as? List<String>
                if (tagsItem?.isNotEmpty() == true) {
                    prefs.edit().putStringSet(TAGS_LIST_KEY, tagsItem.toSet()).apply()
                } else {
                    prefs.edit().putStringSet(TAGS_LIST_KEY, defaultTags.toSet()).apply()
                }
            }

        }.addOnFailureListener {
            prefs.edit().putStringSet(TAGS_LIST_KEY, defaultTags.toSet()).apply()
        }
        _tagsList.value = getTags()
    }


}