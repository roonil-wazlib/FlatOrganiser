package com.emmahogan.flatorganiser

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore




class JoinFlatActivity : AppCompatActivity() {

    private var mAuth = FirebaseAuth.getInstance()

    //Initialise Firebase db
    private var mDatabase = FirebaseDatabase.getInstance()

    //Get reference to users child
    private var mDatabaseReference = mDatabase.reference.child("users")

    //get reference to Firestore Cloud instance
    private var db = FirebaseFirestore.getInstance()

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
        joinBtn.setOnClickListener{ joinFlat() }

    }


    //TODO add to database file
    private fun joinFlat(){
        val flatId = flatIdET.getText().toString()

        //update user account in realtime database
        currentUser = (RealtimeDatabase::updateUserAccount)(RealtimeDatabase(), flatId, currentUser)

        val flatReference = db.collection("flats").document(flatId)

        val member = HashMap<String, Any>()
        member.put("name", currentUser.name.toString())
        member.put("email", currentUser.email.toString())

        flatReference.collection("members").document(mAuth.currentUser!!.uid).set(member)
            .addOnSuccessListener { Toast.makeText(this, "Member created", Toast.LENGTH_SHORT).show() }
            .addOnFailureListener { Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show() }
    }
}