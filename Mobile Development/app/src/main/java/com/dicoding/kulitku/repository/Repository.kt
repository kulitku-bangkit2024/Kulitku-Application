package com.dicoding.kulitku.repository

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

class Repository(private val apiServiceAuth: ApiServiceAuth, private val analyzeRoomDatabase: AnalyzeRoomDatabase) {

    private val analyzeDao = analyzeRoomDatabase.analyzeDao()

    private var _analyze = MutableLiveData<List<Analyze>>()
    var analyze: LiveData<List<Analyze>> = _analyze

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _userLogin = MutableLiveData<LoginResponse>()
    val userLogin: LiveData<LoginResponse> = _userLogin

    private val _userProfile = MutableLiveData<GetProfileResponse>()
    val userProfile: LiveData<GetProfileResponse> = _userProfile

    private val _updateProfileResponse = MutableLiveData<UpdateProfileResponse>()
    val updateProfileResponse: LiveData<UpdateProfileResponse> = _updateProfileResponse

    private val _deleteProfileResponse = MutableLiveData<DeleteProfileResponse>()
    val deleteProfileResponse: LiveData<DeleteProfileResponse> = _deleteProfileResponse

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

//    fun loginWithGoogle(code: String) {
//        _isLoading.value = true
//        val api = ApiConfigAuth.getApiService().loginWithGoogle(code)
//        api.enqueue(object : Callback<GoogleAuthResponse> {
//            override fun onResponse(
//                call: Call<GoogleAuthResponse>,
//                response: Response<GoogleAuthResponse>
//            ) {
//                _isLoading.value = false
//                if (response.isSuccessful) {
//                    val googleAuthResponse = response.body()
//                    if (googleAuthResponse != null) {
//                        // Tambahkan kode untuk menyimpan data user dan token jika diperlukan
//                        _message.value = "Login with Google successful"
//                    } else {
//                        _message.value = "Failed to get Google login response"
//                    }
//                } else {
//                    _message.value = "Error: " + response.message()
//                }
//            }
//
//            override fun onFailure(call: Call<GoogleAuthResponse>, t: Throwable) {
//                _isLoading.value = false
//                _message.value = "Error: " + t.message.toString()
//            }
//        })
//    }

    fun getAllAnalyze(): LiveData<List<Analyze>> {
        return analyzeDao.getAll()
    }

    suspend fun insertAnalyze(analyze: Analyze) {
        withContext(Dispatchers.IO) {
            analyzeDao.insert(analyze)
        }
    }
}

