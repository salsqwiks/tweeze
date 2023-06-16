package com.tweeze.ui.auth.profile

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.tweeze.R
import com.tweeze.databinding.ActivityHomeBinding
import com.tweeze.databinding.ActivityProfileBinding
import com.tweeze.ui.auth.login.LoginActivity

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var auth: FirebaseAuth

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.toolbar
        toolbar.title = "Detail"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Firebase Authentications
        auth = Firebase.auth

        binding.logout.setOnClickListener {
            signOut()
        }

        val user = FirebaseAuth.getInstance().currentUser
        val userName = user?.displayName
        val textViewGreeting = binding.tvGreetings
        textViewGreeting.text = "Hello ${userName ?: "User"}!"
    }

    private fun signOut() {
        auth.signOut()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}