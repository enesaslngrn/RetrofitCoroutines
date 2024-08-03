package com.example.retrofitcoroutines.service

import com.example.retrofitcoroutines.model.Post
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface SimpleApi {

    @GET("/posts")
    suspend fun get10Posts(
        @Query("userId") userId : Int
    ) : Response<List<Post>>
}