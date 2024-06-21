package com.dicoding.kulitku.view.login

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.dicoding.kulitku.MainActivity
import com.dicoding.kulitku.R
import com.dicoding.kulitku.api.LoginData
import com.dicoding.kulitku.databinding.ActivityLoginBinding
import com.dicoding.kulitku.view.MainViewModelFactory
import com.dicoding.kulitku.view.UserModelFactory
import com.dicoding.kulitku.view.UserPreferences
import com.dicoding.kulitku.view.UserViewModel
import com.dicoding.kulitku.view.register.RegisterActivity

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel: LoginViewModel by lazy {
        ViewModelProvider(this, MainViewModelFactory(this))[LoginViewModel::class.java]
    }

    private lateinit var userViewModel: UserViewModel
    private var passwordShowing = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val pref = UserPreferences.getInstance(dataStore)
        userViewModel = ViewModelProvider(this, UserModelFactory(pref))[UserViewModel::class.java]

        loginViewModel.userLogin.observe(this) { loginResult ->
            if (loginResult.success) {
                val intent = Intent(this, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, loginResult.message, Toast.LENGTH_SHORT).show()
            }
        }

        binding.passwordIcon.setOnClickListener {
            togglePasswordVisibility()
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.edEmail.text.toString().trim()
            val password = binding.edPassword.text.toString().trim()

            if (validateLoginInput(email, password)) {
                loginUser(email, password)
            }
        }

        loginViewModel.message.observe(this) { messageLogin ->
            responseLogin(messageLogin)
        }

        binding.btnRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        loginViewModel.userLoginGoogle.observe(this) { googleLoginResult ->
            googleLoginResult?.let {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                if (it.message.contains("Pengguna berhasil dibuat")) {
                    userViewModel.saveLogin(true)
                    userViewModel.saveToken(it.id_user)
                    userViewModel.saveName(it.name)
                    userViewModel.saveEmail(it.email)

                    val intent = Intent(this, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    startActivity(intent)
                    finish()
                }
            }
        }

        binding.signInWithGoogle.setOnClickListener {
            launchGoogleSignInFlow()
        }
    }

    private fun launchGoogleSignInFlow() {
        val authorizationUrl = Uri.parse("https://accounts.google.com/o/oauth2/v2/auth")
            .buildUpon()
            .appendQueryParameter("client_id", "716663510651-n6la85ffcicf2oon3ec0fuckf0lm3eu2")
            .appendQueryParameter("response_type", "code")
            .appendQueryParameter("scope", "https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/userinfo.profile")
            .appendQueryParameter("redirect_uri", "https://kulitku-bangkit2024.et.r.appspot.com/auth/google/callback")
            .appendQueryParameter("state", "state_parameter_passthrough_value")
            .appendQueryParameter("nonce", "nonce_value")
            .build()

        Log.d("GoogleAuth", "Authorization URL: $authorizationUrl")

        val intent = Intent(Intent.ACTION_VIEW, authorizationUrl)
        startActivity(intent)
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

    private fun validateLoginInput(email: String, password: String): Boolean {
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

        return true
    }

    private fun loginUser(email: String, password: String) {
        val loginData = LoginData(email, password)
        loginViewModel.login(loginData)
    }

    private fun responseLogin(message: String) {
        if (message.contains("Login Success")) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

            val user = loginViewModel.userLogin.value
            userViewModel.saveLogin(true)
            user?.userId?.let { userViewModel.saveToken(it) }
            user?.name?.let { userViewModel.saveName(it) }
            user?.email?.let { userViewModel.saveEmail(it) }

            Log.d("LOGIN USER", user.toString())

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        } else {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val TAG = "LoginActivity"
    }
}