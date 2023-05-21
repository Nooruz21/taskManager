package com.example.taskmanager

import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface TaskApi {
    @GET("/todo/")
    fun getTask(): Call<Task>

    @FormUrlEncoded
    @POST("/todo/")
    fun updateTask(
        @Field("text") text: String
    ): Call<Task>

    @DELETE("/todo/{id}/")
    fun deleteTask(
        @Path("id") id: Int
    ): Call<Task>
}