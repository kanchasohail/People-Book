package com.social.people_book.model.util.image_converters

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException

@Throws(IOException::class)
fun compressImage(context: Context, originalUri: Uri): Uri {
    val contentResolver = context.contentResolver
    val inputStream = contentResolver.openInputStream(originalUri)
    val imageSizeBytes = inputStream?.available() ?: 0
    inputStream?.close()

    // Check if the image size is greater than 50KB
    if (imageSizeBytes > 50 * 1024) {
        var compressQuality = 100
        var compressedImageUri: Uri?

        // Step 1: Create a ByteArrayOutputStream
        val bytes = ByteArrayOutputStream()

        // Step 2: Get the bitmap from the original URI
        val originalBitmap = MediaStore.Images.Media.getBitmap(contentResolver, originalUri)

        // Step 3: Compress the bitmap into the ByteArrayOutputStream
        do {
            bytes.reset()
            originalBitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bytes)
            val compressedImageFile = File(context.cacheDir, "compressed_image.jpg")

            // Step 4: Write the compressed bytes to the file
            compressedImageFile.outputStream().use { output ->
                output.write(bytes.toByteArray())
            }

            compressedImageUri = Uri.fromFile(compressedImageFile)
            val compressedImageSize = compressedImageFile.length()

            // Reduce the quality if the compressed image size is still greater than 50KB
            compressQuality -= 10
        } while (compressedImageSize > 50 * 1024 && compressQuality >= 10)

        // Step 5: Return the URI of the compressed image
        return compressedImageUri ?: originalUri
    } else {
        // Return the original URI if the image size is less than or equal to 50KB
        return originalUri
    }
}

//@RequiresApi(Build.VERSION_CODES.P)
//@Throws(IOException::class)
//fun compressImage(context: Context, originalUri: Uri): Uri {
//    val contentResolver = context.contentResolver
//    val inputStream = contentResolver.openInputStream(originalUri)
//    val imageSizeBytes = inputStream?.available() ?: 0
//    inputStream?.close()
//
//    // Check if the image size is greater than 50KB
//    if (imageSizeBytes > 50 * 1024) {
//        var compressQuality = 100
//        var compressedImageUri: Uri?
//
//        // Step 1: Create a ByteArrayOutputStream
//        val bytes = ByteArrayOutputStream()
//
//        // Step 2: Get the bitmap from the original URI using ImageDecoder
//        val source = ImageDecoder.createSource(contentResolver, originalUri)
//        val originalBitmap = ImageDecoder.decodeBitmap(source) { decoder, _, _ ->
//            decoder.allocator = ImageDecoder.ALLOCATOR_SOFTWARE
//        }
//
//        // Step 3: Compress the bitmap into the ByteArrayOutputStream
//        do {
//            bytes.reset()
//            originalBitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bytes)
//            val compressedImageFile = File(context.cacheDir, "compressed_image.jpg")
//
//            // Step 4: Write the compressed bytes to the file
//            compressedImageFile.outputStream().use { output ->
//                output.write(bytes.toByteArray())
//            }
//
//            compressedImageUri = Uri.fromFile(compressedImageFile)
//            val compressedImageSize = compressedImageFile.length()
//
//            // Reduce the quality if the compressed image size is still greater than 50KB
//            compressQuality -= 10
//        } while (compressedImageSize > 50 * 1024 && compressQuality >= 10)
//
//        // Step 5: Return the URI of the compressed image
//        return compressedImageUri ?: originalUri
//    } else {
//        // Return the original URI if the image size is less than or equal to 50KB
//        return originalUri
//    }
//}