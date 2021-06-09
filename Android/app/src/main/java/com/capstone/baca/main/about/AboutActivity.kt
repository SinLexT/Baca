package com.capstone.baca.main.about

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.capstone.baca.R
import com.capstone.baca.databinding.ActivityAboutBinding

class AboutActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityAboutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar!!.hide()

        val imgView : ImageView = findViewById(R.id.ivBackground)
        imgView.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.ivBackground -> {
                val about2 = Intent(this, AboutActivity2::class.java)
                startActivity(about2)
                finish()
            }
        }
    }
}