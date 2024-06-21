package com.dicoding.kulitku.view.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.dicoding.kulitku.databinding.FragmentProfileBinding
import com.dicoding.kulitku.view.UserModelFactory
import com.dicoding.kulitku.view.UserPreferences
import com.dicoding.kulitku.view.UserViewModel
import com.dicoding.kulitku.view.login.LoginActivity
import com.dicoding.kulitku.view.login.dataStore

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null

    private val binding get() = _binding!!
    private lateinit var userViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root
        // Initialize UserPreferences and UserViewModel
        val userPreferences = UserPreferences.getInstance(requireContext().dataStore)
        userViewModel = ViewModelProvider(this, UserModelFactory(userPreferences))[UserViewModel::class.java]

        userViewModel.getName().observe(viewLifecycleOwner) { name ->
            binding.tvUsername.text = name
        }

        userViewModel.getEmail().observe(viewLifecycleOwner) { email ->
            binding.tvEmail.text = email
        }

        binding.btnLogout.setOnClickListener {
            userViewModel.logout()
            Toast.makeText(requireContext(), "Logout berhasil", Toast.LENGTH_SHORT).show()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            requireActivity().finish()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}