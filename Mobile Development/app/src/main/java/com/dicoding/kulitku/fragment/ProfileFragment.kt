package com.dicoding.kulitku.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dicoding.kulitku.databinding.FragmentProfileBinding
import com.dicoding.kulitku.view.ProfileViewModel
import com.dicoding.kulitku.view.UserModelFactory
import com.dicoding.kulitku.view.UserPreferences
import com.dicoding.kulitku.view.login.dataStore

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Initialize UserPreferences
        val userPreferences = UserPreferences.getInstance(requireContext().dataStore)

        // Initialize ProfileViewModel
        val profileViewModel = ViewModelProvider(this, UserModelFactory(userPreferences)).get(
            ProfileViewModel::class.java)

        // Observe the userName LiveData and update the UI
        profileViewModel.name.observe(viewLifecycleOwner) { name ->
            binding.edtUsername.setText(name)
        }
// test
        profileViewModel.email.observe(viewLifecycleOwner) { email ->
            Log.d("ProfileFragment", "Email observed in fragment: $email")
            Toast.makeText(requireContext(), "Email: $email", Toast.LENGTH_SHORT).show()
            binding.edtEmail.setText(email)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = nullgu
    }
}
