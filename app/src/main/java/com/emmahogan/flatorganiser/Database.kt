package com.emmahogan.flatorganiser

import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

class RealtimeDatabase{
    //get reference to Firebase Auth
    private var mAuth = FirebaseAuth.getInstance()

    //Initialise Firebase db
    private var mDatabase = FirebaseDatabase.getInstance()
    //Get reference to users child
    private var mDatabaseReference = mDatabase.reference.child("users")


    fun updateUserAccount(flatID : String?, currentUser : User) : User{
        val userId = mAuth.currentUser!!.uid

        //TODO: add change flat id method to User class to avoid this
        val name = currentUser.name
        val email = currentUser.email

        val user = User(name, email, flatID)
        mDatabaseReference.child(userId).setValue(user)

        return user
    }
}


class CloudFirestore{
    //get reference to Firebase Auth
    private var mAuth = FirebaseAuth.getInstance()

    //Initialise Firebase db
    private var mDatabase = FirebaseDatabase.getInstance()
    //Get reference to users child
    private var mDatabaseReference = mDatabase.reference.child("users")

    //get reference to Firestore Cloud instance
    private var db = FirebaseFirestore.getInstance()

    fun addFlatToDatabase(currentUser : User) : User{
        // Create a new flat

        val flat = HashMap<String, Any>()
        val flatReference = db.collection("flats").document()

        flatReference.set(flat)
            .addOnSuccessListener {}
            .addOnFailureListener {}

        val member = HashMap<String, Any>()
        member.put("name", currentUser.name.toString())
        member.put("email", currentUser.email.toString())

        val flatId = flatReference.id

        //update user account in realtime database
        val updatedUser = (RealtimeDatabase::updateUserAccount)(RealtimeDatabase(), flatId, currentUser)

        flatReference.collection("members").document(mAuth.currentUser!!.uid).set(member)
            .addOnSuccessListener {}
            .addOnFailureListener {}

        return updatedUser
    }

}