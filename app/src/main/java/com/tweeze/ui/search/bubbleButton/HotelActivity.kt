package com.tweeze.ui.search.bubbleButton

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tweeze.R
import com.tweeze.data.adapter.ViewAdapter
import com.tweeze.data.api.ApiConfig
import com.tweeze.data.api.ApiDataItem
import com.tweeze.databinding.ActivityHotelBinding
import com.tweeze.ui.search.detail.DetailActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class HotelActivity : AppCompatActivity(), ViewAdapter.OnItemClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ViewAdapter
    private lateinit var searchEditText: EditText
    private lateinit var binding: ActivityHotelBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHotelBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Show loading before searching
        showLoading(true)

        // Set toolbar title and back button
        val toolbar = binding.toolbar
        toolbar.title = "Hotel Search"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Recycler View
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = ViewAdapter(this)
        recyclerView.adapter = adapter

        // Search
        searchEditText = findViewById(R.id.searchEditText)
        searchEditText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                showLoading(true)
                performSearch()
            }
        })

        fetchData() // Start Fetching Data
    }
    private fun performSearch() {
        val searchTerm = searchEditText.text.toString().trim()

        if (searchTerm.isNotEmpty()) {
            showLoading(true) // Show the loading indicator

            GlobalScope.launch(Dispatchers.Main) {
                try {
                    val posts = ApiConfig.apiService.getHotel()
                    val searchResults = posts.filter { post ->
                        post.Place_Name.contains(searchTerm, ignoreCase = true) ||
                                post.Category.contains(searchTerm, ignoreCase = true)
                    }
                    adapter.setPosts(searchResults)
                } catch (e: Exception) {
                    Toast.makeText(this@HotelActivity, "Error fetching data", Toast.LENGTH_SHORT).show()
                }

                showLoading(false) // Hide the loading indicator
            }
        } else {
            fetchData() // If the search term is empty, show all the posts
        }

    }

    private fun showLoading(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun fetchData() {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                showLoading(false)
                val posts = ApiConfig.apiService.getHotel()
                adapter.setPosts(posts)
            } catch (e: Exception) {
                e.printStackTrace() // Print the stack trace for debugging
                Toast.makeText(this@HotelActivity, "Error fetching data: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onItemClick(post: ApiDataItem) {

        val intent = Intent(this, DetailActivity::class.java)

        intent.putExtra("PlaceName", post.Place_Name)
        intent.putExtra("Category", post.Category)
        intent.putExtra("Description", post.Description)
        intent.putExtra("City", post.City)
        intent.putExtra("Dos1", post.Dos_1)
        intent.putExtra("Dos2", post.Dos_2)
        intent.putExtra("Donts1", post.Donts_1)
        intent.putExtra("Donts2", post.Donts_2)
        intent.putExtra("photoURL", post.photo_URL)

        startActivity(intent)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}