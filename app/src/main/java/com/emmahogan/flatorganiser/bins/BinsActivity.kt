package com.emmahogan.flatorganiser.bins

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.emmahogan.flatorganiser.auth.User
import com.emmahogan.flatorganiser.display.HomeActivity
import java.text.SimpleDateFormat
import java.util.*


class BinsActivity : AppCompatActivity() {

    lateinit var currentUser : User
    lateinit var date : String

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.emmahogan.flatorganiser.R.layout.activity_bins)

        //get user data
        currentUser = intent.getParcelableExtra("user")

        date = getCurrentDate()

        getCurrentDate()
    }

    override fun onBackPressed(){
        val intent = Intent(this, HomeActivity::class.java)
        intent.putExtra("user", currentUser)
        startActivity(intent)
        this.finish()
    }


    fun getCurrentDate() : String {
        val c = Calendar.getInstance().getTime()
        println("Current time => $c")

        val df = SimpleDateFormat("dd/MMM/yyyy")
        val formattedDate = df.format(c)

        return formattedDate

    }
}