package com.emmahogan.flatorganiser

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

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