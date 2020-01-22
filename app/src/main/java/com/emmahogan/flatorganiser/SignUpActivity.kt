package com.emmahogan.flatorganiser

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.database.*
import kotlinx.android.parcel.Parcelize


class SignUpActivity : AppCompatActivity() {

    //Initialize Firebase Auth
    private var mAuth = FirebaseAuth.getInstance()

    //Initialise Firebase db
    private var mDatabase = FirebaseDatabase.getInstance()

    //Get reference to users child
    private var mDatabaseReference = mDatabase.reference.child("users")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val submitButton: Button = findViewById(R.id.submit_signup)
        submitButton.setOnClickListener { createAccount() }

        val loginButton: TextView = findViewById(R.id.login_button)
        loginButton.setOnClickListener { logIn() }

        //skip sign in process and log in directly if current user already exists
        if (isUserLoggedIn()){
            startHomeActivity()
        }
    }



    private fun createAccount(){
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
                        writeNewUser(firebaseUser!!.uid, name, email) //user should not be null if task is successful

                        //go to home
                        val intent = Intent(this, HomeActivity::class.java)
                        startActivity(intent)
                        this.finish()

                    } else {
                        //check if they already have an account. If not, display different error message.
                        mDatabaseReference.addValueEventListener(checkIfUserExists)
                    }
                }
        } else {
            //passwords don't match
            displayMessage("Error: Passwords must match.")
        }
    }


    private fun checkPasswords(password: String, passwordCheck: String) : Boolean {
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

    private val checkIfUserExists = object : ValueEventListener {
        override fun onDataChange(dataSnapshot : DataSnapshot) {
            // Get User objects as iterable
            val users = dataSnapshot.children

            //get entered email
            val emailET: EditText = findViewById(R.id.email)
            val email : String = emailET.getText().toString()

            var userExists = false
            val errorMessage : String

            //find current user and update name
            for (user in users) {
                if (user.getValue(User::class.java)!!.email == email) {
                    userExists = true
                }
            }

            errorMessage = when(userExists){
                true -> "You already have an account. Please login."
                else -> "Something went wrong. Please check your internet connection and try again."
            }

            displayMessage(errorMessage)
        }

        override fun onCancelled(databaseError: DatabaseError) {
            //Getting user name failed, show error message
            displayMessage("Something went wrong")
            // ...
        }
    }


    private fun displayMessage(error : String){
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
    }

    private fun isUserLoggedIn() : Boolean {
        return mAuth.currentUser != null
    }

    private fun startHomeActivity(){
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        this.finish()
    }
}



//define properties of user child
//(properties must be initialised to keep javabeans happy)
@Parcelize
@IgnoreExtraProperties
data class User(
    var name: String? = "",
    var email: String? = "",
    var flat: String? = ""
) : Parcelable