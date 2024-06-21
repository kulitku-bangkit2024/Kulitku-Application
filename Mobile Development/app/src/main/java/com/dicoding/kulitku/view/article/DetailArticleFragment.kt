package com.dicoding.kulitku.view.article

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.dicoding.kulitku.R
import com.dicoding.kulitku.databinding.FragmentDetailArticleBinding

class DetailArticleFragment : Fragment() {

    private var _binding: FragmentDetailArticleBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailArticleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val title = arguments?.getString("arg_title") ?: ""
        val content = arguments?.getString("arg_content") ?: ""
        val imageUrl = arguments?.getString("arg_image_url")

        binding.titleArticle.text = title
        binding.descriptionArticle.text = content

        Glide.with(requireContext())
            .load(imageUrl)
            .placeholder(R.drawable.ic_place_holder)
            .into(binding.imageArticle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
