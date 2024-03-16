package com.social.people_book.room_database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow


@Dao
interface PersonDao {
    @Query("SELECT * FROM personroom")
    fun getAll(): Flow<List<PersonRoom>>

    @Query("SELECT * FROM personroom WHERE id = :id LIMIT 1")
    fun getPersonById(id: Int): PersonRoom

    @Update
    suspend fun updatePerson(personRoom: PersonRoom)

    @Insert
    suspend fun addPerson(personRoom: PersonRoom): Long

    @Delete
    suspend fun deletePerson(personRoom: PersonRoom)

}