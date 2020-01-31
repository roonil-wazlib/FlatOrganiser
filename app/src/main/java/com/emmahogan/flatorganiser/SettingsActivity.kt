package com.emmahogan.flatorganiser

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FieldValue
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
        currentUser = intent.getParcelableExtra("user")
    }


    private fun onDelete(){
        //bring up are you sure

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Confirm delete account?")
        builder.setMessage("This action is not reversable")


        builder.setPositiveButton("Continue"){dialog, which ->
            //TODO debug this - probably need to change the order.
            //TODO figure out how to reproduce the error
            deleteAuthAccount()
            deleteFromDatabase() //maybe put this in onSuccess of deleteAuthAccount to insure order correct?
            deleteFromFlat()
            //Toast.makeText(this, "Deleting account...", Toast.LENGTH_SHORT).show()
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
        //yeah will need a new activity - allow change of flat as well as account editing for name change, password change, email update etc
        val intent = Intent(this, EditAccountActivity::class.java)
        intent.putExtra("user", currentUser)
        startActivity(intent)
    }


    fun deleteAuthAccount() {
        //delete auth account
        val user = mAuth.currentUser

        user?.delete()
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
                }
                else
                    Toast.makeText(this, "FAIL", Toast.LENGTH_SHORT).show()
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

            val flatReference = db.collection("flats").document(currentUser.flat.toString())
            flatReference.update("flatmates", FieldValue.arrayRemove(mAuth.currentUser!!.uid))

            checkDeleteFlat() //delete flat collection if last flatmate just removed
        }
    }


    private fun returnToMain(){
        //go back to main activity, clear stack
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP //TODO check if this is also an issue for users who were auto logged in
        startActivity(intent)
    }


    private fun checkDeleteFlat() {
        val flatReference = db.collection("flats").document(currentUser.flat.toString())
        flatReference.get().addOnSuccessListener { document ->
            if (document != null) {
                if (document.data!!["flatmates"].toString() == "[]") {
                    deleteFlat()
                }
            } else {
                Log.d("TAG", "No such document")
            }
        }
            .addOnFailureListener { exception ->
                Log.d("TAG", "get failed with ", exception)
            }
    }


    fun deleteFlat(){
        val flatReference = db.collection("flats").document(currentUser.flat.toString())
        flatReference.delete()
    }
}