package com.emmahogan.flatorganiser

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.IgnoreExtraProperties


class SignUpActivity : AppCompatActivity() {

    //Initialize Firebase Auth
    var mAuth = FirebaseAuth.getInstance()

    //Initialise Firebase db
    var mDatabase = FirebaseDatabase.getInstance()

    //Get reference to users child
    var mDatabaseReference = mDatabase!!.reference!!.child("users")



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val submitButton: Button = findViewById(R.id.submit_signup)
        submitButton.setOnClickListener { createAccount() }

        val loginButton: TextView = findViewById(R.id.login_button)
        loginButton.setOnClickListener { logIn() }
    }



    fun createAccount(){
        //make a new user
        val emailET: EditText = findViewById(R.id.email)
        val nameET: EditText = findViewById(R.id.name)
        val passwordET: EditText = findViewById(R.id.password)
        val passwordCheckET: EditText = findViewById(R.id.password_check)

        val name = nameET.getText().toString()
        val email = emailET.getText().toString()
        val password = passwordET.getText().toString()
        val passwordCheck = passwordCheckET.getText().toString()


        //if passwords match, create new user
        if (checkPasswords(password, passwordCheck)) {
            mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task: Task<AuthResult> ->

                    if (task.isSuccessful) {
                        //Registration OK
                        val firebaseUser = mAuth.currentUser
                        writeNewUser(firebaseUser!!.getUid(), name, email) //user should not be null if task is successful
                        Toast.makeText(this@SignUpActivity, "Success!", Toast.LENGTH_SHORT).show()

                    } else {
                        //check if they already have an account. If not, display different error message.
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


    fun logIn(){
        //open login activity and finish this one
        val intent = Intent(this, LogInActivity::class.java)
        startActivity(intent)
        this.finish()
    }


    //add new user to realtime database
    private fun writeNewUser(userId: String, name: String?, email: String?) {
        val user = User(name, email)
        mDatabaseReference.child(userId).setValue(user)
    }
}



//define properties of user child
@IgnoreExtraProperties
data class User(
    var name: String?,
    var email: String?
)