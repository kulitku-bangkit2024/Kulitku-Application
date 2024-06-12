package com.dicoding.kulitku.view

import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.dicoding.kulitku.data.Analyze
import com.dicoding.kulitku.databinding.ActivityResultScanBinding
import com.dicoding.kulitku.helper.SkinDetectionHelper

class ResultScanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultScanBinding

    private lateinit var skinDetectionHelper: SkinDetectionHelper
    private lateinit var analyzeViewModel: AnalyzeViewModel
    private var analyzeResult: Analyze? = null

    companion object {
        const val EXTRA_IMAGE_URI = "extra_image_uri"
        private const val REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 1
        private const val TAG = "ResultScanActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultScanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        analyzeViewModel = obtainViewModel(this@ResultScanActivity)

        val imageUriString = intent.getStringExtra(EXTRA_IMAGE_URI)
        val imageUri = Uri.parse(imageUriString)

        binding.progressBar.visibility = View.VISIBLE
        binding.resultImage.setImageURI(imageUri)

        // Inisialisasi SkinDetectionHelper
        skinDetectionHelper = SkinDetectionHelper(this, object : SkinDetectionHelper.ClassifierListener {
            override fun onError(error: String) {
                runOnUiThread {
                    showToast("Error analyzing image")
                    binding.progressBar.visibility = View.GONE
                }
            }

            override fun onResult(result: String) {
                runOnUiThread {
                    binding.progressBar.visibility = View.GONE
                    binding.resultType.text = result
                    analyzeResult = imageUriString?.let { Analyze(uri = it, type = result) }
                }
            }
        })

        // Mulai analisis gambar
        skinDetectionHelper.classifyStaticImage(imageUri)

        // Meminta izin menulis ke penyimpanan jika belum diberikan
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

        // Simpan hasil analisis ke dalam database
        binding.saveButton.setOnClickListener {
            if (analyzeResult == null) {
                showToast("Analyze insert Error")
            } else {
                analyzeViewModel.insert(analyzeResult!!)
                showToast("Analyze data inserted")
            }
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): AnalyzeViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(AnalyzeViewModel::class.java)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}



//package com.dicoding.kulitku.view
//
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
//import com.dicoding.kulitku.helper.SkinDetectionHelper
//
//class ResultScanActivity : AppCompatActivity() {
//    private lateinit var binding: ActivityResultScanBinding
//
//    private lateinit var skinDetectionHelper: SkinDetectionHelper
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
//            analyzeImage(it)
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
//
//    private fun showToast(message: String) {
//        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
//    }
//}



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