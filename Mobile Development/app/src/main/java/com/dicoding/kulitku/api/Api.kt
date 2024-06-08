package com.dicoding.kulitku.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Api {

    private val retrofit = Retrofit.Builder()
        .baseUrl(ApiConfig.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val newsApiService: NewsApiService = retrofit.create(NewsApiService::class.java)

}