package com.emmahogan.flatorganiser

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import android.widget.Button
import android.widget.TextView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class HomeActivity : AppCompatActivity() {

    var mAuth = FirebaseAuth.getInstance()

    //Initialise Firebase db
    var mDatabase = FirebaseDatabase.getInstance()

    //Get reference to users child
    var mDatabaseReference = mDatabase!!.reference!!.child("users")


    val nameListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot : DataSnapshot) {
            // Get User object
            val users = dataSnapshot.children

            for (user in users) {
                val u = user.getValue(User::class.java!!)
                updateName(u!!.name)
            }

            //val contactSnapshot = dataSnapshot.child("users")
            //val contactChildren = contactSnapshot.children

        }

        override fun onCancelled(databaseError: DatabaseError) {
            //Getting Post failed, log a message
            //Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            // ...
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


        val user = mAuth!!.currentUser
        val uid = user!!.uid

        //tell database to listen for name
        mDatabaseReference.addValueEventListener(nameListener)

        //get email of user
        val email : String = intent.getStringExtra("email")

        //check email received correctly
        val testTV : TextView = findViewById(R.id.test)
        testTV.setText("$email")

        //set up logout button
        val logoutButton : Button = findViewById(R.id.logout_button)
        logoutButton.setOnClickListener{ logOut() }

        //hopefully this should end the activity when user is logged out
        mAuth.addAuthStateListener {
            if(mAuth.currentUser == null){
                this.finish()
            }
        }

    }

    fun updateName(name : String?){
        val nameTV : TextView = findViewById(R.id.nameTv)
        nameTV.setText("hi" + name)
    }

    fun logOut(){
        mAuth.signOut()
    }
}