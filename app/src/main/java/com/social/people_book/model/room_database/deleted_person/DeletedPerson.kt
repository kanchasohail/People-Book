package com.social.people_book.model.room_database.deleted_person

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Date

@Entity
data class DeletedPerson(
    @PrimaryKey(autoGenerate = true) val id: Long?,
    @ColumnInfo(name = "person_id") val personId: Long,
    @ColumnInfo("deleted_date") val deletedDate: Date
)