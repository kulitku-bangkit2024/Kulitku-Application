package com.dicoding.kulitku.view

import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.dicoding.kulitku.R
import com.dicoding.kulitku.data.Analyze
import com.dicoding.kulitku.databinding.ActivityResultScanBinding
import java.io.File

class ResultScanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultScanBinding

    private lateinit var analyzeViewModel: AnalyzeViewModel
//    private var analyzeResult: Analyze? = null
//    private var analyzeResultList: MutableList<Analyze> = mutableListOf()

    companion object {
        const val RESULT_IMAGE = "RESULT_IMAGE"
        const val RESULT_TITLE = "RESULT_TITLE"
        const val RESULT_CONFIDENCE = "RESULT_CONFIDENCE"
        const val RESULT_MEDICINE = "result_medicine"
        private const val REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 1
        private const val TAG = "ResultScanActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultScanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        analyzeViewModel = obtainViewModel(this@ResultScanActivity)

        // Menampilkan hasil gambar dan analisis
//        val byteArray = intent.getByteArrayExtra(RESULT_IMAGE)
        val imageUriString = intent.getStringExtra(RESULT_IMAGE)
        Log.d(TAG, imageUriString.toString())
        val imageUri = imageUriString?.let { Uri.parse(it) }
        Log.d(TAG, imageUri.toString())
        val title = intent.getStringExtra(RESULT_TITLE)
        val confidence = intent.getFloatExtra(RESULT_CONFIDENCE, 0.0f)
        val recommendedMedicine = intent.getStringExtra(RESULT_MEDICINE)

        imageUri?.let {
            binding.resultImage.setImageURI(it)
        }
        binding.resultType.text = title
        binding.resultScore.text = confidence.toString()
        binding.resultMedicine.text = "Recommended Medicine: $recommendedMedicine"

        // Request write permission at runtime
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_CODE_WRITE_EXTERNAL_STORAGE
            )
        }

        // Save result to history
        binding.saveButton.setOnClickListener {
            val analyzeResult = Analyze(
                uri = imageUriString ?: "",
                type = title ?: "",
                confidence = confidence
            )
//            analyzeResultList.add(analyzeResult)
            analyzeViewModel.insert(analyzeResult)
            showToast("Analyze data inserted")
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): AnalyzeViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(AnalyzeViewModel::class.java)
    }

    private fun byteArrayToUri(byteArray: ByteArray): Uri {
        val file = File(cacheDir, "image.png")
        file.writeBytes(byteArray)
        return Uri.fromFile(file)
    }


//    private fun analyzeImage(uri: Uri) {
//        binding.progressBar.visibility = View.GONE
//        skinDetectionHelper = SkinDetectionHelper(
//            context = this,
//            classifierListener = object : SkinDetectionHelper.ClassifierListener {
//                override fun onError(error: String) {
//                    runOnUiThread {
//                        showToast("Error analyzing image")
//                    }
//                }
//
//                override fun onResult(result: String) {
//                    runOnUiThread {
//                        binding.progressBar.visibility = View.GONE
//
//                        binding.resultType.text = result
//
//                        Log.i(TAG, "Hasil Deteksi $result")
//
//                        analyzeResult = Analyze(
//                            uri = uri.toString(),
//                            type = result
//                        )
//                    }
//                }
//            }
//        )
//
//        skinDetectionHelper.classifyStaticImage(uri)
//    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}



//package com.dicoding.kulitku.view
//
//import SkinModelHelper
//import android.content.pm.PackageManager
//import android.net.Uri
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import android.util.Log
//import android.view.View
//import android.widget.Toast
//import androidx.core.app.ActivityCompat
//import androidx.core.content.ContextCompat
//import androidx.lifecycle.ViewModelProvider
//import com.dicoding.kulitku.data.Analyze
//import com.dicoding.kulitku.databinding.ActivityResultScanBinding
//
//class ResultScanActivity : AppCompatActivity() {
//    private lateinit var binding: ActivityResultScanBinding
//
//    private lateinit var skinDetectionHelper: SkinModelHelper
//    private lateinit var analyzeViewModel: AnalyzeViewModel
//    private var analyzeResult: Analyze? = null
//
//    companion object {
//        const val EXTRA_IMAGE_URI = "extra_image_uri"
//        private const val REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 1
//        private const val TAG = "ResultScanActivity"
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityResultScanBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        analyzeViewModel = obtainViewModel(this@ResultScanActivity)
//
//        val imageUri = Uri.parse(intent.getStringExtra(EXTRA_IMAGE_URI))
//        binding.progressBar.visibility = View.VISIBLE
//        imageUri.let {
//            binding.resultImage.setImageURI(it)
//            loadModelAndAnalyzeImage(it)
//        }
//
//        // Request write permission at runtime
//        if (ContextCompat.checkSelfPermission(
//                this,
//                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            ActivityCompat.requestPermissions(
//                this,
//                arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
//                REQUEST_CODE_WRITE_EXTERNAL_STORAGE
//            )
//        }
//
//        // Save result to history
//        binding.saveButton.setOnClickListener {
//            if (analyzeResult == null) {
//                showToast("Analyze insert Error")
//            } else {
//                analyzeViewModel.insert(analyzeResult!!)
//                showToast("Analyze data inserted")
//            }
//        }
//    }
//
//    private fun obtainViewModel(activity: AppCompatActivity): AnalyzeViewModel {
//        val factory = ViewModelFactory.getInstance(activity.application)
//        return ViewModelProvider(activity, factory).get(AnalyzeViewModel::class.java)
//    }
//
//    private fun loadModelAndAnalyzeImage(uri: Uri) {
//        skinDetectionHelper = SkinModelHelper(
//            context = this,
//            classifierListener = object : SkinModelHelper.ClassifierListener {
//                override fun onError(error: String) {
//                    runOnUiThread {
//                        showToast("Error analyzing image")
//                    }
//                }
//
//                override fun onResult(result: String) {
//                    runOnUiThread {
//                        binding.progressBar.visibility = View.GONE
//                        binding.resultType.text = result
//                        Log.i(TAG, "Hasil Deteksi $result")
//
//                        analyzeResult = Analyze(
//                            uri = uri.toString(),
//                            type = result
//                        )
//                    }
//                }
//            }
//        )
//
//        // Load model from JSON
//        skinDetectionHelper.loadModelFromJSON("model.json")
//
//        // Classify image
//        skinDetectionHelper.classifyStaticImage(uri)
//    }
//
//    private fun showToast(message: String) {
//        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
//    }
//}