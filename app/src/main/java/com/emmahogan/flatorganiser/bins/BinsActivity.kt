package com.emmahogan.flatorganiser.bins

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.emmahogan.flatorganiser.R
import com.emmahogan.flatorganiser.auth.User
import com.emmahogan.flatorganiser.display.HomeActivity

class BinsActivity : AppCompatActivity() {

    lateinit var currentUser : User

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bins)

        //get user data
        currentUser = intent.getParcelableExtra("user")
    }

    override fun onBackPressed(){
        val intent = Intent(this, HomeActivity::class.java)
        intent.putExtra("user", currentUser)
        startActivity(intent)
        this.finish()
    }
}