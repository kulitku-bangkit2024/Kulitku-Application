package com.dicoding.kulitku.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.kulitku.R
import com.dicoding.kulitku.adapter.OnSkinInformationClickListener
import com.dicoding.kulitku.adapter.SkinInformationAdapter
import com.dicoding.kulitku.data.informasiItem
import com.dicoding.kulitku.databinding.FragmentSkinInformationBinding

class SkinInformationFragment : Fragment(), OnSkinInformationClickListener {

    private var _binding: FragmentSkinInformationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSkinInformationBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Access the activity's toolbar and set it up
        val toolbar = (activity as? AppCompatActivity)?.supportActionBar
        toolbar?.setDisplayHomeAsUpEnabled(true)
        (activity as? AppCompatActivity)?.supportActionBar?.title = "Categories"

        // Set up the navigation click listener for the toolbar
        binding.toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }

        // Change to GridLayoutManager with 2 columns
        val rvSkinInformation = binding.rvSkinInformation
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rvSkinInformation.layoutManager = layoutManager
        rvSkinInformation.adapter = SkinInformationAdapter(informasiItem, this)
        return root
    }

    override fun onInformationClick(position: Int) {
        findNavController().navigate(R.id.navigation_detail_skin_information)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}