package com.emmahogan.flatorganiser

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import android.util.Log
import com.emmahogan.flatorganiser.auth.User
import com.google.firebase.firestore.FieldValue


class RealtimeDatabase{
    //get reference to Firebase Auth
    private var mAuth = FirebaseAuth.getInstance()

    //Initialise Firebase db
    private var mDatabase = FirebaseDatabase.getInstance()
    //Get reference to users child
    private var mDatabaseReference = mDatabase.reference.child("users")


    fun updateUserAccount(flatID : String?, currentUser : User) : User {
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


    fun updateUser(currentUser : User){
        mDatabaseReference.child(mAuth.currentUser!!.uid).setValue(currentUser)
    }
}




class CloudFirestore {
    //get reference to Firebase Auth
    private var mAuth = FirebaseAuth.getInstance()
    //get reference to Firestore Cloud instance
    private var db = FirebaseFirestore.getInstance()


    fun addFlatToDatabase(currentUser: User): User {
        // Create a new flat

        val flat = HashMap<String, Any>()
        flat.put("flatmates", mutableListOf(mAuth.currentUser!!.uid))

        val flatReference = db.collection("flats").document()

        flatReference.set(flat)
            .addOnSuccessListener {Log.d("TAG", "Flat created")}
            .addOnFailureListener {Log.d("TAG", "Something went wrong creating flat")}

        val member = HashMap<String, Any>()
        member.put("name", currentUser.name.toString())
        member.put("email", currentUser.email.toString())

        val flatId = flatReference.id

        //update user account in realtime database
        val updatedUser =
            (RealtimeDatabase::updateUserAccount)(RealtimeDatabase(), flatId, currentUser)

        flatReference.collection("members").document(mAuth.currentUser!!.uid).set(member)
            .addOnSuccessListener {Log.d("TAG", "User added successfully")}
            .addOnFailureListener {Log.d("TAG", "Something went wrong adding user")}

        return updatedUser
    }


    fun joinFlat(flatID: String, currentUser: User): User {

        //update user account in realtime database
        val updatedUser =
            (RealtimeDatabase::updateUserAccount)(RealtimeDatabase(), flatID, currentUser)

        val flatReference = db.collection("flats").document(flatID)

        flatReference.update("flatmates", FieldValue.arrayUnion(mAuth.currentUser!!.uid))

        val member = HashMap<String, Any>()
        member.put("name", updatedUser.name.toString())
        member.put("email", updatedUser.email.toString())

        flatReference.collection("members").document(mAuth.currentUser!!.uid).set(member)
            .addOnSuccessListener {Log.d("TAG", "User joined flat successfully")}
            .addOnFailureListener {Log.d("TAG", "Something went wrong joining flat")}

        return updatedUser
    }


    fun deleteFromFlat(flatToDeleteFrom : String, currentUser : User){
        //delete user from collection of flat members
        if (flatToDeleteFrom != "") {
            val userReference = db.collection("flats").document(flatToDeleteFrom).collection("members").document(mAuth.currentUser!!.uid)
            userReference.delete()

            val flatReference = db.collection("flats").document(flatToDeleteFrom)
            flatReference.update("flatmates", FieldValue.arrayRemove(mAuth.currentUser!!.uid))

            checkDeleteFlat(flatToDeleteFrom, flatToDeleteFrom) //delete flat collection if last flatmate just removed
        }
    }

    private fun checkDeleteFlat(previousFlat : String, flat : String) {
        val flatReference = db.collection("flats").document(previousFlat)
        flatReference.get().addOnSuccessListener { document ->
            if (document != null) {
                if (document.data!!["flatmates"].toString() == "[]") {
                    deleteFlat(flat)
                }
            } else {
                Log.d("TAG", "No such document")
            }
        }
            .addOnFailureListener { exception ->
                Log.d("TAG", "get failed with ", exception)
            }
    }

    private fun deleteFlat(flat : String){
        val flatReference = db.collection("flats").document(flat)
        flatReference.delete()
    }


    fun addShoppingList(flatID : String, listData : HashMap<String, Any>){
        val flatReference = db.collection("flats").document(flatID)
        flatReference.collection("data").document("shopping_list").set(listData)
            .addOnSuccessListener {Log.d("TAG", "Shopping list doc created/updated")}
            .addOnFailureListener {Log.d("TAG", "Something went wrong writing shopping list")}
    }

    fun addDinnerInfo(flatID : String, listData : HashMap<String, Any>){
        val flatReference = db.collection("flats").document(flatID)
        flatReference.collection("data").document("dinner_plans").set(listData)
            .addOnSuccessListener{Log.d("TAG", "Dinner plans added to database")}
            .addOnFailureListener{Log.d("TAG", "Something went wrong writing dinner plans")}
    }


    fun addBinDates(currentUser: User, flatID : String, listData : HashMap<String, Any>){
        val flatReference = db.collection("flats").document(flatID)
        flatReference.collection("data").document("bin_dates").set(listData)
            .addOnSuccessListener {Log.d("TAG", "Bin info added")}
            .addOnFailureListener {Log.d("TAG", "Something went wrong adding bin info")}
        currentUser.setBinsAdded()
        (RealtimeDatabase::updateUser)(RealtimeDatabase(), currentUser)
    }
}