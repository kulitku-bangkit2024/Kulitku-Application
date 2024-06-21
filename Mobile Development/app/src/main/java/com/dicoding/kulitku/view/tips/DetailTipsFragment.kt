package com.dicoding.kulitku.view.tips

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dicoding.kulitku.databinding.FragmentDetailTipsBinding

class DetailTipsFragment : Fragment() {

    private var _binding: FragmentDetailTipsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailTipsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val title = arguments?.getString("arg_title") ?: ""
        val content = arguments?.getString("arg_content") ?: ""

        binding.tipsTitle.text = title
        binding.tipsDescription.text = content

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}