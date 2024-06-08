package com.dicoding.kulitku.view.home.banner

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.dicoding.kulitku.R

class BannerFragment : Fragment() {

    private var imageResId: Int = 0

    companion object {
        private const val ARG_PARAM1 = "imgSlider"

        @JvmStatic
        fun newInstance(params: Int) = BannerFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_PARAM1, params)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        imageResId = arguments?.getInt(ARG_PARAM1) ?: R.drawable.img_placeholder
        val view = inflater.inflate(R.layout.fragment_banner, container, false)
        val img: ImageView = view.findViewById(R.id.img)
        Glide.with(requireActivity())
            .load(imageResId)
            .placeholder(R.drawable.img_placeholder)
            .into(img)
        return view
    }
}