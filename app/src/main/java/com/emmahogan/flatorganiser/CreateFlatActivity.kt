package com.emmahogan.flatorganiser;

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import android.widget.Toast



class CreateFlatActivity : AppCompatActivity() {

    //get reference to Firebase Auth
    private var mAuth = FirebaseAuth.getInstance()

    //Initialise Firebase db
    private var mDatabase = FirebaseDatabase.getInstance()
    //Get reference to users child
    private var mDatabaseReference = mDatabase.reference.child("users")

    //get reference to Firestore Cloud instance
    private var db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState : Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_flat)

        addFlatToDatabase()
    }

    private fun addFlatToDatabase(){
        // Create a new flat
        val flat = hashMapOf(
            "name" to "Test",
            "members" to listOf("User name")
        )

        db.collection("flats").add(flat)
            .addOnSuccessListener { Toast.makeText(this, "Flat created", Toast.LENGTH_SHORT).show() }
            .addOnFailureListener { Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show() }
    }
}
