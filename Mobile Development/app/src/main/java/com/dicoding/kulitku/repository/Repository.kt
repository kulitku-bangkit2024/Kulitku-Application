package com.dicoding.kulitku.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dicoding.kulitku.api.*
import com.dicoding.kulitku.data.Analyze
import com.dicoding.kulitku.data.AnalyzeRoomDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import com.dicoding.kulitku.api.ApiConfigAuth
import com.dicoding.kulitku.api.AuthResponse


class Repository(
    private val apiServiceAuth: ApiServiceAuth,
    private val analyzeRoomDatabase: AnalyzeRoomDatabase
) {

    private val analyzeDao = analyzeRoomDatabase.analyzeDao()

    private var _analyze = MutableLiveData<List<Analyze>>()
    var analyze: LiveData<List<Analyze>> = _analyze

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _userLogin = MutableLiveData<LoginResponse>()
    val userLogin: LiveData<LoginResponse> = _userLogin

    private val _userLoginGoogle = MutableLiveData<AuthResponse>()
    val userLoginGoogle: LiveData<AuthResponse> get() = _userLoginGoogle

    private val _userProfile = MutableLiveData<GetProfileResponse>()
    val userProfile: LiveData<GetProfileResponse> = _userProfile

    private val _updateProfileResponse = MutableLiveData<UpdateProfileResponse>()
    val updateProfileResponse: LiveData<UpdateProfileResponse> = _updateProfileResponse

    private val _deleteProfileResponse = MutableLiveData<DeleteProfileResponse>()
    val deleteProfileResponse: LiveData<DeleteProfileResponse> = _deleteProfileResponse

    private val _articles = MutableLiveData<List<ResponseArticleItem>>()
    val articles: LiveData<List<ResponseArticleItem>> = _articles

    private val _quizzes = MutableLiveData<List<ResponseQuizItem>>()
    val quizzes: LiveData<List<ResponseQuizItem>> = _quizzes

    private val _tips = MutableLiveData<List<ResponseTipsItem>>()
    val tips: LiveData<List<ResponseTipsItem>> = _tips

    fun registerUser(registerData: RegisterData) {
        _isLoading.value = true

        val api = ApiConfigAuth.getApiService().registerUser(registerData)
        api.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                _isLoading.value = false
                val responseBody = response.body()
                if (response.isSuccessful) {
                    _message.value = "Your account has been successfully created"
                } else {
                    val errorBody = response.errorBody()?.string()
                    _message.value = "Error: $errorBody"
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                _isLoading.value = false
                _message.value = "Error: " + t.message.toString()
            }
        })
    }

    fun loginUser(loginData: LoginData) {
        _isLoading.value = true
        val api = ApiConfigAuth.getApiService().loginUser(loginData)
        api.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                _isLoading.value = false
                val responseBody = response.body()
                if (response.isSuccessful) {
                    _userLogin.value = responseBody!!
                    _message.value = "Login Success"
                } else {
                    val errorBody = response.errorBody()?.string()
                    _message.value = "Error: $errorBody"
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _isLoading.value = false
                _message.value = "Error: " + t.message.toString()
            }
        })
    }

    fun getUserProfile(id: String) {
        _isLoading.value = true
        val api = apiServiceAuth.getUserProfile(id)
        api.enqueue(object : Callback<GetProfileResponse> {
            override fun onResponse(
                call: Call<GetProfileResponse>,
                response: Response<GetProfileResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _userProfile.value = response.body()
                    _message.value = "Data fetched successfully"
                } else {
                    _message.value = "Error: " + response.message()
                }
            }

            override fun onFailure(call: Call<GetProfileResponse>, t: Throwable) {
                _isLoading.value = false
                _message.value = "Error: " + t.message.toString()
            }
        })
    }

    fun updateUserProfile(userData: UserData) {
        _isLoading.value = true
        val api = apiServiceAuth.updateUserProfile(userData)
        api.enqueue(object : Callback<UpdateProfileResponse> {
            override fun onResponse(
                call: Call<UpdateProfileResponse>,
                response: Response<UpdateProfileResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _updateProfileResponse.value = response.body()
                    _message.value = "Profile updated successfully"
                } else {
                    _message.value = "Error: " + response.message()
                }
            }

            override fun onFailure(call: Call<UpdateProfileResponse>, t: Throwable) {
                _isLoading.value = false
                _message.value = "Error: " + t.message.toString()
            }
        })
    }

    fun deleteUserProfile(id: String) {
        _isLoading.value = true
        val api = apiServiceAuth.deleteUserProfile(id)
        api.enqueue(object : Callback<DeleteProfileResponse> {
            override fun onResponse(
                call: Call<DeleteProfileResponse>,
                response: Response<DeleteProfileResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _deleteProfileResponse.value = response.body()
                    _message.value = "User with id = $id has been deleted"
                } else {
                    _message.value = "Error: " + response.message()
                }
            }

            override fun onFailure(call: Call<DeleteProfileResponse>, t: Throwable) {
                _isLoading.value = false
                _message.value = "Error: " + t.message.toString()
            }
        })
    }

    fun loginWithGoogle(code: String) {
        _isLoading.value = true
        val api = ApiConfigAuth.getApiService().loginWithGoogle(code)
        api.enqueue(object : Callback<AuthResponse> {
            override fun onResponse(
                call: Call<AuthResponse>,
                response: Response<AuthResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    Log.d("Repository", "Response: $responseBody")
                    responseBody?.let {
                        _userLoginGoogle.value = it
                        _message.value = "Login with Google successful"
                    } ?: run {
                        _message.value = "Empty response body"
                        Log.e("Repository", "Empty response body")
                    }
                } else {
                    Log.e("Repository", "Response error: ${response.errorBody()?.string()}")
                    _message.value = "Error: " + response.message()
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                _isLoading.value = false
                _message.value = "Login with Google failed: ${t.message}"
                Log.d("Repository", "Error: ${t.message}")
            }
        })
    }


    fun getAllAnalyze(): LiveData<List<Analyze>> {
        return analyzeDao.getAll()
    }

    suspend fun insertAnalyze(analyze: Analyze) {
        withContext(Dispatchers.IO) {
            analyzeDao.insert(analyze)
        }
    }

    fun getArticles() {
        _isLoading.value = true
        val api = apiServiceAuth.getArticles()
        api.enqueue(object : Callback<List<ResponseArticleItem>> {
            override fun onResponse(
                call: Call<List<ResponseArticleItem>>,
                response: Response<List<ResponseArticleItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _articles.value = response.body()
                } else {
                    _message.value = "Error: " + response.message()
                }
            }

            override fun onFailure(call: Call<List<ResponseArticleItem>>, t: Throwable) {
                _isLoading.value = false
                _message.value = "Error: " + t.message.toString()
            }
        })
    }

    fun getTips() {
        _isLoading.value = true
        val api = apiServiceAuth.getTips()
        api.enqueue(object : Callback<List<ResponseTipsItem>> {
            override fun onResponse(
                call: Call<List<ResponseTipsItem>>,
                response: Response<List<ResponseTipsItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _tips.value = response.body()
                } else {
                    _message.value = "Error: " + response.message()
                }
            }

            override fun onFailure(call: Call<List<ResponseTipsItem>>, t: Throwable) {
                _isLoading.value = false
                _message.value = "Error: " + t.message.toString()
            }
        })
    }

    fun getQuizzes() {
        _isLoading.value = true
        val api = apiServiceAuth.getQuizs()
        api.enqueue(object : Callback<List<ResponseQuizItem>> {
            override fun onResponse(
                call: Call<List<ResponseQuizItem>>,
                response: Response<List<ResponseQuizItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _quizzes.value = response.body()
                } else {
                    _message.value = "Error: " + response.message()
                }
            }

            override fun onFailure(call: Call<List<ResponseQuizItem>>, t: Throwable) {
                _isLoading.value = false
                _message.value = "Error: " + t.message.toString()
            }
        })
    }
}

