package com.dicoding.kulitku.view

import android.content.Context
import android.net.Uri
import android.os.Environment
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Locale

fun createCustomTempFile(context: Context): File {
    val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(
        SimpleDateFormat("dd-MMM-yyyy", Locale.US).format(System.currentTimeMillis()),
        ".jpg",
        storageDir
    )
}

fun uriToFile(imageUri: Uri, context: Context): File {
    val myFile = createCustomTempFile(context)
    try {
        context.contentResolver.openInputStream(imageUri)?.use { inputStream ->
            FileOutputStream(myFile).use { outputStream ->
                val buffer = ByteArray(4 * 1024)
                var read: Int
                while (inputStream.read(buffer).also { read = it } != -1) {
                    outputStream.write(buffer, 0, read)
                }
                outputStream.flush()
            }
        }
    } catch (e: IOException) {
        e.printStackTrace()
        throw IOException("Error copying image file", e)
    }
    return myFile
}
