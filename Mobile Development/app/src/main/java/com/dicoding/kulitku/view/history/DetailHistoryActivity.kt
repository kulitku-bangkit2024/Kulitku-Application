package com.dicoding.kulitku.view.history

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.dicoding.kulitku.api.Product
import com.dicoding.kulitku.data.Analyze
import com.dicoding.kulitku.databinding.ActivityDetailHistoryBinding
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import android.widget.Toast
import com.bumptech.glide.Glide
import com.dicoding.kulitku.R
import java.text.NumberFormat

class DetailHistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Detail History"

        val analyze = intent.getParcelableExtra<Analyze>(EXTRA_ANALYZE_ID)

        analyze?.let {
            binding.resultImage.setImageURI(Uri.parse(it.uri))
            binding.resultType.text = it.type
            binding.resultScore.text = getString(
                R.string.analyze_score,
                NumberFormat.getPercentInstance().format(it.confidence)
            )
            binding.resultMedicine.text = it.medicine
            binding.resultSkinType.text = it.skin_type

            Log.d("Detail History", it.product.toString())

            try {
                val product: Product = Gson().fromJson(it.product, Product::class.java)

                Glide.with(this)
                    .load(product.gambar_produk)
                    .placeholder(R.drawable.ic_place_holder)
                    .into(binding.imageProduct)
                binding.textProductName.text = product.nama_produk
                binding.textProductCategory.text = product.kategori_produk
                binding.textSkinType.text = product.jenis_kulit
                binding.textProductMainBenefits.text = product.manfaat_utama
                binding.textProductUsage.text = product.cara_penggunaan
            } catch (e: JsonSyntaxException) {
                e.printStackTrace()
                Toast.makeText(this, "Failed to parse product information", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object {
        const val EXTRA_ANALYZE_ID = "extra_analyze"
    }
}