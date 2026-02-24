package com.example.uploadingscreen

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.uploadingscreen.model.SignUpRequest
import com.example.uploadingscreen.viewmodel.AuthViewModel
import kotlin.math.sign

class RegisterActivity : AppCompatActivity() {

    private lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        viewModel = ViewModelProvider(this)[AuthViewModel::class.java]

        val usernameEt = findViewById<EditText>(R.id.etUserName)
        val emailEt = findViewById<EditText>(R.id.etEmail)
        val passwordEt = findViewById<EditText>(R.id.etPassword)
        val btnconfirm = findViewById<ImageView>(R.id.btnconfirm)
        val loader = findViewById<FrameLayout>(R.id.signLoader)

        viewModel.signLoad.observe(this){signLoad->
            loader.visibility = if(signLoad) View.VISIBLE else View.GONE
            btnconfirm.isEnabled = !signLoad
        }

        btnconfirm.setOnClickListener {
            val username = usernameEt.text.toString().trim()
            val email = emailEt.text.toString().trim()
            val password = passwordEt.text.toString().trim()

            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "All Fields Required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val req = SignUpRequest(username, email, password)
            viewModel.register(req)
        }


        viewModel.signUpRes.observe(this) { response ->
            if (response.message == "User created") {
                Toast.makeText(this, "SignUp Successful", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, FormActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "SignUp Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
