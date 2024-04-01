package com.social.people_book.model.room_database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PersonRoom(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "number") val number: String?,
    @ColumnInfo(name = "email") val email: String?,
    @ColumnInfo(name = "about") val about: String?
)