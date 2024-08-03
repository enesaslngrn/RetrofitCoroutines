package com.example.retrofitcoroutines.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.retrofitcoroutines.databinding.ItemPostBinding
import com.example.retrofitcoroutines.model.Post

class PostRecyclerAdapter : RecyclerView.Adapter<PostRecyclerAdapter.PostViewHolder>() {
    inner class PostViewHolder(val binding : ItemPostBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffCallBack = object : DiffUtil.ItemCallback<Post>(){
        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer<Post>(this,diffCallBack)

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PostRecyclerAdapter.PostViewHolder {
        val binding = ItemPostBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostRecyclerAdapter.PostViewHolder, position: Int) {
        val post = differ.currentList[position]
        holder.binding.apply {
            tvUserId.text = post.userId.toString()
            tvId.text = post.id.toString()
            tvTitle.text = post.title
            tvBody.text = post.body
        }
    }

}