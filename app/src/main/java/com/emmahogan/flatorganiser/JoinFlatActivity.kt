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
        joinBtn.setOnClickListener{(CloudFirestore::joinFlat)(CloudFirestore(), flatIdET.getText().toString(), currentUser) }

        deleteFromFlat(previousFlat)

    }


    //TODO add all these to firestore class
    private fun deleteFromFlat(previousFlat : String){
        //delete user from collection of flat members
        if (previousFlat != "") {
            val userReference = db.collection("flats").document(previousFlat).collection("members").document(mAuth.currentUser!!.uid)
            userReference.delete()

            val flatReference = db.collection("flats").document(currentUser.flat.toString())
            flatReference.update("flatmates", FieldValue.arrayRemove(mAuth.currentUser!!.uid))

            checkDeleteFlat(previousFlat) //delete flat collection if last flatmate just removed
        }
    }

    private fun checkDeleteFlat(previousFlat : String) {
        val flatReference = db.collection("flats").document(previousFlat)
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