package com.social.people_book.model.room_database.deleted_person

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DeletedPersonDao {
    @Query("SELECT * FROM deletedperson")
    fun getAllDeletedPerson(): Flow<List<DeletedPerson>>

    @Insert
    suspend fun deletePerson(deletedPerson: DeletedPerson): Long

    @Query("DELETE FROM deletedperson")
    suspend fun emptyTrash()

    @Query("DELETE FROM deletedperson WHERE person_id = :personId ")
    suspend fun clearDeletedPerson(personId:Long)

}