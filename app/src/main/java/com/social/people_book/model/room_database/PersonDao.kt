package com.social.people_book.model.room_database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import java.sql.Date


@Dao
interface PersonDao {
    @Query("SELECT * FROM person WHERE is_deleted = :isDeleted ORDER BY id DESC")
    fun getAll(isDeleted: Boolean = false): Flow<List<Person>>


    @Query("SELECT * FROM person WHERE is_favorite = :isFavorite ORDER BY id DESC")
    fun getAllFavorite(isFavorite: Boolean = true): Flow<List<Person>>

    @Query("SELECT * FROM person WHERE is_deleted = :isDeleted ORDER BY id DESC")
    fun getAllDeletedPerson(isDeleted: Boolean = true): Flow<List<Person>>

    @Query("SELECT * FROM person WHERE id = :id LIMIT 1")
    fun getPersonById(id: Long): Person

    @Update
    suspend fun updatePerson(person: Person)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPerson(person: Person): Long

    @Query("UPDATE person SET is_deleted = :isDeleted , deleted_at = :deletedAt WHERE id = :personId")
    suspend fun deletePerson(personId: Long, deletedAt: Date, isDeleted: Boolean = true)

    @Query("UPDATE person SET is_deleted = :isDeleted , deleted_at = :deletedAt WHERE id = :personId")
    suspend fun restorePerson(personId: Long, deletedAt: Date? = null, isDeleted: Boolean = false)

    @Query("SELECT id FROM person WHERE is_deleted = :isDeleted")
    suspend fun getDeletedIds(isDeleted: Boolean = true): List<Long>

    @Query("DELETE FROM person WHERE is_deleted = :isDeleted")
    suspend fun emptyTrash(isDeleted: Boolean = true)


    @Query("DELETE FROM person WHERE is_deleted = :isDeleted AND id = :personId")
    suspend fun deletePersonFromTrash(personId: Long, isDeleted: Boolean = true)

   @Query("DELETE FROM person")
    suspend fun clearDatabase()

}