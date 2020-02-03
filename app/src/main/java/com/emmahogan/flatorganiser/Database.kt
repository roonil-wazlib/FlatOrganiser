package com.emmahogan.flatorganiser

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import android.util.Log
import com.google.common.primitives.UnsignedBytes.toInt
import com.google.firebase.firestore.FieldValue


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



//TODO add delete flat and delete from flat functionality
class CloudFirestore {
    //get reference to Firebase Auth
    private var mAuth = FirebaseAuth.getInstance()
    //Initialise Firebase db
    private var mDatabase = FirebaseDatabase.getInstance()
    //Get reference to users child
    private var mDatabaseReference = mDatabase.reference.child("users")
    //get reference to Firestore Cloud instance
    private var db = FirebaseFirestore.getInstance()


    fun addFlatToDatabase(currentUser: User): User {
        // Create a new flat

        val flat = HashMap<String, Any>()
        flat.put("flatmates", mutableListOf(mAuth.currentUser!!.uid))
        flat.put("flat_size", 1)

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

        //TODO figure out why this is broken
        //flatReference.update("flat_size", FieldValue.increment(1))

        val member = HashMap<String, Any>()
        member.put("name", updatedUser.name.toString())
        member.put("email", updatedUser.email.toString())

        flatReference.collection("members").document(mAuth.currentUser!!.uid).set(member)
            .addOnSuccessListener {Log.d("TAG", "User joined flat successfully")}
            .addOnFailureListener {Log.d("TAG", "Something went wrong joining flat")}

        return updatedUser
    }


    fun deleteFromFlat(previousFlat : String, currentUser : User){
        //delete user from collection of flat members
        if (previousFlat != "") {
            val userReference = db.collection("flats").document(previousFlat).collection("members").document(mAuth.currentUser!!.uid)
            userReference.delete()

            val flatReference = db.collection("flats").document(currentUser.flat.toString())
            flatReference.update("flatmates", FieldValue.arrayRemove(mAuth.currentUser!!.uid))

            checkDeleteFlat(previousFlat, currentUser) //delete flat collection if last flatmate just removed
        }
    }

    fun checkDeleteFlat(previousFlat : String, currentUser : User) {
        val flatReference = db.collection("flats").document(previousFlat)
        flatReference.get().addOnSuccessListener { document ->
            if (document != null) {
                if (document.data!!["flatmates"].toString() == "[]") {
                    deleteFlat(currentUser)
                }
            } else {
                Log.d("TAG", "No such document")
            }
        }
            .addOnFailureListener { exception ->
                Log.d("TAG", "get failed with ", exception)
            }
    }

    fun deleteFlat(currentUser : User){
        val flatReference = db.collection("flats").document(currentUser.flat.toString())
        flatReference.delete()
    }


    fun addShoppingList(flatID: String, listData: HashMap<String, Any>){
        val flatReference = db.collection("flats").document(flatID)
        flatReference.collection("data").document("shopping_list").set(listData)
            .addOnSuccessListener {Log.d("TAG", "Shopping list doc created/updated")}
            .addOnFailureListener {Log.d("TAG", "Something went wrong writing shopping list")}
    }
}