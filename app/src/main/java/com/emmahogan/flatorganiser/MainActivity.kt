package com.emmahogan.flatorganiser

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {

    var mAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val signUpButton: Button = findViewById(R.id.signup_button)
        signUpButton.setOnClickListener { signUp() }

        val logInButton: Button = findViewById(R.id.login_button)
        logInButton.setOnClickListener { logIn() }

        //check if user auth already exists
        if (isUserLoggedIn()){
            startHomeActivity()
        }
    }

    fun signUp(){
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }

    fun logIn(){
        val intent = Intent(this, LogInActivity::class.java)
        startActivity(intent)
    }

    fun isUserLoggedIn() : Boolean {
        return mAuth.currentUser != null
    }

    fun startHomeActivity(){
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }

}
