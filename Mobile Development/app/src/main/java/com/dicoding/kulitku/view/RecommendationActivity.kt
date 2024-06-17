package com.dicoding.kulitku.view

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.dicoding.kulitku.api.Product
import com.dicoding.kulitku.databinding.ActivityRecommendationBinding
import com.google.gson.Gson

class RecommendationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecommendationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecommendationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Recommendation Product"

        val selectedProductJson = intent.getStringExtra(RESULT_SELECTED_PRODUCT)
        val selectedProduct = Gson().fromJson(selectedProductJson, Product::class.java)

        selectedProduct?.let {
            if (!it.gambar_produk.isNullOrEmpty()) {
                binding.imageProduct.setImageURI(Uri.parse(it.gambar_produk))
            }
            binding.textProductName.text = it.nama_produk
            binding.textProductCategory.text = it.kategori_produk
            binding.textSkinType.text = it.jenis_kulit
            binding.textProductMainBenefits.text = it.manfaat_utama
            binding.textProductUsage.text = it.cara_penggunaan
        } ?: run {
            Toast.makeText(this, "No product selected", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    companion object {
        const val RESULT_SELECTED_PRODUCT = "result_selected_product"
    }
}