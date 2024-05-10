package com.social.people_book.model.room_database

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Person(
    @PrimaryKey(autoGenerate = true) val id: Long?,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "number") val number: String?,
    @ColumnInfo(name = "email") val email: String?,
    @ColumnInfo(name = "about") val about: String?,
    @ColumnInfo(name = "image") val image: Bitmap?,
    @ColumnInfo(name = "is_deleted") val isDeleted: Boolean
) {
    fun doesMatchSearchQuery(query: String): Boolean {
        val matchingCombinations = listOf(
            name,
            "$number",
            "$email",
        )

        return matchingCombinations.any {
            it.contains(query, ignoreCase = true)
        }
    }

}