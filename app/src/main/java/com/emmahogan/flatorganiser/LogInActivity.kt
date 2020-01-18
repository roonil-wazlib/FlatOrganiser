package com.emmahogan.flatorganiser

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult


class LogInActivity : AppCompatActivity() {

    var mAuth = FirebaseAuth.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        val loginButton: Button = findViewById(R.id.login_button)
        loginButton.setOnClickListener { logIn() }

        val signUpButton : TextView = findViewById(R.id.signUp_button)
        signUpButton.setOnClickListener { signUp() }

        //check if user is logged in
        if (isUserLoggedIn()){
            startHomeActivity()
        }
    }

    fun logIn() {

        val emailET: EditText = findViewById(R.id.email)
        val email = emailET.getText().toString()

        val passwordET: EditText = findViewById(R.id.password)
        val password = passwordET.getText().toString()

        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, OnCompleteListener<AuthResult> { task ->
                if (task.isSuccessful) {
                    startHomeActivity()
                } else {
                    Toast.makeText(this@LogInActivity, "Email or password incorrect.", Toast.LENGTH_SHORT).show()
                }
            }
            )
    }
    fun isUserLoggedIn() : Boolean {
        val user = mAuth.currentUser
        return user != null
    }

    fun signUp(){
        //switch to SignUpActivity and close this one
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
        this.finish()
    }

    fun startHomeActivity(){
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        this.finish() //close log in page so user goes back to MainActivity on Logout.
    }
}
