package com.capstone.baca.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.capstone.baca.R

class MenuActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        supportActionBar!!.hide()

        val btnCamera : Button = findViewById(R.id.btn_camera)
        btnCamera.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.btn_camera -> {
                val cameraActivity = Intent(this, CameraActivity::class.java)
                startActivity(cameraActivity)
            }
        }
    }
}