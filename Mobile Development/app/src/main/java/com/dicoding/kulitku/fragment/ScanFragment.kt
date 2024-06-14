package com.dicoding.kulitku.fragment

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import com.dicoding.kulitku.helper.Classifier
import com.dicoding.kulitku.R
import com.dicoding.kulitku.api.DiseaseData
import com.dicoding.kulitku.databinding.FragmentScanBinding
import com.dicoding.kulitku.view.ResultScanActivity
import com.dicoding.kulitku.view.getImageUri
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.lang.reflect.Type

class ScanFragment : Fragment() {
    private var _binding: FragmentScanBinding? = null
    private val binding get() = _binding!!
    private var currentImageUri: Uri? = null

    private lateinit var classifier: Classifier
    private lateinit var mBitmap: Bitmap

    private val mInputSize = 224
    private val mModelPath = "model2.tflite"
    private val mLabelPath = "disease_names.txt"
    private val mJsonPath = "label.json"

    private lateinit var diseaseDataList: List<DiseaseData>

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var launcherGallery: ActivityResultLauncher<PickVisualMediaRequest>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentScanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        classifier = Classifier(requireContext().assets, mModelPath, mLabelPath, mInputSize)
        // Load disease data from JSON
        loadDiseaseData()

        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                showToast("Permission request granted")
            } else {
                showToast("Permission request denied")
            }
        }

        launcherGallery = registerForActivityResult(
            ActivityResultContracts.PickVisualMedia()
        ) { uri: Uri? ->
            if (uri != null) {
                currentImageUri = uri
                Log.d("SCAN FRAGMENT", "$currentImageUri")
                showImage()
            } else {
                Log.d("Pilih Photo", "Tidak ada media")
            }
        }

        if (!checkForPermission()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requestPermissionLauncher.launch(READ_MEDIA_IMAGES)
            } else {
                requestPermissionLauncher.launch(READ_EXTERNAL_STORAGE)
            }
        }

        binding.btnGallery.setOnClickListener { startGallery() }
        binding.btnCamera.setOnClickListener { startCamera() }
        binding.btnAnalyze.setOnClickListener {
            currentImageUri?.let {
                analyzeImage(it)
            } ?: run {
                showToast(getString(R.string.empty_image_warning))
            }
        }
    }

    private fun checkForPermission(): Boolean {
        val requiredPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            READ_MEDIA_IMAGES
        } else {
            READ_EXTERNAL_STORAGE
        }

        val permission = ContextCompat.checkSelfPermission(
            requireContext(),
            requiredPermission
        )
        return permission == PackageManager.PERMISSION_GRANTED
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun startCamera() {
        currentImageUri = getImageUri(requireContext())
        launcherIntentCamera.launch(currentImageUri)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.previewImageView.setImageURI(it)
            try {
                mBitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, it)
            } catch (e: IOException) {
                e.printStackTrace()
                showToast(getString(R.string.error_loading_image))
            }
        }
    }

    private fun analyzeImage(uri: Uri) {
//        moveToResult(uri)
        val results = classifier.recognizeImage(mBitmap).firstOrNull()
        results?.let {
            val diseaseName = it.title
            val recommendedMedicine = getMedicineRecommendation(diseaseName)
            navigateToResult(it.title, it.confidence, recommendedMedicine)
        } ?: run {
            Toast.makeText(requireContext(), "No results from detection", Toast.LENGTH_SHORT).show()
        }
    }


    private fun navigateToResult(title: String, confidence: Float, recommendedMedicine: String) {
        val stream = ByteArrayOutputStream()
        mBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val byteArray = stream.toByteArray()

        Log.d("SCAN FRAGMENT", "$mBitmap")

        val intent = Intent(requireContext(), ResultScanActivity::class.java).apply {
            putExtra(ResultScanActivity.RESULT_IMAGE, currentImageUri.toString())
            putExtra(ResultScanActivity.RESULT_TITLE, title)
            putExtra(ResultScanActivity.RESULT_CONFIDENCE, confidence)
            putExtra(ResultScanActivity.RESULT_MEDICINE, recommendedMedicine)
        }
        startActivity(intent)
    }

    private fun loadDiseaseData() {
        try {
            val inputStream: InputStream = requireContext().assets.open(mJsonPath)
            val json = inputStream.bufferedReader().use { it.readText() }
            val gson = Gson()
            val type: Type = object : TypeToken<List<DiseaseData>>() {}.type
            diseaseDataList = gson.fromJson(json, type)
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "Failed to load disease data", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getMedicineRecommendation(diseaseName: String): String {
        val disease =
            diseaseDataList.find { it.nama_penyakit.equals(diseaseName, ignoreCase = true) }
        return disease?.obat_rekomendasi ?: "No recommendation available"
    }

//    private fun handleResult(result: String) {
//        currentImageUri?.let { uri ->
//            moveToResult(uri, result)
//        }
//    }
//
//    private fun moveToResult(title: String, confidence: Float) {
//        val stream = ByteArrayOutputStream()
//        mBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
//        val byteArray = stream.toByteArray()
//
//        val intent = Intent(requireContext(), ResultScanActivity::class.java).apply {
//            putExtra(ResultScanActivity.RESULT_IMAGE, byteArray)
//            putExtra(ResultScanActivity.RESULT_TITLE, title)
//            putExtra(ResultScanActivity.RESULT_CONFIDENCE, confidence)
//        }
//        startActivity(intent)
//    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        private const val READ_MEDIA_IMAGES = Manifest.permission.READ_MEDIA_IMAGES
        private const val READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
