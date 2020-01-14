package com.emmahogan.flatorganiser

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult


class SignUpActivity : AppCompatActivity() {

    //Initialize Firebase Auth
    var mAuth = FirebaseAuth.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val submitButton: Button = findViewById(R.id.submit_signup)
        submitButton.setOnClickListener { createAccount() }
    }


    fun createAccount(){
        //make a new user
        val emailET: EditText = findViewById(R.id.email)
        val nameET: EditText = findViewById(R.id.name)
        val passwordET: EditText = findViewById(R.id.password)
        val passwordCheckET: EditText = findViewById(R.id.password_check)

        val email = emailET.getText().toString()
        val password = passwordET.getText().toString()
        val passwordCheck = passwordCheckET.getText().toString()

        if (checkPasswords(password, passwordCheck)) {
            mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task: Task<AuthResult> ->

                    if (task.isSuccessful) {
                        //Registration OK
                        val firebaseUser = mAuth.currentUser

                        //at some point here use user to add name info to profile.

                        Toast.makeText(this@SignUpActivity, "Success!", Toast.LENGTH_SHORT).show()

                    } else {
                        Toast.makeText(this@SignUpActivity, "It looks like you already have an account. Please try signing in.", Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            //passwords don't match
            Toast.makeText(this@SignUpActivity, "Error: Passwords must match.", Toast.LENGTH_SHORT).show()
        }
    }


    fun checkPasswords(password: String, passwordCheck: String) : Boolean {
        //check correct password entered
        return password == passwordCheck

    }
}