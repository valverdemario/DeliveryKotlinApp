package com.example.deliverykotlin.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient {

    fun getClient(): Retrofit{
        return Retrofit.Builder()
            .baseUrl("https://delivery-app-meva.herokuapp.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


}