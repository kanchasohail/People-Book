package com.social.people_book.model.room_database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow


@Dao
interface PersonDao {
    @Query("SELECT * FROM personroom WHERE is_deleted = :isDeleted")
    fun getAll(isDeleted: Boolean = false): Flow<List<PersonRoom>>

    @Query("SELECT * FROM personroom WHERE is_deleted = :isDeleted")
    fun getAllDeletedPerson(isDeleted: Boolean = true): Flow<List<PersonRoom>>

    @Query("SELECT * FROM personroom WHERE id = :id LIMIT 1")
    fun getPersonById(id: Int): PersonRoom

    @Update
    suspend fun updatePerson(personRoom: PersonRoom)

    @Insert
    suspend fun addPerson(personRoom: PersonRoom): Long

    @Query("UPDATE personroom SET is_deleted = :isDeleted WHERE id = :personId")
    suspend fun deletePerson(personId: Long, isDeleted: Boolean = true)

    @Query("SELECT id FROM personroom WHERE is_deleted = :isDeleted")
    suspend fun getDeletedIds(isDeleted: Boolean = true): List<Long>

    @Query("DELETE FROM personroom WHERE is_deleted = :isDeleted")
    suspend fun emptyTrash(isDeleted: Boolean = true)


    @Query("DELETE FROM personroom WHERE is_deleted = :isDeleted AND id = :personId")
    suspend fun deletePersonFromTrash(personId: Long, isDeleted: Boolean = true)

}