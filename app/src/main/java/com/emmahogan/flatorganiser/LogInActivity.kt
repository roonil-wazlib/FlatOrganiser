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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class LogInActivity : AppCompatActivity() {

    private var mAuth = FirebaseAuth.getInstance()

    lateinit var currentUser : User

    //Initialise Firebase db
    private var mDatabase = FirebaseDatabase.getInstance()

    //Get reference to users child
    private var mDatabaseReference = mDatabase.reference.child("users")


    private val nameListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot : DataSnapshot) {
            // Get User objects as iterable
            val users = dataSnapshot.children

            //find current user and update name
            for (user in users) {
                if (user.key == mAuth.currentUser!!.uid) {
                    val name = user.getValue(User::class.java)!!.name
                    val email = user.getValue(User::class.java)!!.email
                    val flatId = user.getValue(User::class.java)!!.flat
                    instantiateUser(name, email, flatId)
                    //go to home activity
                    openHome()
                    break
                }
            }
        }

        override fun onCancelled(databaseError: DatabaseError) {
            //Getting user name failed, show error message
            Toast.makeText(this@LogInActivity, "Something went wrong.", Toast.LENGTH_SHORT).show()
            // ...
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        val loginButton: Button = findViewById(R.id.login_button)
        loginButton.setOnClickListener { logIn() }

        val signUpButton: TextView = findViewById(R.id.signUp_button)
        signUpButton.setOnClickListener { signUp() }
    }


    fun logIn() {

        val emailET: EditText = findViewById(R.id.email)
        val email = emailET.getText().toString()

        val passwordET: EditText = findViewById(R.id.password)
        val password = passwordET.getText().toString()

        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, OnCompleteListener<AuthResult> { task ->
                if (task.isSuccessful) {
                    //tell database to listen for user info
                    mDatabaseReference.addValueEventListener(nameListener)
                } else {
                    Toast.makeText(
                        this@LogInActivity,
                        "Email or password incorrect.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            )
    }


    private fun signUp() {
        //switch to SignUpActivity and close this one
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
        this.finish()
    }


    private fun instantiateUser(name : String?, email : String?, flatId : String?) {
        currentUser = User(name, email, flatId)
    }


    private fun openHome(){
        val intent = Intent(this, HomeActivity::class.java)
        intent.putExtra("user", currentUser)
        startActivity(intent)
        this.finish()
    }

}
