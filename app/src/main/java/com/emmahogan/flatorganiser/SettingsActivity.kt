package com.emmahogan.flatorganiser

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class SettingsActivity : AppCompatActivity() {

    private var mAuth = FirebaseAuth.getInstance()

    //Initialise Firebase db
    private var mDatabase = FirebaseDatabase.getInstance()

    //Get reference to users child
    private var mDatabaseReference = mDatabase!!.reference!!.child("users")

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
        builder.setTitle("Confirm delete account?")
        builder.setMessage("This action is not reversable")


        builder.setPositiveButton("Continue"){dialog, which ->
            deleteAuthAccount()
            deleteFromDatabase()
            Toast.makeText(this, "Deleting account...", Toast.LENGTH_SHORT).show()
            returnToMain()
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

    fun deleteAuthAccount() {
        //delete auth account
        val user = mAuth.currentUser

        user?.delete()
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun deleteFromDatabase(){
        //delete user from database
        val userKey = mAuth.currentUser!!.uid
        mDatabaseReference.child(userKey).removeValue()
    }

    private fun returnToMain(){
        //go back to main activity, clear stack
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }
}