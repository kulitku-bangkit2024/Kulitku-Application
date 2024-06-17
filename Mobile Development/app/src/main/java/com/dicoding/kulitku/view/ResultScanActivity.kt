package com.dicoding.kulitku.view

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.dicoding.kulitku.MainActivity
import com.dicoding.kulitku.R
import com.dicoding.kulitku.api.Product
import com.dicoding.kulitku.data.Analyze
import com.dicoding.kulitku.databinding.ActivityResultScanBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.NumberFormat
import kotlin.random.Random

class ResultScanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultScanBinding
    private lateinit var analyzeViewModel: AnalyzeViewModel

    companion object {
        const val RESULT_IMAGE = "RESULT_IMAGE"
        const val RESULT_TITLE = "RESULT_TITLE"
        const val RESULT_CONFIDENCE = "RESULT_CONFIDENCE"
        const val RESULT_MEDICINE = "RESULT_MEDICINE"

        const val RESULT_SKIN_TYPE = "RESULT_SKIN_TYPE"
        const val RESULT_SKIN_CONFIDENCE = "RESULT_SKIN_CONFIDENCE"
        const val RESULT_PRODUCTS = "RESULT_PRODUCTS"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultScanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Hasil Analisis"

        analyzeViewModel = obtainViewModel(this@ResultScanActivity)

        // Menampilkan hasil gambar dan analisis
        val resultImage = intent.getStringExtra(RESULT_IMAGE)
        val imageUri = resultImage?.let { Uri.parse(it) }
        val resultTitle = intent.getStringExtra(RESULT_TITLE)
        val resultConfidence = intent.getFloatExtra(RESULT_CONFIDENCE, 0.0f)
        val skinType = intent.getStringExtra(RESULT_SKIN_TYPE)
        val skinConfidence = intent.getFloatExtra(RESULT_SKIN_CONFIDENCE, 0.0f)
        val resultMedicine = intent.getStringExtra(RESULT_MEDICINE)

        // Ambil data produk yang direkomendasikan
        val productsJson = intent.getStringExtra(RESULT_PRODUCTS)
        val productListType = object : TypeToken<List<Product>>() {}.type
        val recommendedProducts = Gson().fromJson<List<Product>>(productsJson, productListType)
        val resultProduct = buildResultText(recommendedProducts)

        imageUri?.let {
            binding.resultImage.setImageURI(it)
        }
        binding.resultType.text = resultTitle
        binding.resultScore.text = getString(
            R.string.analyze_score,
            NumberFormat.getPercentInstance().format(resultConfidence)
        )
        binding.resultMedicine.text = "$resultMedicine"

        binding.resultSkinType.text = skinType

        // Save result to history
        val analyzeResult = Analyze(
            uri = resultImage ?: "",
            type = resultTitle ?: "",
            confidence = resultConfidence,
            medicine = resultMedicine,
            skin_type = skinType,
            confidence_skin_type = skinConfidence,
            product = Gson().toJson(resultProduct)
        )

        analyzeViewModel.insertAnalyze(analyzeResult)

        binding.recommendationButton.setOnClickListener {
            val intent = Intent(this, RecommendationActivity::class.java).apply {
                putExtra(RecommendationActivity.RESULT_SELECTED_PRODUCT, Gson().toJson(resultProduct))
            }
            startActivity(intent)
        }

        binding.finishButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun buildResultText(products: List<Product>): Product? {

        return if (products.isNotEmpty()) {
            val randomIndex = Random.nextInt(products.size)
            products[randomIndex]
        } else {
            null
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): AnalyzeViewModel {
        val factory = MainViewModelFactory(activity.applicationContext)
        return ViewModelProvider(activity, factory)[AnalyzeViewModel::class.java]
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}