package com.dicoding.kulitku.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.kulitku.adapter.HistoryAdapter
import com.dicoding.kulitku.data.Analyze
import com.dicoding.kulitku.databinding.FragmentHistoryBinding
import com.dicoding.kulitku.view.DetailHistoryActivity
import com.dicoding.kulitku.view.HistoryViewModel
import com.dicoding.kulitku.view.MainViewModelFactory

class HistoryFragment : Fragment(), HistoryAdapter.ItemClickListener {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
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

        historyViewModel = obtainViewModel(this)
        historyAdapter = HistoryAdapter(emptyList(), this)
        binding.historyRv.adapter = historyAdapter
        binding.historyRv.layoutManager = LinearLayoutManager(requireContext())
        binding.historyRv.setHasFixedSize(true)

        historyViewModel.allNotes.observe(viewLifecycleOwner, Observer { analyzeList ->
            analyzeList?.let {
                historyAdapter.updateData(it)
            }
        })
    }

    private fun obtainViewModel(fragment: Fragment): HistoryViewModel {
        val factory = MainViewModelFactory(fragment.requireContext())
        return ViewModelProvider(fragment, factory)[HistoryViewModel::class.java]
    }

    override fun onItemClick(analyze: Analyze) {
        // Handle item click, navigate to detail activity
        val intent = Intent(requireContext(), DetailHistoryActivity::class.java)
        intent.putExtra(
            DetailHistoryActivity.EXTRA_ANALYZE_ID,
            analyze
        )
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}