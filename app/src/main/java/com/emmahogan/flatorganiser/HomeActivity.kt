package com.emmahogan.flatorganiser

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class HomeActivity : AppCompatActivity() {

    private var mAuth = FirebaseAuth.getInstance()

    //Initialise Firebase db
    private var mDatabase = FirebaseDatabase.getInstance()

    //Get reference to users child
    private var mDatabaseReference = mDatabase!!.reference!!.child("users")


    private val nameListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot : DataSnapshot) {
            // Get User objects as iterable
            val users = dataSnapshot.children

            //find current user and update name
            for (user in users) {
                if (user.key == mAuth.currentUser!!.uid) {
                    val name = user.getValue(User::class.java!!)!!.name
                    updateName(name)
                }
            }
        }

        override fun onCancelled(databaseError: DatabaseError) {
            //Getting user name failed, show error message
            Toast.makeText(this@HomeActivity, "Something went wrong.", Toast.LENGTH_SHORT).show()
            // ...
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


        val user = mAuth!!.currentUser
        val email = user!!.email

        //tell database to listen for name
        mDatabaseReference.addValueEventListener(nameListener)

        //check email received correctly
        val testTV : TextView = findViewById(R.id.test)
        testTV.setText("$email")

        //set up logout button
        val logoutButton : Button = findViewById(R.id.logout_button)
        logoutButton.setOnClickListener{ logOut() }

        //end activity when user logs out
        mAuth.addAuthStateListener {
            if(mAuth.currentUser == null){
                this.finish()
            }
        }

    }

    //update name textview
    fun updateName(name : String?){
        val nameTV : TextView = findViewById(R.id.nameTv)
        nameTV.setText("hi" + name)
    }

    private fun logOut(){
        mAuth.signOut()
    }
}