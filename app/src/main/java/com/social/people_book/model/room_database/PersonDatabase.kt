package com.social.people_book.model.room_database

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import java.io.ByteArrayOutputStream

@Database(entities = [PersonRoom::class], version = 1)
@TypeConverters(Converters::class)
abstract class PersonDatabase : RoomDatabase() {
    abstract fun personDao(): PersonDao

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
}