package com.social.people_book.model.room_database

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.social.people_book.model.room_database.deleted_person.DeletedPerson
import com.social.people_book.model.room_database.deleted_person.DeletedPersonDao
import java.io.ByteArrayOutputStream
import java.sql.Date

@Database(entities = [Person::class, DeletedPerson::class], version = 1)
@TypeConverters(Converters::class)
abstract class PersonDatabase : RoomDatabase() {
    abstract fun personDao(): PersonDao

    abstract fun DeletedPersonDao(): DeletedPersonDao


    companion object {
        @Volatile
        private var INSTANCE: PersonDatabase? = null

        fun getInstance(context: Context): PersonDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PersonDatabase::class.java,
                    "people_book.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

class Converters {

    @TypeConverter
    fun fromBitmap(bmp: Bitmap?): ByteArray {
        val outputStream = ByteArrayOutputStream()
        bmp?.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        return outputStream.toByteArray()
    }

    @TypeConverter
    fun toBitmap(bytes: ByteArray?): Bitmap? {
        return bytes?.let { BitmapFactory.decodeByteArray(bytes, 0, it.size) }
    }


    @TypeConverter
    fun fromTimeStamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}