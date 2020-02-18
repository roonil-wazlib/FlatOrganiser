package com.emmahogan.flatorganiser.dinner_plan

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.emmahogan.flatorganiser.R
import com.emmahogan.flatorganiser.auth.User

class DinnerPlanActivity : AppCompatActivity() {


    lateinit var currentUser : User
    
    override fun onCreate(savedInstanceState : Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dinner_plan)

        //get user data
        currentUser = intent.getParcelableExtra("user")
    }
}