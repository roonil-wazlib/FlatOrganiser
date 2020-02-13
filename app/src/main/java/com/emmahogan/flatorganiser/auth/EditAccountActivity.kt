package com.emmahogan.flatorganiser.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.emmahogan.flatorganiser.*
import com.emmahogan.flatorganiser.display.HomeActivity
import com.emmahogan.flatorganiser.flat.JoinFlatActivity

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

        val changeFlat : Button = findViewById(R.id.changeFlat)
        changeFlat.setOnClickListener{ changeFlat() }

        val leaveFlat : Button = findViewById(R.id.leaveFlat)
        leaveFlat.setOnClickListener{ leaveFlatCheck() }
    }


    private fun changeFlat(){
        val intent = Intent(this, JoinFlatActivity::class.java)
        intent.putExtra("user", currentUser)
        startActivity(intent)
    }


    private fun leaveFlatCheck(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Leave flat?")
        builder.setMessage("This action is not reversable. Your flat data will be lost.")


        builder.setPositiveButton("Continue"){dialog, which ->
            leaveFlat()
        }

        builder.setNegativeButton("No"){dialog,which ->
            //close alert dialog
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }


    private fun leaveFlat(){
        (CloudFirestore::deleteFromFlat)(CloudFirestore(), currentUser.flat.toString(), currentUser)
        Toast.makeText(this, "Removing from flat...", Toast.LENGTH_SHORT).show()

        //TODO sort out this mess of updateUser functions - only need one ffs
        currentUser = (RealtimeDatabase::updateUserAccount)(RealtimeDatabase(), "", currentUser)
        currentUser.unAddBins()
        (RealtimeDatabase::updateUser)(RealtimeDatabase(), currentUser)
        val intent = Intent(this, HomeActivity::class.java)
        intent.putExtra("user", currentUser)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }
}