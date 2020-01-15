package com.emmahogan.flatorganiser

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.view.View
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.AuthResult


class LogInActivity : AppCompatActivity() {

    var mAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        val loginButton: Button = findViewById(R.id.login_button)
        loginButton.setOnClickListener { logIn() }
    }

    fun logIn() {

        val emailET: EditText = findViewById(R.id.email)
        val email = emailET.getText().toString()

        val passwordET: EditText = findViewById(R.id.password)
        val password = passwordET.getText().toString()

        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, OnCompleteListener<AuthResult> { task ->
                if (task.isSuccessful) {
                    val intent = Intent(this, HomeActivity::class.java)
                    intent.putExtra("email", mAuth.currentUser?.email) //pass email to home activity
                    startActivity(intent)
                    this.finish() //close log in page so user goes back to MainActivity on Logout.

                } else {
                    Toast.makeText(this@LogInActivity, "Email or password incorrect.", Toast.LENGTH_SHORT).show()
                }
            }
            )
    }
}
