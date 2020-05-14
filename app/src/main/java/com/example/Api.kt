package com.example

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface Api {
    @GET("jokes")
    fun getJokes(): Call<List<Joke>>

    @POST("jokes")
    fun addJoke(
        @Body jsonObject: JsonObject
    ): Call<JsonObject>
}