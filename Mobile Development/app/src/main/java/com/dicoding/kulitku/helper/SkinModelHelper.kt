import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import org.json.JSONObject
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder

class SkinModelHelper(
    val context: Context,
    val classifierListener: ClassifierListener?
) {
    private var modelJson: String? = null

    fun loadModelFromJSON(filename: String) {
        modelJson = loadJSONFromAsset(filename)
    }

    fun classifyStaticImage(imageUri: Uri) {
        val bitmap = decodeImage(imageUri)
        bitmap?.let {
            val resizedBitmap = Bitmap.createScaledBitmap(it, 224, 224, true)
            val floatBuffer = convertBitmapToFloatBuffer(resizedBitmap)

            // Pastikan modelJson tidak null sebelum memproses
            modelJson?.let { json ->
                try {
                    val jsonObject = JSONObject(json)
                    val diseaseName = parseResult(jsonObject, floatBuffer)
                    classifierListener?.onResult(diseaseName)
                } catch (e: Exception) {
                    classifierListener?.onError("Error parsing JSON or classifying image")
                    Log.e(TAG, "Error parsing JSON or classifying image", e)
                }
            } ?: run {
                classifierListener?.onError("Model JSON is not loaded")
            }
        } ?: run {
            classifierListener?.onError("Failed to decode image")
        }
    }

    private fun loadJSONFromAsset(filename: String): String {
        var json = ""
        try {
            val inputStream = context.assets.open(filename)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            json = String(buffer, Charsets.UTF_8)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return json
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

    private fun parseResult(jsonObject: JSONObject, inputBuffer: ByteBuffer): String {
        // Ubah logika ini sesuai dengan struktur JSON dan kebutuhan aplikasi Anda
        val modelFileName = jsonObject.getString("modelFileName")
        val threshold = jsonObject.getDouble("threshold")

        // Proses klasifikasi berdasarkan modelFileName dan inputBuffer
        // Misalnya, menggunakan nilai ambang dan nama model untuk klasifikasi
        // Anda bisa menyesuaikan ini sesuai dengan logika aplikasi Anda

        // Contoh sederhana:
        return "Hasil Deteksi: ${classify(inputBuffer, modelFileName, threshold)}"
    }

    private fun classify(inputBuffer: ByteBuffer, modelFileName: String, threshold: Double): String {
        // Implementasikan logika klasifikasi berdasarkan modelFileName dan inputBuffer di sini
        // Menggunakan nilai ambang untuk menentukan hasil
        // Anda harus mengubah bagian ini sesuai dengan logika klasifikasi Anda yang sebenarnya
        return if (threshold > 0.5) {
            "Kulit Bermasalah"
        } else {
            "Kulit Normal"
        }
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
