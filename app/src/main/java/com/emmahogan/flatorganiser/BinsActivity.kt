package com.emmahogan.flatorganiser

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class BinsActivity : AppCompatActivity() {

    lateinit var currentUser : User

    override fun onCreate(savedInstanceState : Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bins)

        //get user data
        currentUser = intent.getParcelableExtra("user")
    }
}