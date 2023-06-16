package com.tweeze.ui.search.main

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tweeze.R
import com.tweeze.data.adapter.ViewAdapter
import com.tweeze.data.api.ApiConfig
import com.tweeze.data.api.ApiDataItem
import com.tweeze.databinding.ActivityMainSearchBinding
import com.tweeze.ui.home.HomeActivity
import com.tweeze.ui.search.detail.DetailActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainSearchActivity : AppCompatActivity(), ViewAdapter.OnItemClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ViewAdapter
    private lateinit var searchEditText: EditText
    private lateinit var binding: ActivityMainSearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showLoading(true)

        val toolbar = binding.toolbar
        toolbar.title = "Search"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        searchEditText = findViewById(R.id.searchEditText)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = ViewAdapter(this)
        recyclerView.adapter = adapter

        searchEditText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                performSearch()
            }
        })

        fetchData() // Call fetchData() to populate the RecyclerView with initial data

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.menu.findItem(R.id.navigation_search).isChecked = true
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    // Handle Home item selection
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    true
                }

                R.id.navigation_search -> {

                    true
                }

                R.id.navigation_near_me -> {
                    showCustomDialog()
                    true
                }

                R.id.navigation_plan_a_trip -> {
                    showCustomDialog()
                    true
                }
                else -> false
            }
        }
    }

    private fun performSearch() {
        val searchTerm = searchEditText.text.toString().trim()

        if (searchTerm.isNotEmpty()) {
            showLoading(true) // Show the loading indicator

            GlobalScope.launch(Dispatchers.Main) {
                try {
                    val posts = ApiConfig.apiService.getPosts()
                    val searchResults = posts.filter { post ->
                        post.Place_Name.contains(searchTerm, ignoreCase = true) ||
                                post.Category.contains(searchTerm, ignoreCase = true)
                    }
                    adapter.setPosts(searchResults)
                } catch (e: Exception) {
                    Toast.makeText(this@MainSearchActivity, "Error fetching data", Toast.LENGTH_SHORT).show()
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
                val posts = ApiConfig.apiService.getPosts()
                adapter.setPosts(posts)
            } catch (e: Exception) {
                e.printStackTrace() // Print the stack trace for debugging
                Toast.makeText(this@MainSearchActivity, "Error fetching data: ${e.message}", Toast.LENGTH_SHORT).show()
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

    private fun showCustomDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_layout, null)
        val dialogBuilder = AlertDialog.Builder(this)
            .setView(dialogView)
            .setTitle("Notice")

        val dialog = dialogBuilder.create()
        dialog.show()

        dialogView.findViewById<Button>(R.id.btnOk).setOnClickListener {
            dialog.dismiss()
        }
    }
}