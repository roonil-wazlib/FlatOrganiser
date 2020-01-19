package com.emmahogan.flatorganiser

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        //set up delete account button
        val deleteButton : Button = findViewById(R.id.delete)
        deleteButton.setOnClickListener{ onDelete() }

        //set up edit details
        val editButton : Button = findViewById(R.id.edit)
        editButton.setOnClickListener{ editAccount() }
    }

    private fun onDelete(){
        //bring up are you sure

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete Account?")
        builder.setMessage("This action is not reversable")


        builder.setPositiveButton("Continue"){dialog, which ->
            deleteAccount()
            Toast.makeText(this, "Deleting account...", Toast.LENGTH_SHORT).show()
        }

        builder.setNegativeButton("No"){dialog,which ->
            //close alert dialog
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun editAccount(){
        //do something here - new activity?
    }

    private fun deleteAccount(){
        //delete account from db and auth, return to MainActivity
    }
}