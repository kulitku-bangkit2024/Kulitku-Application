package com.dicoding.kulitku.view.tips

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.kulitku.R
import com.dicoding.kulitku.adapter.OnTipsClickListener
import com.dicoding.kulitku.adapter.TipsAdapter
import com.dicoding.kulitku.databinding.FragmentTipsBinding
import com.dicoding.kulitku.view.MainViewModelFactory

class TipsFragment : Fragment(), OnTipsClickListener {

    private var _binding: FragmentTipsBinding? = null
    private val binding get() = _binding!!
    private lateinit var tipsAdapter: TipsAdapter
    private val tipsViewModel: TipsViewModel by lazy {
        ViewModelProvider(this, MainViewModelFactory(requireContext()))[TipsViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentTipsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val layoutManager = LinearLayoutManager(context)
        binding.rvTips.layoutManager = layoutManager

        tipsAdapter = TipsAdapter(this)
        binding.rvTips.adapter = tipsAdapter

        tipsViewModel.tips.observe(viewLifecycleOwner) { articles ->
            tipsAdapter.submitList(articles)
        }

        tipsViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }

        tipsViewModel.message.observe(viewLifecycleOwner) { message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            Log.d("TIPS FRAGMENT", message)
        }

        return root
    }

    override fun onTipsClick(position: Int) {
        val tips = tipsAdapter.currentList[position]
        val bundle = Bundle().apply {
            putString("arg_title", tips.title ?: "")
            putString("arg_content", tips.content ?: "")
        }
        findNavController().navigate(R.id.navigation_detail_tips, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}