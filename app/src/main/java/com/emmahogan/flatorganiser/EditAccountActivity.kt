package com.emmahogan.flatorganiser

import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class EditAccountActivity : AppCompatActivity() {

    lateinit var currentUser : User

    override fun onCreate(savedInstanceState : Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_account)

        //get user data
        currentUser = intent.getParcelableExtra("user")

        //set up layout
        val editNameET : EditText = findViewById(R.id.editName)
        editNameET.setText(currentUser.name.toString())

        val editEmailET : EditText = findViewById(R.id.editEmail)
        editEmailET.setText(currentUser.email.toString())

        val currentFlatTV : TextView = findViewById(R.id.currentFlat)
        currentFlatTV.setText(currentUser.flat.toString())

        //TODO link change flat to joinflatactivity, new code should do the trick
    }
}