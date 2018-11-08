package com.alimuzaffar.blank.net

import com.alimuzaffar.blank.database.entity.Sample
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiInterface {

    @GET("users/{user}")
    fun getUser(@Path("user") userId: String): Call<Sample>
}
