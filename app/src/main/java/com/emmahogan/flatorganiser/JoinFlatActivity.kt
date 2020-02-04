package com.emmahogan.flatorganiser

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore


//TODO test new delete from previous flat code
class JoinFlatActivity : AppCompatActivity() {

    lateinit var currentUser : User
    lateinit var flatIdET : EditText

    private var mAuth = FirebaseAuth.getInstance()
    //get reference to Firestore Cloud instance
    private var db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState : Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join_flat)

        //get intent bundles
        currentUser = intent.getParcelableExtra("user")

        val previousFlat = currentUser.flat.toString()

        //set up widgets
        flatIdET = findViewById(R.id.flat_id)
        val joinBtn : Button = findViewById(R.id.join_flat)
        joinBtn.setOnClickListener{ joinFlat(previousFlat) }

    }

    private fun joinFlat(previousFlat : String){
        (CloudFirestore::joinFlat)(CloudFirestore(), flatIdET.getText().toString(), currentUser)
        if(previousFlat != flatIdET.text.toString()){
            deleteFromFlat(previousFlat) //fixes removal from current flat bug
        }

    }

    private fun deleteFromFlat(previousFlat : String){
        //delete user from collection of flat members
        (CloudFirestore::deleteFromFlat)(CloudFirestore(), previousFlat, currentUser)
    }
}