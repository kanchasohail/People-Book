package com.social.people_book.model.util.image_converters

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File

fun loadImageBitmap(imageFileName: String?, context: Context): Bitmap? {
    return if (imageFileName != null) {
        val imagesDir = context.filesDir
        val file = File(imagesDir, imageFileName)
        BitmapFactory.decodeFile(file.absolutePath)
    } else {
        null
    }
}