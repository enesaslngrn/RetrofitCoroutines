package com.example.retrofitcoroutines.view

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.retrofitcoroutines.adapter.PostRecyclerAdapter
import com.example.retrofitcoroutines.databinding.ActivityMainBinding
import com.example.retrofitcoroutines.factory.MainViewModelFactory
import com.example.retrofitcoroutines.repository.Repository
import com.example.retrofitcoroutines.utils.Resource
import com.example.retrofitcoroutines.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var postRecyclerAdapter: PostRecyclerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel = ViewModelProvider(this, MainViewModelFactory(Repository())).get(MainViewModel::class.java)
        binding.rvPosts.apply {
            postRecyclerAdapter = PostRecyclerAdapter()
            adapter = postRecyclerAdapter
            layoutManager = LinearLayoutManager(this@MainActivity,LinearLayoutManager.VERTICAL,false)

            // PAGINATION  -> Manuel Pagination
            addOnScrollListener(object : RecyclerView.OnScrollListener(){
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (!recyclerView.canScrollVertically(1) && !mainViewModel.isLastPage && !mainViewModel.isRefreshing){
                        mainViewModel.loadMorePosts()
                    }
                }
            })
        }

        // Swipe to refresh
        binding.swipeToRefresh.setOnRefreshListener {
            postRecyclerAdapter.differ.submitList(emptyList()) // Recyclerview temizlendi.
            mainViewModel.refreshPosts()
        }

        mainViewModel.response10Posts.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    binding.swipeToRefresh.isRefreshing = false
                    hideProgressBar()
                    hidePaginationProgressBar()
                    hideErrorText()
                    response.data?.let { postList ->
                        postRecyclerAdapter.differ.submitList(postRecyclerAdapter.differ.currentList + postList)
                    }
                }
                is Resource.Error -> {
                    binding.swipeToRefresh.isRefreshing = false
                    hideProgressBar()
                    hidePaginationProgressBar()
                    showErrorText()
                    response.message?.let { errorMessage ->
                        Log.e("MainActivity", "Error: $errorMessage")
                    }
                }
                is Resource.Loading -> {
                    if (!mainViewModel.hasFirstPostsSeen && !mainViewModel.isPaginating && !mainViewModel.isRefreshing){
                        showProgressBar()
                    }else if(!mainViewModel.isRefreshing){
                        showPaginationProgressBar()
                    }
                }
            }
        })
    }

    private fun showErrorText() {
        binding.tvError.apply {
            visibility = View.VISIBLE
            text = "Verileri alırken hata oluştu!"
        }
    }

    private fun hideErrorText() {
        binding.tvError.visibility = View.GONE
    }
    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }
    private fun hideProgressBar(){
        binding.progressBar.visibility = View.GONE
    }
    private fun showPaginationProgressBar(){
        binding.pbPagination.visibility = View.VISIBLE
    }
    private fun hidePaginationProgressBar(){
        binding.pbPagination.visibility = View.GONE
    }
}