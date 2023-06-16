package com.tweeze.data.adapter

import android.view.LayoutInflater
import android.view.RoundedCorner
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.tweeze.R
import com.tweeze.data.api.ApiDataItem

class HomeViewAdapter(private val listener: OnItemClickListener) : RecyclerView.Adapter<HomeViewAdapter.PostViewHolder>() {
    private var posts: List<ApiDataItem> = emptyList()

    fun setPosts(posts: List<ApiDataItem>) {
        this.posts = posts
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_list_home, parent, false)
        return PostViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = posts[position]
        holder.tvPlaceName.text = post.Place_Name
        holder.tvCategory.text = post.Category
        holder.tvCity.text = post.City
        Glide.with(holder.itemView)
            .load(post.photo_URL)
            .centerCrop()
            .transform(RoundedCorners(20))
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(holder.imageView)

        holder.itemView.setOnClickListener {
            listener.onItemClick(post)
        }
    }

    interface OnItemClickListener {
        fun onItemClick(post: ApiDataItem)
    }

    inner class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvPlaceName: TextView = itemView.findViewById(R.id.tv_placeName)
        val tvCategory: TextView = itemView.findViewById(R.id.tv_category)
        val tvCity: TextView = itemView.findViewById(R.id.tv_city)
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
    }

}