package com.emmahogan.flatorganiser

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity




class JoinFlatActivity : AppCompatActivity() {

    lateinit var currentUser : User
    lateinit var flatIdET : EditText


    override fun onCreate(savedInstanceState : Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join_flat)

        //get intent bundles
        currentUser = intent.getParcelableExtra("currentUser")

        //set up widgets
        flatIdET = findViewById(R.id.flat_id)
        val joinBtn : Button = findViewById(R.id.join_flat)
        joinBtn.setOnClickListener{(CloudFirestore::joinFlat)(CloudFirestore(), flatIdET.getText().toString(), currentUser) }

    }
}