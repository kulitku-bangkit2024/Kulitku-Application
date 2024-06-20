package com.dicoding.kulitku.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.kulitku.adapter.HistoryAdapter
import com.dicoding.kulitku.data.Analyze
import com.dicoding.kulitku.databinding.FragmentHistoryBinding
import com.dicoding.kulitku.data.sampleHistoryItems
import com.dicoding.kulitku.view.HistoryViewModel
import com.dicoding.kulitku.view.ViewModelFactory

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var historyViewModel: HistoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        historyViewModel = obtainViewModel()

        historyViewModel.getAll().observe(viewLifecycleOwner) {
            showRecyclerView(it as ArrayList<Analyze>)
        }
    }

    private fun obtainViewModel(): HistoryViewModel {
        val factory = ViewModelFactory.getInstance(requireActivity().application)
        return ViewModelProvider(this, factory).get(HistoryViewModel::class.java)
    }

    private fun showRecyclerView(analyzeHistoryList: ArrayList<Analyze>) {
        val layoutManager = LinearLayoutManager(requireContext())
        binding.historyRv.layoutManager = layoutManager
        binding.historyRv.setHasFixedSize(true)

        val adapter = HistoryAdapter(analyzeHistoryList)
        binding.historyRv.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}