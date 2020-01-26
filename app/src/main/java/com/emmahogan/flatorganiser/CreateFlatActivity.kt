package com.emmahogan.flatorganiser;

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import android.content.Intent



class CreateFlatActivity : AppCompatActivity() {

    lateinit var currentUser : User
    lateinit var flatIdTV : TextView


    override fun onCreate(savedInstanceState : Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_flat)

        //get intent bundles
        currentUser = intent.getParcelableExtra("currentUser")

        //set up widgets
        flatIdTV = findViewById(R.id.flat_id)
        val shareBtn : Button = findViewById(R.id.share_flat)
        shareBtn.setOnClickListener{ shareFlat() }

        //create flat in flats collection
        currentUser = (CloudFirestore::addFlatToDatabase)(CloudFirestore(),currentUser)
        updateFlatIdTextView(currentUser.flat)
    }

    private fun updateFlatIdTextView(flatId : String?){
        flatIdTV.setText(flatId)
    }


    private fun shareFlat(){
        //open email
        val name = currentUser.name
        val flat = currentUser.flat.toString()

        val i = Intent(Intent.ACTION_SEND)
        i.type = "message/rfc822"
        i.putExtra(Intent.EXTRA_SUBJECT, "$name invited you to join their flat!")
        i.putExtra(Intent.EXTRA_TEXT, "Please join my flat! The ID is $flat")
        try {
            startActivity(Intent.createChooser(i, "Send mail..."))
        } catch (ex: android.content.ActivityNotFoundException) {
            Toast.makeText(
                this,
                "There are no email clients installed.",
                Toast.LENGTH_SHORT
            ).show()
        }

    }
}