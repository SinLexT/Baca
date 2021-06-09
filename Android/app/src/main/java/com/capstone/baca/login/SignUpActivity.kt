package com.capstone.baca.login

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.capstone.baca.R
import com.capstone.baca.Users
import com.capstone.baca.databinding.ActivitySignUpBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUpActivity : AppCompatActivity() {

    private var rootNode : FirebaseDatabase? = null
    private var reference : DatabaseReference? = null
    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar!!.hide()

        val firstName : EditText = findViewById(R.id.first_name)
        val lastName : EditText = findViewById(R.id.last_name)
        val username : EditText = findViewById(R.id.username)
        val email : EditText = findViewById(R.id.email)
        val password : EditText = findViewById(R.id.password)

        val btnRegister : Button = findViewById(R.id.btn_register)
        btnRegister.setOnClickListener {
            rootNode = FirebaseDatabase.getInstance()
            reference = rootNode!!.getReference("Users")

            val users = Users()

            users.firstName = firstName.text.toString()
            users.lastName = lastName.text.toString()
            users.username = username.text.toString()
            users.email = email.text.toString()
            users.password = password.text.toString()

            reference!!.child(users.username).setValue(users)
            finish()
        }
    }
}