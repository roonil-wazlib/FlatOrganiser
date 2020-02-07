package com.emmahogan.flatorganiser.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.emmahogan.flatorganiser.R


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val signUpButton: Button = findViewById(R.id.signup_button)
        signUpButton.setOnClickListener { signUp() }

        val logInButton: Button = findViewById(R.id.login_button)
        logInButton.setOnClickListener { logIn() }
    }

    fun signUp(){
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
        this.finish()
    }

    fun logIn(){
        val intent = Intent(this, LogInActivity::class.java)
        startActivity(intent)
        this.finish()
    }
}
