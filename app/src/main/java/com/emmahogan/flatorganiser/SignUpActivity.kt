package com.emmahogan.flatorganiser

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult


class SignUpActivity : AppCompatActivity() {

    var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        //Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        val submitButton: Button = findViewById(R.id.submit_signup)
        submitButton.setOnClickListener { createAccount() }

        val emailET: EditText = findViewById(R.id.email)
        val nameET: EditText = findViewById(R.id.name)
        val passwordET: EditText = findViewById(R.id.password)
        val passwordCheckET: EditText = findViewById(R.id.password_check)
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
            mAuth!!.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task: Task<AuthResult> ->
                    if (task.isSuccessful) {
                        //Registration OK
                        val firebaseUser = mAuth!!.currentUser //throw exception if null
                        Toast.makeText(this@SignUpActivity, "Ah fuck, what have you done wrong this time.", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@SignUpActivity, "It looks like you already have an account. Please try signing in.", Toast.LENGTH_SHORT).show()
                    }
                }
        }
        else {
            //passwords don't match
            Toast.makeText(this@SignUpActivity, "Error: Passwords must match.", Toast.LENGTH_SHORT).show()
        }
    }

    fun checkPasswords(password: String, passwordCheck: String) : Boolean {
        //check correct password entered
        return password == passwordCheck

    }
}