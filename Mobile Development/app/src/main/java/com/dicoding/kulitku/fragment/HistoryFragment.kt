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
import com.dicoding.kulitku.view.AnalyzeViewModel
import com.dicoding.kulitku.view.HistoryViewModel
import com.dicoding.kulitku.view.ViewModelFactory

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var analyzeViewModel: AnalyzeViewModel
    private lateinit var historyViewModel: HistoryViewModel
    private lateinit var historyAdapter: HistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        historyViewModel = obtainViewModel()
        historyAdapter = HistoryAdapter(arrayListOf())
        binding.historyRv.adapter = historyAdapter
        binding.historyRv.layoutManager = LinearLayoutManager(requireContext())
        binding.historyRv.setHasFixedSize(true)

        historyViewModel.allNotes.observe(viewLifecycleOwner) { analyzeList ->
            historyAdapter.updateData(analyzeList)
        }
    }

    private fun obtainViewModel(): HistoryViewModel {
        val factory = ViewModelFactory.getInstance(requireActivity().application)
        return ViewModelProvider(this, factory).get(HistoryViewModel::class.java)
    }

//    private fun setupRecyclerView() {
//        historyAdapter = HistoryAdapter(ArrayList())
//        binding.historyRv.apply {
//            layoutManager = LinearLayoutManager(requireContext())
//            adapter = historyAdapter
//        }
//    }
//    private fun showRecyclerView(analyzeHistoryList: ArrayList<Analyze>) {
//        val layoutManager = LinearLayoutManager(requireContext())
//        binding.historyRv.layoutManager = layoutManager
//        binding.historyRv.setHasFixedSize(true)
//
//        val adapter = HistoryAdapter(analyzeHistoryList)
//        binding.historyRv.adapter = adapter
//    }
//
//    private fun observeViewModel() {
//        historyViewModel.allNotes.observe(viewLifecycleOwner) { analyzes ->
//            historyAdapter.updateData(analyzes)
//        }
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}