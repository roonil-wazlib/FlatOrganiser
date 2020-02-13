package com.emmahogan.flatorganiser.display

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.emmahogan.flatorganiser.R
import com.emmahogan.flatorganiser.auth.MainActivity
import com.emmahogan.flatorganiser.auth.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SplashScreenActivity : AppCompatActivity() {

    private var mAuth = FirebaseAuth.getInstance()

    //Initialise Firebase db
    private var mDatabase = FirebaseDatabase.getInstance()

    //Get reference to users child
    private var mDatabaseReference = mDatabase.reference.child("users")


    lateinit var currentUser : User

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
                    val binsAdded = user.getValue(User::class.java)!!.binsAdded
                    instantiateUser(name, email, flatId, binsAdded)
                    //go to home activity
                    openHome()
                    break
                }
            }
        }

        override fun onCancelled(databaseError: DatabaseError) {
            //Getting user name failed, show error message
            Toast.makeText(this@SplashScreenActivity, "Something went wrong.", Toast.LENGTH_SHORT).show()
            // ...
        }
    }

    override fun onCreate(savedInstanceState : Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        //display loading toast
        Toast.makeText(this, "Loading...", Toast.LENGTH_LONG).show()

        if (mAuth.currentUser != null){
            //load user
            mDatabaseReference.addValueEventListener(nameListener)
        }else{
            //load MainActivity
            openMain()
        }
    }


    private fun instantiateUser(name : String?, email : String?, flatId : String?, binsAdded : Boolean){
        currentUser = User(name, email, flatId, binsAdded)
    }


    private fun openHome(){
        val intent = Intent(this, HomeActivity::class.java)
        intent.putExtra("user", currentUser)
        startActivity(intent)
        this.finish()
    }


    private fun openMain(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        this.finish()
    }
}