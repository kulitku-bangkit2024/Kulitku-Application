package com.dicoding.kulitku.view

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.dicoding.kulitku.R
import com.dicoding.kulitku.data.Analyze
import com.dicoding.kulitku.databinding.ActivityResultScanBinding
import com.dicoding.kulitku.helper.ImageClassifierHelper
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.text.NumberFormat

class ResultScanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultScanBinding
    private lateinit var imageClassifierHelper: ImageClassifierHelper
    private lateinit var analyzeViewModel: AnalyzeViewModel
    private var analyzeResult: Analyze? = null

    companion object {
        const val EXTRA_IMAGE_URI = "extra_image_uri"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultScanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // TODO: Menampilkan hasil gambar, prediksi, dan confidence score.

        analyzeViewModel = obtainViewModel(this@ResultScanActivity)

        val imageUri = Uri.parse(intent.getStringExtra(EXTRA_IMAGE_URI))
        binding.progressBar.visibility = View.VISIBLE
        imageUri.let {
            binding.resultImage.setImageURI(it)
            analyzeImage(it)
        }
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

    private fun analyzeImage(uri: Uri) {
        binding.progressBar.visibility = View.GONE
        imageClassifierHelper = ImageClassifierHelper(
            context = this,
            classifierListener = object : ImageClassifierHelper.ClassifierListener {
                override fun onError(error: String) {
                    runOnUiThread {
                        showToast("Error analyzing image")
                    }
                }

                override fun onResults(results: List<Classifications>?, inferenceTime: Long) {
                    runOnUiThread {
                        results?.let {
                            if (it.isNotEmpty() && it[0].categories.isNotEmpty()) {
                                println(it)
                                val processResult = it[0].categories[0]
                                val resultLabel = processResult.label
                                val resultScore = processResult.score
                                binding.resultText.text = getString(R.string.hasil_analisis)
                                binding.resultType.text =
                                    getString(R.string.analyze_type, resultLabel)
                                binding.resultScore.text = getString(
                                    R.string.analyze_score,
                                    NumberFormat.getPercentInstance().format(resultScore).toString()
                                )
                                binding.resultInferenceTime.text =
                                    getString(R.string.analyze_time, inferenceTime.toString())

                                analyzeResult = Analyze(
                                    uri = uri.toString(),
                                    type = resultLabel,
                                    confidence = resultScore
                                )

                            } else {
                                binding.resultText.text = getString(R.string.analyze_error)
                            }
                        }
                    }
                }

            }
        )

        imageClassifierHelper.classifyStaticImage(uri)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}