package com.dicoding.kulitku.helper
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import com.dicoding.kulitku.ml.Model2
import com.dicoding.kulitku.ml.ModelNew
import com.dicoding.kulitku.ml.SkinDetectionKulitku
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder

class SkinDetectionHelper(
    val context: Context,
    val classifierListener: ClassifierListener?
) {
    fun classifyStaticImage(imageUri: Uri) {
        val bitmap = decodeImage(imageUri)
        bitmap?.let {
            val resizedBitmap = Bitmap.createScaledBitmap(it, 224, 224, true)
            val floatBuffer = convertBitmapToFloatBuffer(resizedBitmap)

            val inputFeature0 =
                TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.FLOAT32)
            inputFeature0.loadBuffer(floatBuffer)

            try {
                val model = Model2.newInstance(context)
                val outputs = model.process(inputFeature0)
                val outputFeature0 = outputs.outputFeature0AsTensorBuffer
                val diseaseName = parseResult(outputFeature0)
                model.close()
                classifierListener?.onResult(diseaseName)
            } catch (e: IOException) {
                classifierListener?.onError("Error loading model")
                Log.e(TAG, "Error loading model", e)
            }
        } ?: run {
            classifierListener?.onError("Failed to decode image")
        }
    }

    private fun decodeImage(imageUri: Uri): Bitmap? {
        return try {
            val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val source = ImageDecoder.createSource(context.contentResolver, imageUri)
                ImageDecoder.decodeBitmap(source)
            } else {
                MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
            }
            bitmap.copy(Bitmap.Config.ARGB_8888, true)
        } catch (e: IOException) {
            Log.e(TAG, "Error decoding image", e)
            null
        }
    }

    private fun parseResult(output: TensorBuffer): String {
//        val result = output.floatArray[0]
////        val diseaseName = if (result > 0.5) "Kulit Bermasalah" else "Kulit Normal"
//        return "Hasil Deteksi : $result"
        val resultArray = output.floatArray
        // Assume you want to get the max value and its index
        val maxIndex = resultArray.indices.maxByOrNull { resultArray[it] } ?: -1
        val maxValue = resultArray[maxIndex]
        return "Hasil Deteksi : Index $maxIndex \n dengan nilai $maxValue"
    }


    private fun convertBitmapToFloatBuffer(bitmap: Bitmap): ByteBuffer {
        val byteBuffer = ByteBuffer.allocateDirect(4 * 224 * 224 * 3)
        byteBuffer.order(ByteOrder.nativeOrder())

        val intValues = IntArray(224 * 224)
        bitmap.getPixels(intValues, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
        var pixelIndex = 0
        for (i in 0 until 224) {
            for (j in 0 until 224) {
                val pixelValue = intValues[pixelIndex++]
                byteBuffer.putFloat(((pixelValue shr 16 and 0xFF) / 255.0f))
                byteBuffer.putFloat(((pixelValue shr 8 and 0xFF) / 255.0f))
                byteBuffer.putFloat(((pixelValue and 0xFF) / 255.0f))
            }
        }
        return byteBuffer
    }

    interface ClassifierListener {
        fun onError(error: String)
        fun onResult(result: String)
    }

    companion object {
        private const val TAG = "SKIN_DETECTION_HELPER"
    }
}




//package com.dicoding.kulitku.helper
//
//import android.content.Context
//import android.graphics.Bitmap
//import android.graphics.BitmapFactory
//import android.net.Uri
//import android.util.Log
//import org.tensorflow.lite.Interpreter
//import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
//import java.io.FileInputStream
//import java.nio.MappedByteBuffer
//import java.nio.channels.FileChannel
//
//class SkinDetectionHelper(
//    private val context: Context,
//    private val classifierListener: ClassifierListener
//) {
//    private lateinit var interpreter: Interpreter
//
//    interface ClassifierListener {
//        fun onError(error: String)
//        fun onResult(result: String)
//    }
//
//    init {
//        try {
//            interpreter = Interpreter(loadModelFile(context))
//        } catch (e: Exception) {
//            classifierListener.onError("Error initializing interpreter: ${e.message}")
//        }
//    }
//
//    private fun loadModelFile(context: Context): MappedByteBuffer {
//        val modelFileDescriptor = context.assets.openFd("skin_disease_model.tflite")
//        val inputStream = FileInputStream(modelFileDescriptor.fileDescriptor)
//        val fileChannel = inputStream.channel
//        val startOffset = modelFileDescriptor.startOffset
//        val declaredLength = modelFileDescriptor.declaredLength
//        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
//    }
//
//    fun classifyStaticImage(imageUri: Uri) {
//        val inputStream = context.contentResolver.openInputStream(imageUri)
//        val bitmap = BitmapFactory.decodeStream(inputStream)
//        classifyBitmap(bitmap)
//    }
//
//    fun classifyBitmap(bitmap: Bitmap) {
//        try {
//            val resizedImage = Bitmap.createScaledBitmap(bitmap, INPUT_IMAGE_SIZE, INPUT_IMAGE_SIZE, true)
//            val input = convertBitmapToFloatArray(resizedImage)
//
//            val inputShape = interpreter.getInputTensor(0).shape()
//            val inputDataType = interpreter.getInputTensor(0).dataType()
//            val inputBuffer = TensorBuffer.createFixedSize(inputShape, inputDataType)
//            inputBuffer.loadArray(input)
//
//            val outputShape = interpreter.getOutputTensor(0).shape()
//            val outputDataType = interpreter.getOutputTensor(0).dataType()
//            val outputBuffer = TensorBuffer.createFixedSize(outputShape, outputDataType)
//
//            interpreter.run(inputBuffer.buffer, outputBuffer.buffer)
//
//            val probabilities = outputBuffer.floatArray
//            val maxIndex = probabilities.indices.maxByOrNull { probabilities[it] } ?: -1
//            val result = if (maxIndex >= 0) SKIN_DISEASES[maxIndex] else "Unknown"
//
//            classifierListener.onResult(result)
//        } catch (e: Exception) {
//            Log.e(TAG, "Error classifying image: ${e.message}")
//            classifierListener.onError("Error classifying image")
//        }
//    }
//
//    private fun convertBitmapToFloatArray(bitmap: Bitmap): FloatArray {
//        val inputImage = Array(INPUT_IMAGE_SIZE) { FloatArray(INPUT_IMAGE_SIZE) }
//        for (x in 0 until INPUT_IMAGE_SIZE) {
//            for (y in 0 until INPUT_IMAGE_SIZE) {
//                val pixelValue = bitmap.getPixel(x, y)
//                inputImage[x][y] = ((pixelValue shr 16 and 0xFF) / 255.0f)
//            }
//        }
//        // Flatten the 2D array into 1D array
//        return inputImage.flatMap { it.toList() }.toFloatArray()
//    }
//
//    companion object {
//        private const val TAG = "SkinDetectionHelper"
//        private const val INPUT_IMAGE_SIZE = 224
//        private val SKIN_DISEASES = listOf("Disease A", "Disease B", "Disease C") // Ganti dengan nama penyakit sebenarnya
//    }
//}



//import android.content.Context
//import android.graphics.Bitmap
//import android.graphics.ImageDecoder
//import android.net.Uri
//import android.os.Build
//import android.provider.MediaStore
//import android.util.Log
//import com.dicoding.kulitku.ml.SkinDetectionKulitku
//import org.tensorflow.lite.DataType
//import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
//import java.io.BufferedReader
//import java.io.IOException
//import java.io.InputStreamReader
//import java.nio.ByteBuffer
//import java.nio.ByteOrder
//
//class SkinDetectionHelper(
//    val context: Context,
//    val classifierListener: ClassifierListener?
//) {
//
//    private var skinDiseaseDbHelper: SkinDiseaseDBHelper = SkinDiseaseDBHelper(context)
//
//    fun classifyStaticImage(imageUri: Uri) {
//        val bitmap = decodeImage(imageUri)
//        bitmap?.let {
//            val resizedBitmap = Bitmap.createScaledBitmap(it, 224, 224, true)
//            val floatBuffer = convertBitmapToFloatBuffer(resizedBitmap)
//
//            val inputFeature0 =
//                TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.FLOAT32)
//            inputFeature0.loadBuffer(floatBuffer)
//
//            try {
//                val model = SkinDetectionKulitku.newInstance(context)
//                val outputs = model.process(inputFeature0)
//                val outputFeature0 = outputs.outputFeature0AsTensorBuffer
//                val diseaseName = parseResult(outputFeature0)
//                model.close()
//
//                // Ambil rekomendasi obat dari SQLite berdasarkan nama penyakit
//                val medication = skinDiseaseDbHelper.getMedicationForDisease(diseaseName)
//                val resultText = "Hasil Deteksi: $diseaseName\nRekomendasi Obat: $medication"
//                classifierListener?.onResult(resultText)
//            } catch (e: IOException) {
//                classifierListener?.onError("Error loading model")
//                Log.e(TAG, "Error loading model", e)
//            }
//        } ?: run {
//            classifierListener?.onError("Failed to decode image")
//        }
//    }
//
//    private fun decodeImage(imageUri: Uri): Bitmap? {
//        return try {
//            val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//                val source = ImageDecoder.createSource(context.contentResolver, imageUri)
//                ImageDecoder.decodeBitmap(source)
//            } else {
//                MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
//            }
//            bitmap.copy(Bitmap.Config.ARGB_8888, true)
//        } catch (e: IOException) {
//            Log.e(TAG, "Error decoding image", e)
//            null
//        }
//    }
//
//    private fun parseResult(output: TensorBuffer): String {
//        // Misalnya, Anda ingin mengekstrak hasil klasifikasi dari TensorBuffer
//        // di sini Anda bisa menyesuaikan logika sesuai output model Anda
//        val result = output.floatArray[0]
//        return "$result"
//    }
//
//    private fun convertBitmapToFloatBuffer(bitmap: Bitmap): ByteBuffer {
//        val byteBuffer = ByteBuffer.allocateDirect(4 * 224 * 224 * 3)
//        byteBuffer.order(ByteOrder.nativeOrder())
//
//        val intValues = IntArray(224 * 224)
//        bitmap.getPixels(intValues, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
//        var pixelIndex = 0
//        for (i in 0 until 224) {
//            for (j in 0 until 224) {
//                val pixelValue = intValues[pixelIndex++]
//                byteBuffer.putFloat(((pixelValue shr 16 and 0xFF) / 255.0f))
//                byteBuffer.putFloat(((pixelValue shr 8 and 0xFF) / 255.0f))
//                byteBuffer.putFloat(((pixelValue and 0xFF) / 255.0f))
//            }
//        }
//        return byteBuffer
//    }
//
//    interface ClassifierListener {
//        fun onError(error: String)
//        fun onResult(result: String)
//    }
//
//    companion object {
//        private const val TAG = "SKIN_DETECTION_HELPER"
//    }
//}


//import android.content.Context
//import android.graphics.Bitmap
//import android.graphics.ImageDecoder
//import android.net.Uri
//import android.os.Build
//import android.provider.MediaStore
//import android.util.Log
//import com.dicoding.kulitku.ml.SkinDetectionKulitku
//import org.tensorflow.lite.DataType
//import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
//import java.io.IOException
//import java.nio.ByteBuffer
//import java.nio.ByteOrder
//
//class SkinDetectionHelper(
//    val context: Context,
//    val classifierListener: ClassifierListener?
//) {
//    fun classifyStaticImage(imageUri: Uri) {
//        val bitmap = decodeImage(imageUri)
//        bitmap?.let {
//            val resizedBitmap = Bitmap.createScaledBitmap(it, 224, 224, true)
//            val floatBuffer = convertBitmapToFloatBuffer(resizedBitmap)
//
//            val inputFeature0 =
//                TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.FLOAT32)
//            inputFeature0.loadBuffer(floatBuffer)
//
//            try {
//                val model = SkinDetectionKulitku.newInstance(context)
//                val outputs = model.process(inputFeature0)
//                val outputFeature0 = outputs.outputFeature0AsTensorBuffer
//                val diseaseName = parseResult(outputFeature0)
//                model.close()
//                classifierListener?.onResult(diseaseName)
//            } catch (e: IOException) {
//                classifierListener?.onError("Error loading model")
//                Log.e(TAG, "Error loading model", e)
//            }
//        } ?: run {
//            classifierListener?.onError("Failed to decode image")
//        }
//    }
//
//    private fun decodeImage(imageUri: Uri): Bitmap? {
//        return try {
//            val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//                val source = ImageDecoder.createSource(context.contentResolver, imageUri)
//                ImageDecoder.decodeBitmap(source)
//            } else {
//                MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
//            }
//            bitmap.copy(Bitmap.Config.ARGB_8888, true)
//        } catch (e: IOException) {
//            Log.e(TAG, "Error decoding image", e)
//            null
//        }
//    }
//
//    private fun parseResult(output: TensorBuffer): String {
//        val result = output.floatArray[0]
////        val diseaseName = if (result > 0.5) "Kulit Bermasalah" else "Kulit Normal"
//        return "Hasil Deteksi : $result"
//    }
//
//
//    private fun convertBitmapToFloatBuffer(bitmap: Bitmap): ByteBuffer {
//        val byteBuffer = ByteBuffer.allocateDirect(4 * 224 * 224 * 3)
//        byteBuffer.order(ByteOrder.nativeOrder())
//
//        val intValues = IntArray(224 * 224)
//        bitmap.getPixels(intValues, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
//        var pixelIndex = 0
//        for (i in 0 until 224) {
//            for (j in 0 until 224) {
//                val pixelValue = intValues[pixelIndex++]
//                byteBuffer.putFloat(((pixelValue shr 16 and 0xFF) / 255.0f))
//                byteBuffer.putFloat(((pixelValue shr 8 and 0xFF) / 255.0f))
//                byteBuffer.putFloat(((pixelValue and 0xFF) / 255.0f))
//            }
//        }
//        return byteBuffer
//    }
//
//    interface ClassifierListener {
//        fun onError(error: String)
//        fun onResult(result: String)
//    }
//
//    companion object {
//        private const val TAG = "SKIN_DETECTION_HELPER"
//    }
//}