package com.emmahogan.flatorganiser

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val billsButton: Button = findViewById(R.id.signup_button)
        billsButton.setOnClickListener { signUp() }
    }

    fun signUp(){
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }
}
