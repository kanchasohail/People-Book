package com.social.people_book.room_database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [PersonRoom::class], version = 1)
abstract class PersonDatabase : RoomDatabase() {
    abstract fun personDao(): PersonDao

}