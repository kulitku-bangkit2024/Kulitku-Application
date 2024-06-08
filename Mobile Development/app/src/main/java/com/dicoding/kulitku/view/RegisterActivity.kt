package com.dicoding.kulitku.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Patterns
import com.dicoding.kulitku.MainActivity
import com.dicoding.kulitku.R
import com.dicoding.kulitku.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private var passwordShowing = false
    private var confirmPasswordShowing = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.passwordIcon.setOnClickListener {
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

        binding.confirmPasswordIcon.setOnClickListener {
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

        binding.btnRegister.setOnClickListener {
            val name = binding.edName.text.toString().trim()
            val email = binding.edEmail.text.toString().trim()
            val password = binding.edPassword.text.toString().trim()
            val confirmPassword = binding.edConfirmPassword.text.toString().trim()

            if (name.isEmpty()) {
                binding.edName.error = getString(R.string.name_empty_error)
                binding.edName.requestFocus()
                return@setOnClickListener
            }

            if (email.isEmpty()) {
                binding.edEmail.error = getString(R.string.email_empty_error)
                binding.edEmail.requestFocus()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.edEmail.error = getString(R.string.email_invalid_error)
                binding.edEmail.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                binding.edPassword.error = getString(R.string.password_empty_error)
                binding.edPassword.requestFocus()
                return@setOnClickListener
            }

            if (password.length < 8 || !password.matches(".*\\d.*".toRegex()) || !password.matches(".*[a-zA-Z].*".toRegex())) {
                binding.edPassword.error = getString(R.string.password_invalid_error)
                binding.edPassword.requestFocus()
                return@setOnClickListener
            }

            if (confirmPassword.isEmpty()) {
                binding.edConfirmPassword.error = getString(R.string.confirm_password_empty_error)
                binding.edConfirmPassword.requestFocus()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                binding.edConfirmPassword.error = getString(R.string.password_mismatch_error)
                binding.edConfirmPassword.requestFocus()
                return@setOnClickListener
            }

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}