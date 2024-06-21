package com.dicoding.kulitku.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dicoding.kulitku.MainActivity
import com.dicoding.kulitku.view.login.LoginViewModel

class RedirectUriActivity : AppCompatActivity() {

    private val loginViewModel: LoginViewModel by lazy {
        ViewModelProvider(this, MainViewModelFactory(this))[LoginViewModel::class.java]
    }

    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val uri = intent.data
        if (uri != null && uri.toString()
                .startsWith("https://kulitku-bangkit2024.et.r.appspot.com/auth/google/callback")
        ) {
            val code = uri.getQueryParameter("code")
            if (code != null) {
                loginViewModel.loginWithGoogle(code)

                loginViewModel.userLoginGoogle.observe(this) { googleLoginResult ->
                    googleLoginResult?.let {
                        Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                        if (it.message.contains("Pengguna berhasil dibuat")) {
                            userViewModel.saveLogin(true)
                            userViewModel.saveToken(it.id_user)
                            userViewModel.saveName(it.name)
                            userViewModel.saveEmail(it.email)

                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                }
            } else {
                Toast.makeText(this, "Failed to get authorization code", Toast.LENGTH_SHORT).show()
                finish()
            }
        } else {
            Toast.makeText(this, "Invalid redirect URI", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}