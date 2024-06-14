package com.dicoding.kulitku.view

import android.content.Intent
import com.dicoding.kulitku.R
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.kulitku.databinding.ActivitySkinDataBinding

class SkinDataActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySkinDataBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySkinDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val spinnerTipeKulit = binding.tipeKulit
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.tipe_kulit_array,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTipeKulit.adapter = adapter

//        val imageUriString = intent.getStringExtra(ResultScanActivity.EXTRA_IMAGE_URI)

        binding.buttonSelanjutnya.setOnClickListener {
            val intent = Intent(this, ResultScanActivity::class.java)
//            intent.putExtra(ResultScanActivity.EXTRA_IMAGE_URI, imageUriString)
            startActivity(intent)
        }

    }
}