package com.dicoding.kulitku.view.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.dicoding.kulitku.R
import com.dicoding.kulitku.api.RegisterData
import com.dicoding.kulitku.databinding.ActivityRegisterBinding
import com.dicoding.kulitku.view.login.LoginActivity
import com.dicoding.kulitku.view.MainViewModelFactory

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private var passwordShowing = false
    private var confirmPasswordShowing = false

    private val registerViewModel: RegisterViewModel by lazy {
        ViewModelProvider(this, MainViewModelFactory(this))[RegisterViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        registerViewModel.message.observe(this) { messageRegister ->
            responseRegister(messageRegister)
        }

        registerViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        binding.passwordIcon.setOnClickListener {
            togglePasswordVisibility()
        }

        binding.confirmPasswordIcon.setOnClickListener {
            toggleConfirmPasswordVisibility()
        }

        binding.btnRegister.setOnClickListener {
            registerUser()
        }

        binding.btnLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun togglePasswordVisibility() {
        if (passwordShowing) {
            passwordShowing = false
            binding.edPassword.inputType =
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            binding.passwordIcon.setImageResource(R.drawable.password_show)
        } else {
            passwordShowing = true
            binding.edPassword.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            binding.passwordIcon.setImageResource(R.drawable.password_hide)
        }
        binding.edPassword.setSelection(binding.edPassword.length())
    }

    private fun toggleConfirmPasswordVisibility() {
        if (confirmPasswordShowing) {
            confirmPasswordShowing = false
            binding.edConfirmPassword.inputType =
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            binding.confirmPasswordIcon.setImageResource(R.drawable.password_show)
        } else {
            confirmPasswordShowing = true
            binding.edConfirmPassword.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            binding.confirmPasswordIcon.setImageResource(R.drawable.password_hide)
        }
        binding.edConfirmPassword.setSelection(binding.edConfirmPassword.length())
    }

    private fun registerUser() {
        val name = binding.edName.text.toString().trim()
        val email = binding.edEmail.text.toString().trim()
        val password = binding.edPassword.text.toString().trim()
        val confirmPassword = binding.edConfirmPassword.text.toString().trim()

        if (!validateInput(name, email, password, confirmPassword)) return

        val registerData = RegisterData(name, email, password)
        registerViewModel.register(registerData)
    }

    private fun validateInput(
        name: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        if (name.isEmpty()) {
            binding.edName.error = getString(R.string.name_empty_error)
            binding.edName.requestFocus()
            return false
        }

        if (email.isEmpty()) {
            binding.edEmail.error = getString(R.string.email_empty_error)
            binding.edEmail.requestFocus()
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.edEmail.error = getString(R.string.email_invalid_error)
            binding.edEmail.requestFocus()
            return false
        }

        if (password.isEmpty()) {
            binding.edPassword.error = getString(R.string.password_empty_error)
            binding.edPassword.requestFocus()
            return false
        }

        if (password.length < 8 || !password.matches(".*\\d.*".toRegex()) || !password.matches(".*[a-zA-Z].*".toRegex())) {
            binding.edPassword.error = getString(R.string.password_invalid_error)
            binding.edPassword.requestFocus()
            return false
        }

        if (confirmPassword.isEmpty()) {
            binding.edConfirmPassword.error = getString(R.string.confirm_password_empty_error)
            binding.edConfirmPassword.requestFocus()
            return false
        }

        if (password != confirmPassword) {
            binding.edConfirmPassword.error = getString(R.string.password_mismatch_error)
            binding.edConfirmPassword.requestFocus()
            return false
        }

        return true
    }

    private fun responseRegister(message: String) {
        if (message.contains("Your account has been successfully created")) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        } else {
            Log.e(TAG, message)
            Toast.makeText(this, message, Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        private const val TAG = "REGISTER ACTIVITY"
    }

}