package com.example.retrofitcoroutines.service

import com.example.retrofitcoroutines.utils.Constants.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance { // OBJECT ? -> Singleton. ? -> Instance tek olmalı diye.

    private val retrofit by lazy { // Neden by lazy? -> Network çağrısı ilk kez yapıldığında çağırılsın.

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api : SimpleApi by lazy {
        retrofit.create(SimpleApi::class.java)
    }
}