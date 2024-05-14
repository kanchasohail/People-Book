package com.social.people_book.model.util.image_converters

import android.graphics.Bitmap
import java.io.ByteArrayOutputStream

fun getBytesFromBitmap(bitmap: Bitmap, compressFormat: Bitmap.CompressFormat, quality: Int): ByteArray? {
    val stream = ByteArrayOutputStream()
    bitmap.compress(compressFormat, quality, stream)
    return stream.toByteArray()
}