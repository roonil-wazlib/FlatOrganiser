package com.emmahogan.flatorganiser

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import android.util.Log
import com.google.common.primitives.UnsignedBytes.toInt


class RealtimeDatabase{
    //get reference to Firebase Auth
    private var mAuth = FirebaseAuth.getInstance()

    //Initialise Firebase db
    private var mDatabase = FirebaseDatabase.getInstance()
    //Get reference to users child
    private var mDatabaseReference = mDatabase.reference.child("users")


    fun updateUserAccount(flatID : String?, currentUser : User) : User{
        val userId = mAuth.currentUser!!.uid

        currentUser.changeFlat(flatID)
        mDatabaseReference.child(userId).setValue(currentUser)

        return currentUser
    }

    //add new user to realtime database
    fun writeNewUser(userId: String, name: String?, email: String?) {
        val user = User(name, email)
        mDatabaseReference.child(userId).setValue(user)
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
        flat.put("flatmates", mutableListOf(mAuth.currentUser!!.uid))
        flat.put("number", 1)

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


    fun joinFlat(flatID: String, currentUser: User) : User{

        //update user account in realtime database
        val updatedUser = (RealtimeDatabase::updateUserAccount)(RealtimeDatabase(), flatID, currentUser)

        val flatReference = db.collection("flats").document(flatID)
        //TODO get flatmates array and add current user to it
        //TODO learn how to update data
        var number = getNumberOfFlatmates(flatID)
        number ++

        val member = HashMap<String, Any>()
        member.put("name", updatedUser.name.toString())
        member.put("email", updatedUser.email.toString())

        flatReference.collection("members").document(mAuth.currentUser!!.uid).set(member)
            .addOnSuccessListener {}
            .addOnFailureListener {}

        return updatedUser
    }

    fun getNumberOfFlatmates(flatID: String) : Int{
        var number = 0
        val flatReference = db.collection("flats").document(flatID)
        flatReference.get().addOnSuccessListener { document ->
            if (document != null) {
                //flatmates = listOf(document.data!!["flatmates"])
                //Log.d("TAG", flatmates.toString())
                number = document.data!!["number"].toString().toInt()
            } else {
                //document doesn't exist
            }
        }
            .addOnFailureListener { exception ->
                //didn't work
            }
        return number
    }

}