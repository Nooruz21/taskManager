package com.example.taskmanager

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitService {
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://68.183.214.2:8000")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: TaskApi = retrofit.create(TaskApi::class.java)
}