package com.riyazuddin.shoppinglist.data.remote

import com.riyazuddin.shoppinglist.BuildConfig
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PixabayApi {

    @GET("/api")
    suspend fun searchImage(
        @Query("q") query: String,
        @Query("key") key: String = BuildConfig.PIXABAY_API
    ): Response<ImagesResponse>
}