package com.social.people_book.model.util.image_converters

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri

fun getBitmapFromUri(uri: Uri, context: Context): Bitmap? {
    val parcelFileDescriptor = context.contentResolver.openFileDescriptor(uri, "r")
    val fileDescriptor = parcelFileDescriptor?.fileDescriptor
    var image: Bitmap? = null
    fileDescriptor?.let {
        image = BitmapFactory.decodeFileDescriptor(fileDescriptor)
    }
    parcelFileDescriptor?.close()
    return image
}