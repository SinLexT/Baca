package com.capstone.baca.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.capstone.baca.main.MenuActivity
import com.capstone.baca.R
import com.capstone.baca.databinding.ActivitySignInBinding
import com.google.firebase.database.*


class SignInActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding : ActivitySignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar!!.hide()

        val btnStart : Button = findViewById(R.id.btn_start)
        btnStart.setOnClickListener(this)

        val btnRegister : Button = findViewById(R.id.btn_register)
        btnRegister.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_start -> {
                user()
            }
            R.id.btn_register -> {
                val register = Intent (this, SignUpActivity::class.java)
                startActivity(register)
            }
        }
    }

    private fun user() {

        val username : EditText = findViewById(R.id.username)
        val password : EditText = findViewById(R.id.password)

        val userUsername : String = username.text.toString()
        val userPassword : String = password.text.toString()

        val databaseReference : DatabaseReference = FirebaseDatabase.getInstance().getReference("Users")
        val user : Query = databaseReference.orderByChild("username").equalTo(userUsername)

        user.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val passwordUser : String? = dataSnapshot.child(userUsername).child("password").getValue(String::class.java)
                    if (passwordUser.equals(userPassword)) {
                        val start = Intent(this@SignInActivity, MenuActivity::class.java)
                        startActivity(start)
                        finish()
                    } else {
                        Toast.makeText(this@SignInActivity,"Wrong Password",Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@SignInActivity,"User not Found",Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
            }
        })
    }
}