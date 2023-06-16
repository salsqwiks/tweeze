package com.tweeze.ui.home

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.tweeze.R
import com.tweeze.data.adapter.HomeViewAdapter
import com.tweeze.data.api.ApiConfig
import com.tweeze.data.api.ApiDataItem
import com.tweeze.databinding.ActivityHomeBinding
import com.tweeze.ui.auth.login.LoginActivity
import com.tweeze.ui.auth.profile.ProfileActivity
import com.tweeze.ui.search.bubbleButton.AttractionActivity
import com.tweeze.ui.search.bubbleButton.CulinaryActivity
import com.tweeze.ui.search.bubbleButton.HotelActivity
import com.tweeze.ui.search.bubbleButton.StayActivity
import com.tweeze.ui.search.detail.DetailActivity
import com.tweeze.ui.search.main.MainSearchActivity
import com.tweeze.utils.SpaceItemDecoration
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity(), HomeViewAdapter.OnItemClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var binding: ActivityHomeBinding
    private lateinit var adapter: HomeViewAdapter
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Firebase Authentications
        auth = Firebase.auth
        val firebaseUser = auth.currentUser
        if (firebaseUser == null) {
            // Not signed in, launch the Login activity
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        // Bubble Buttons
        binding.ivStay.setOnClickListener {
            val intent = Intent(this, StayActivity::class.java)
            startActivity(intent)
        }
        binding.ivCulinary.setOnClickListener {
            val intent = Intent(this, CulinaryActivity::class.java)
            startActivity(intent)
        }
        binding.ivHotel.setOnClickListener {
            val intent = Intent(this, HotelActivity::class.java)
            startActivity(intent)
        }
        binding.ivAttraction.setOnClickListener {
            val intent = Intent(this, AttractionActivity::class.java)
            startActivity(intent)
        }

        // Route to Profile
        binding.ivProfile.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        // Show Loading before fetching recommended destinations
        showLoading(true)

        recyclerView = findViewById(R.id.recyclerView)
        // Set Recycler View to horizontal scroll
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        adapter = HomeViewAdapter(this)

        // Add spacing between every item in recyclerView
        val space = resources.getDimensionPixelSize(R.dimen.item_spacing) // Adjust the value as needed
        val itemDecoration = SpaceItemDecoration(space)
        recyclerView.addItemDecoration(itemDecoration)
        recyclerView.adapter = adapter

        // Fetch data for recyclerView
        fetchData()

        // Bottom Navbar
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {

                    true
                }

                R.id.navigation_search -> {

                    val intent = Intent(this, MainSearchActivity::class.java)
                    startActivity(intent)

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

    private fun fetchData() {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val posts = ApiConfig.apiService.getPosts().shuffled().take(5)
                adapter.setPosts(posts)
                showLoading(false)
            } catch (e: Exception) {
                e.printStackTrace() // Print the stack trace for debugging
                Toast.makeText(this@HomeActivity, "Error fetching data: ${e.message}", Toast.LENGTH_SHORT).show()
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

    private fun showLoading(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
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