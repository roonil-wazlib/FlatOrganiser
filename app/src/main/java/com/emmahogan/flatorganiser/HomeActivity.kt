package com.emmahogan.flatorganiser

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import android.content.Intent
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.widget.Button
import android.widget.TextView


class HomeActivity : AppCompatActivity() {

    var mAuth = FirebaseAuth.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

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

    fun logOut(){
        mAuth.signOut()
    }
}