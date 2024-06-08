import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
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
                val model = SkinDetectionKulitku.newInstance(context)
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
        val result = output.floatArray[0]
//        val diseaseName = if (result > 0.5) "Kulit Bermasalah" else "Kulit Normal"
        return "Hasil Deteksi : $result"
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