package com.social.people_book.util.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.firebase.Firebase
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.ktx.storage
import com.social.people_book.model.room_database.PersonDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DeleteTrashPersonWork(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {

    private val db = PersonDatabase.getInstance(appContext)
    private val scope = CoroutineScope(Dispatchers.IO)
    override suspend fun doWork(): Result {
        val personId: Long = inputData.getLong("person_id", -1)

        val firebaseDb = Firebase.firestore
        val auth = com.google.firebase.ktx.Firebase.auth
        val storage = com.google.firebase.ktx.Firebase.storage

        scope.launch {
            //Delete from Room Database
            db.personDao().deletePersonFromTrash(personId)

            //Delete from firebase
            firebaseDb.collection("users").document(auth.currentUser?.uid.toString())
                .collection("persons").document(personId.toString()).delete()

            //Delete the image
            val imageRef =
                storage.reference.child("images/${auth.currentUser?.uid.toString()}/$personId/profile.jpg")
            imageRef.delete()

        }
        return Result.success()
    }

}