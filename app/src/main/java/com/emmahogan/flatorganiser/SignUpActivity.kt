package com.emmahogan.flatorganiser

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class SignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val billsButton: Button = findViewById(R.id.submit_signup)
        billsButton.setOnClickListener { signUp() }
    }

    fun signUp(){
        //do firebase stuff
    }

}