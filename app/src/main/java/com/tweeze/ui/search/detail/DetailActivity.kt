package com.tweeze.ui.search.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.tweeze.R
import com.tweeze.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.toolbar
        toolbar.title = "Detail"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val placeName = intent.getStringExtra("PlaceName")
        val category = intent.getStringExtra("Category")
        val description = intent.getStringExtra("Description")
        val city = intent.getStringExtra("City")
        val dos1 = intent.getStringExtra("Dos1")
        val dos2 = intent.getStringExtra("Dos2")
        val donts1 = intent.getStringExtra("Donts1")
        val donts2 = intent.getStringExtra("Donts2")
        val photoURL = intent.getStringExtra("photoURL")

        Glide.with(this)
            .load(photoURL)
            .placeholder(R.drawable.ic_launcher_background) // Placeholder image while loading
            .error(R.drawable.ic_launcher_background) // Error image if the URL is invalid or loading fails
            .into(binding.imageView)

        binding.tvPlaceName.text = "$placeName"
        binding.tvCity.text = "$city"
        binding.tvCategory.text = "$category"
        binding.tvDescription.text = "$description"
        binding.tvDos1.text = "$dos1"
        binding.tvDos2.text = "$dos2"
        binding.tvDonts1.text = "$donts1"
        binding.tvDonts2.text = "$donts2"

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}