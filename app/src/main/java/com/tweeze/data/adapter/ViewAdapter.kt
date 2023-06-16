package com.tweeze.data.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tweeze.R
import com.tweeze.data.adapter.ViewAdapter.ViewHolder
import com.tweeze.data.api.ApiDataItem

class ViewAdapter(private val listener: OnItemClickListener) : RecyclerView.Adapter<ViewHolder>() {
    private var posts: List<ApiDataItem> = emptyList()

    fun setPosts(posts: List<ApiDataItem>) {
        this.posts = posts
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = posts[position]
        holder.tvPlaceName.text = post.Place_Name
        holder.tvCategory.text = post.Category
        holder.tvCity.text = post.City
        Glide.with(holder.itemView)
            .load(post.photo_URL)
            .placeholder(R.drawable.ic_launcher_background)
            .into(holder.imageView)

        holder.itemView.setOnClickListener {
            listener.onItemClick(post)
        }
    }

    interface OnItemClickListener {
        fun onItemClick(post: ApiDataItem)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvPlaceName: TextView = itemView.findViewById(R.id.tv_placeName)
        val tvCategory: TextView = itemView.findViewById(R.id.tv_category)
        val tvCity: TextView = itemView.findViewById(R.id.tv_city)
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
    }

}