package com.emmahogan.flatorganiser

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore


class SettingsActivity : AppCompatActivity() {

    private var mAuth = FirebaseAuth.getInstance()
    //Initialise Firebase db
    private var mDatabase = FirebaseDatabase.getInstance()
    //Get reference to users child
    private var mDatabaseReference = mDatabase.reference.child("users")
    //get reference to Firestore Cloud instance
    private var db = FirebaseFirestore.getInstance()

    lateinit var currentUser : User


    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        //set up delete account button
        val deleteButton : Button = findViewById(R.id.delete)
        deleteButton.setOnClickListener{ onDelete() }

        //set up edit details
        val editButton : Button = findViewById(R.id.edit)
        editButton.setOnClickListener{ editAccount() }

        //get intent bundles
        currentUser = intent.getParcelableExtra("currentUser")
    }


    private fun onDelete(){
        //bring up are you sure

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Confirm delete account?")
        builder.setMessage("This action is not reversable")


        builder.setPositiveButton("Continue"){dialog, which ->
            deleteAuthAccount()
            deleteFromDatabase()
            deleteFromFlat()
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
        //delete user from realtimeDatabase
        val userKey = mAuth.currentUser!!.uid
        mDatabaseReference.child(userKey).removeValue()
    }


    private fun deleteFromFlat(){
        //delete user from collection of flat members
        if (currentUser.flat != "") {
            val userReference = db.collection("flats").document(currentUser.flat.toString()).collection("members").document(mAuth.currentUser!!.uid)
            userReference.delete()
        }
        //TODO research whether empty collection should be deleted - seen weird things saying never to delete collections, not sure why
    }


    private fun returnToMain(){
        //go back to main activity, clear stack
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }


    private fun flatIsEmpty() : Boolean{
        val flatReference = db.collection("flats").document(currentUser.flat.toString())
        //check if flatReference exists (if empty it does not exist)
        return true
    }


    private fun deleteFlat(){
        val flatReference = db.collection("flats").document(currentUser.flat.toString())
        flatReference.delete()
    }
}