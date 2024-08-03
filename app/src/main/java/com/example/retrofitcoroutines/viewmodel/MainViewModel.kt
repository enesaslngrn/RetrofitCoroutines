package com.example.retrofitcoroutines.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.retrofitcoroutines.model.Post
import com.example.retrofitcoroutines.repository.Repository
import com.example.retrofitcoroutines.utils.Constants.QUERY_PAGE_SIZE
import com.example.retrofitcoroutines.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Response

class MainViewModel(private val repository: Repository) : ViewModel() {

    val response10Posts : MutableLiveData<Resource<List<Post>>> = MutableLiveData()
    private var currentUserId = 1
    var hasFirstPostsSeen : Boolean = false
    var isPaginating : Boolean = false
        private set
    var isLastPage : Boolean = false
    private val totalResults = 100
    private val totalPages = totalResults / QUERY_PAGE_SIZE
    var isRefreshing : Boolean = false
        private set

    init {
        get10Posts(currentUserId)
    }

    fun get10Posts(userId : Int){
        viewModelScope.launch(Dispatchers.IO) {
            response10Posts.postValue(Resource.Loading())
            delay(500L)
            val response = repository.get10Posts(userId)
            response10Posts.postValue(handleListResponse(response))
            isPaginating = false
            isRefreshing = false
        }
    }

    fun loadMorePosts(){
        if (!isLastPage){
            isPaginating = true
            currentUserId++
            get10Posts(currentUserId)
        }
    }
    fun refreshPosts(){
        isRefreshing = true
        currentUserId = 1
        hasFirstPostsSeen = false
        isPaginating = false
        isLastPage = false
        get10Posts(currentUserId)
    }

    private fun handleListResponse(response: Response<List<Post>>): Resource<List<Post>> {
        if (response.isSuccessful) {
            response.body()?.let { myResponse ->
                if (!hasFirstPostsSeen){
                    hasFirstPostsSeen = true
                }
                isLastPage = currentUserId == totalPages
                return Resource.Success(myResponse)
            }
        }
        return Resource.Error("Hata oluştu : ${response.code()} - ${response.errorBody()}")
    }
}
