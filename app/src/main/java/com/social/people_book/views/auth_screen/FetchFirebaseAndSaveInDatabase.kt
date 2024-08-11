package com.social.people_book.views.auth_screen

import android.graphics.BitmapFactory
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.social.people_book.MainActivity
import com.social.people_book.model.repositories.LocalFileStorageRepository
import com.social.people_book.model.room_database.Person
import com.social.people_book.model.room_database.PersonDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun fetchAndSaveDataFromFirebase(
    db: FirebaseFirestore,
    storage: FirebaseStorage,
    localFileStorageRepository: LocalFileStorageRepository,
    userId: String,
    personDao: PersonDao,
    viewModelScope: CoroutineScope
) {
    viewModelScope.launch {
        personDao.clearDatabase()
    }

    db.collection("users").document(userId).collection("persons").get()
        .addOnSuccessListener { result ->
            for (document in result) {
                document.data.forEach { (_, _) ->
                    val person = Person(
                        id = document.id.toLong(),
                        name = document.data["name"].toString(),
                        number = document.data["number"].toString(),
                        email = document.data["email"].toString(),
                        about = document.data["about"].toString(),
                        tag = if (document.data["tag"].toString() == "null") null else document.data["tag"].toString(),
//                        image = loadPersonImage(storage, document.id, userId),
                        image = if (document.data["image"].toString() == "null") null else document.data["image"].toString(),
                        isFavorite = document.data["is_favorite"].toString() == "true",
                        isDeleted = document.data["is_deleted"].toString() == "true",
                        deletedAt = null
                    )
                    saveToRoomDatabase(person, viewModelScope, personDao)
                    loadAndSavePersonImage(
                        storage = storage,
                        userId = userId,
                        documentId = document.id,
                        localFileStorageRepository = localFileStorageRepository,
                        viewModelScope = viewModelScope
                    )
                }
            }
            MainActivity.tagsRepository.saveTagsOnLogin()
        }
}


private fun saveToRoomDatabase(data: Person, viewModelScope: CoroutineScope, personDao: PersonDao) {
    // Save data to Room database
    viewModelScope.launch {
        personDao.addPerson(data)
    }
}

private fun loadAndSavePersonImage(
    storage: FirebaseStorage,
    documentId: String,
    userId: String,
    localFileStorageRepository: LocalFileStorageRepository,
    viewModelScope: CoroutineScope
) {
    val imageRef =
        storage.reference.child("images/$userId/$documentId/profile.jpg")

    val ONE_MEGABYTE: Long = 1024 * 1024
    imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener {
        //   downloadedImage = it
        val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)

        viewModelScope.launch {
            localFileStorageRepository.saveImageToInternalStorage("profile_$documentId", bitmap)
        }
        Log.d("Person Image", "Downloading Successful")

    }.addOnFailureListener { e ->
        Log.d("Person Image", "Downloading Failed")
        Log.d("Person Image", e.message.toString())
    }

}