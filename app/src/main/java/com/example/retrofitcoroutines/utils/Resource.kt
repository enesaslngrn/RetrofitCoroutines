package com.example.retrofitcoroutines.utils

/**
 * BU YAPIYI tüm network ve database işlemlerinizde uygulayabilirsiniz.
 * Sealed class olması gerekiyor. Sadece belli sınıfların inherit edebilmesi için.
 * Generic class <T> -> flexibility, reusable of data, different types of data.
 * Success - Error - Loading ->
 */

sealed class Resource<T>(
    val data: T? = null, // response body()
    val message: String? = null) {

    class Success<T>(data : T) : Resource<T>(data)
    class Error<T>(message : String, data : T? = null) : Resource<T>(data,message)
    class Loading<T> : Resource<T>()

}