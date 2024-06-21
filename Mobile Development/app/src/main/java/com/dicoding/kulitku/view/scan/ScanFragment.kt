package com.dicoding.kulitku.view.scan

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.dicoding.kulitku.helper.Classifier
import com.dicoding.kulitku.R
import com.dicoding.kulitku.api.DiseaseData
import com.dicoding.kulitku.api.Product
import com.dicoding.kulitku.databinding.FragmentScanBinding
import com.dicoding.kulitku.view.createCustomTempFile
import com.dicoding.kulitku.view.uriToFile
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.lang.reflect.Type

class ScanFragment : Fragment() {
    private var _binding: FragmentScanBinding? = null
    private val binding get() = _binding!!
    private var getFile: File? = null
    private lateinit var currentPhotoPath: String

    private lateinit var classifier: Classifier
    private lateinit var classifierSkintype: Classifier
    private lateinit var mBitmap: Bitmap

    private val mInputSize = 224
    private val mModelPath = "model_skin_disease.tflite"
    private val mLabelPath = "disease_names.txt"
    private val mJsonPath = "disease_names.json"

    private val mModelPathSkinType = "model_skin_types.tflite"
    private val mLabelPathSkinType = "skin_types.txt"
    private val mJsonPathSkinType = "skin_types.json"

    private lateinit var diseaseDataList: List<DiseaseData>
    private lateinit var skinTypeDataList: List<Product>

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

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
        classifierSkintype =
            Classifier(requireContext().assets, mModelPathSkinType, mLabelPathSkinType, mInputSize)
        // Load disease data from JSON
        loadDiseaseData()
        // Load skin type data from JSON
        loadSkinTypeData()

        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                showToast("Permission request granted")
            } else {
                showToast("Permission request denied")
            }
        }

        checkCameraPermission()

        binding.btnGallery.setOnClickListener { startGallery() }
        binding.btnCamera.setOnClickListener { startCamera() }
        binding.btnAnalyze.setOnClickListener {
            getFile?.let {
                navigateToResult(it)
            } ?: run {
                showToast(getString(R.string.empty_image_warning))
            }
        }
    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    // CAMERA
    private fun startCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(requireActivity().packageManager)

        createCustomTempFile(requireActivity().application).also {
            val photoUri: Uri = FileProvider.getUriForFile(
                requireContext(), getString(R.string.package_name), it
            )

            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
            launcherIntentCamera.launch(intent)
        }
    }

    private val launcherIntentCamera =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == AppCompatActivity.RESULT_OK) {
                val myFile = File(currentPhotoPath)
                getFile = myFile

                val result = BitmapFactory.decodeFile(getFile?.path)
                binding.previewImageView.setImageBitmap(result)

                analyzeImage(myFile)
            }
        }

    // GALLERY
    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"

        val chooser = Intent.createChooser(intent, getString(R.string.choose_picture))
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                val imageUri: Uri = result.data?.data as Uri
                val myFile = uriToFile(imageUri, requireContext())

                Log.d(TAG, imageUri.toString())
                Log.d(TAG, myFile.toString())

                myFile.let { file ->
                    getFile = file
                    binding.previewImageView.setImageBitmap(BitmapFactory.decodeFile(file.path))
                }

                getFile = myFile
                binding.previewImageView.setImageURI(imageUri)

                analyzeImage(myFile)
            }
        }

    private fun analyzeImage(file: File) {
        try {
            mBitmap = BitmapFactory.decodeFile(file.path)

            val diseaseResults = classifier.recognizeImage(mBitmap).firstOrNull()
            val skinTypeResults = classifierSkintype.recognizeImage(mBitmap).firstOrNull()

            if (diseaseResults != null && skinTypeResults != null) {
                binding.textScan.text = getString(R.string.photo_suitable)
                binding.textScan.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
                binding.imageViewSuitable.visibility = View.VISIBLE
                binding.imageViewNotSuitable.visibility = View.GONE
                binding.validationLayout.setBackgroundResource(R.drawable.rounded_background_validation_scan)
                binding.textAnalysisResult.text = getString(R.string.analysis_result)
            } else {
                binding.textScan.text = getString(R.string.error_no_result)
                binding.textScan.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
                binding.imageViewSuitable.visibility = View.GONE
                binding.imageViewNotSuitable.visibility = View.VISIBLE
                binding.validationLayout.setBackgroundResource(R.drawable.rounded_background_validation_scan)
                binding.textAnalysisResult.text = "Please take another photo"
            }

        } catch (e: IOException) {
            e.printStackTrace()
            binding.textScan.text = getString(R.string.error_loading_image)
            binding.textScan.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
            binding.imageViewSuitable.visibility = View.GONE
            binding.imageViewNotSuitable.visibility = View.VISIBLE
            binding.textAnalysisResult.text = ""
            showToast(getString(R.string.error_loading_image))
        }
    }

    private fun navigateToResult(file: File) {
        try {
            mBitmap = BitmapFactory.decodeFile(file.path)

            val diseaseResults = classifier.recognizeImage(mBitmap).firstOrNull()
            val skinTypeResults = classifierSkintype.recognizeImage(mBitmap).firstOrNull()

            diseaseResults?.let { disease ->
                skinTypeResults?.let { skinType ->
                    val diseaseName = disease.title
                    val skinTypeName = skinType.title
                    val recommendedMedicine = getMedicineRecommendation(diseaseName)
                    val recommendedProducts = getProductRecommendation(skinTypeName)

                    val intent = Intent(requireContext(), ResultScanActivity::class.java).apply {
                        putExtra(ResultScanActivity.RESULT_IMAGE, getFile?.absolutePath)

                        putExtra(ResultScanActivity.RESULT_TITLE, diseaseName)
                        putExtra(ResultScanActivity.RESULT_CONFIDENCE, disease.confidence)
                        putExtra(ResultScanActivity.RESULT_MEDICINE, recommendedMedicine)

                        putExtra(ResultScanActivity.RESULT_SKIN_TYPE, skinTypeName)
                        putExtra(ResultScanActivity.RESULT_SKIN_CONFIDENCE, skinType.confidence)
                        putExtra(ResultScanActivity.RESULT_PRODUCTS, Gson().toJson(recommendedProducts))
                    }
                    startActivity(intent)
                } ?: run {
                    showToast(getString(R.string.error_no_skin_type_result))
                }
            } ?: run {
                showToast(getString(R.string.error_no_result))
            }
        } catch (e: IOException) {
            e.printStackTrace()
            showToast(getString(R.string.error_loading_image))
        }
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
            Toast.makeText(
                requireContext(),
                "Failed to load disease data",
                Toast.LENGTH_SHORT
            )
                .show()
        }
    }

    private fun loadSkinTypeData() {
        try {
            val inputStream: InputStream = requireContext().assets.open(mJsonPathSkinType)
            val json = inputStream.bufferedReader().use { it.readText() }
            val type: Type = object : TypeToken<List<Product>>() {}.type
            skinTypeDataList = Gson().fromJson(json, type)
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(
                requireContext(),
                "Failed to load skin type data",
                Toast.LENGTH_SHORT
            )
                .show()
        }
    }

    private fun getMedicineRecommendation(diseaseName: String): String {
        val disease =
            diseaseDataList.find { it.nama_penyakit.equals(diseaseName, ignoreCase = true) }
        return disease?.obat_rekomendasi ?: "No recommendation available"
    }

    private fun getProductRecommendation(skinTypeName: String): List<Product> {
        return skinTypeDataList.filter {
            it.jenis_kulit.equals(
                skinTypeName,
                ignoreCase = true
            )
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        Log.d(TAG, message)
    }

    companion object {
        const val TAG = "ScanFragment"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
