package com.dicoding.kulitku.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiServiceAuth {

    @POST("auth/api/v1/register")
    fun registerUser(
        @Body registerData: RegisterData
    ): Call<RegisterResponse>

    @POST("auth/api/v1/login")
    fun loginUser(
        @Body loginData: LoginData
    ): Call<LoginResponse>

    @GET("user/{id}")
    fun getUserProfile(
        @Path("id") id: String
    ): Call<GetProfileResponse>

    @PUT("user/update")
    fun updateUserProfile(
        @Body userData: UserData
    ): Call<UpdateProfileResponse>

    @DELETE("user/delete")
    fun deleteUserProfile(
        @Query("id_user") id: String
    ): Call<DeleteProfileResponse>

    @GET("auth/google/")
    fun loginWithGoogle(
        @Query("code") code: String
    ): Call<AuthResponse>

    @GET("article")
    fun getArticles(): Call<List<ResponseArticleItem>>

    @GET("quiz")
    fun getQuizs(): Call<List<ResponseQuizItem>>

    @GET("tips")
    fun getTips(): Call<List<ResponseTipsItem>>
}