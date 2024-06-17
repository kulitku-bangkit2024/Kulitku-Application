package com.dicoding.kulitku.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import android.os.Environment
import androidx.exifinterface.media.ExifInterface
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

//private const val MAXIMAL_SIZE = 1000000
private const val FILENAME_FORMAT = "dd-MMM-yyyy"
private val timeStamp: String = SimpleDateFormat(
    FILENAME_FORMAT, Locale.US
).format(System.currentTimeMillis())

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

//fun Bitmap.getRotatedBitmap(file: File): Bitmap? {
//    val orientation = ExifInterface(file).getAttributeInt(
//        ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED
//    )
//
//    return when (orientation) {
//        ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(this, 90F)
//        ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(this, 180F)
//        ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(this, 270F)
//        ExifInterface.ORIENTATION_NORMAL -> this
//        else -> this
//    }
//}
//
//fun rotateImage(source: Bitmap, angle: Float): Bitmap? {
//    val matrix = Matrix()
//    matrix.postRotate(angle)
//    return Bitmap.createBitmap(
//        source, 0, 0, source.width, source.height, matrix, true
//    )
//}

fun formatDateToString(dateString: String): String {
    val inputDateFormat =
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    val outputDateFormat = SimpleDateFormat("EEEE, dd MMMM yyyy | HH:mm", Locale.getDefault())

    val date: Date?
    var outputDate = ""

    try {
        date = inputDateFormat.parse(dateString)
        outputDate = outputDateFormat.format(date!!)
    } catch (e: ParseException) {
        e.printStackTrace()
    }

    return outputDate
}
