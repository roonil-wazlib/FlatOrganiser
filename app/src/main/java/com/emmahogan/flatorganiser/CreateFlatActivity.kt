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

    lateinit var currentUser : User


    override fun onCreate(savedInstanceState : Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_flat)

        //get intent bundles
        currentUser = intent.getParcelableExtra("currentUser")


        addFlatToDatabase()
    }

    private fun addFlatToDatabase(){
        // Create a new flat

        val flat = HashMap<String, Any>()

        val flatReference = db.collection("flats").document()

        flatReference.set(flat)
            .addOnSuccessListener { Toast.makeText(this, "Flat created", Toast.LENGTH_SHORT).show() }
            .addOnFailureListener { Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show() }

        val member = HashMap<String, Any>()
        member.put("name", "testname")
        member.put("email", "testemail")

        val flatId = flatReference.id
        updateUserAccount(flatId)

        flatReference.collection("members").document("Member1").set(member)
            .addOnSuccessListener { Toast.makeText(this, "Member created", Toast.LENGTH_SHORT).show() }
            .addOnFailureListener { Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show() }
    }

    private fun updateUserAccount(flatId : String?){
        val userId = mAuth.currentUser!!.uid

        //TODO: add change flat id method to User class to avoid this
        val name = currentUser.name
        val email = currentUser.email

        val user = User(name, email, flatId)
        mDatabaseReference.child(userId).setValue(user)
    }
}