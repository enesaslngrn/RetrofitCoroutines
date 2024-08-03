package com.example.retrofitcoroutines.repository

import com.example.retrofitcoroutines.model.Post
import com.example.retrofitcoroutines.service.RetrofitInstance
import retrofit2.Response

class Repository {

    suspend fun get10Posts(userId : Int) : Response<List<Post>>{
        return RetrofitInstance.api.get10Posts(userId)
    }

}