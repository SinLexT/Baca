package com.capstone.baca.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.capstone.baca.R
import com.capstone.baca.databinding.ActivityMainBinding
import com.capstone.baca.login.SignInActivity
import com.capstone.baca.main.about.AboutActivity

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar!!.hide()

        val btnSignIn : Button = findViewById(R.id.btn_signIn)
        btnSignIn.setOnClickListener(this)

        val btnStart : Button = findViewById(R.id.btn_start)
        btnStart.setOnClickListener(this)

        val btnAbout : Button = findViewById(R.id.btn_about)
        btnAbout.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_signIn -> {
                val signIn = Intent(this, SignInActivity::class.java)
                startActivity(signIn)
            }
            R.id.btn_about -> {
                val about = Intent(this, AboutActivity::class.java)
                startActivity(about)
            }
            R.id.btn_start -> {
                val start = Intent(this, MenuActivity::class.java)
                startActivity(start)
            }
        }
    }
}