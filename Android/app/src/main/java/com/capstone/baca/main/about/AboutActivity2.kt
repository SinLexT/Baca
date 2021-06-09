package com.capstone.baca.main.about

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.capstone.baca.databinding.ActivityAbout2Binding

class AboutActivity2 : AppCompatActivity() {

    private lateinit var binding: ActivityAbout2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAbout2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
    }
}