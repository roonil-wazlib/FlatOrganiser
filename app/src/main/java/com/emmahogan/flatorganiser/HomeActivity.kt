package com.emmahogan.flatorganiser

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener



class HomeActivity : AppCompatActivity() {

    private var mAuth = FirebaseAuth.getInstance()

    //Initialise Firebase db
    private var mDatabase = FirebaseDatabase.getInstance()

    //Get reference to users child
    private var mDatabaseReference = mDatabase.reference.child("users")


    lateinit var currentUser : User
    lateinit var createFlatButton : Button
    lateinit var joinFlatButton : Button


    private val nameListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot : DataSnapshot) {
            // Get User objects as iterable
            val users = dataSnapshot.children

            //find current user and update name
            for (user in users) {
                if (user.key == mAuth.currentUser!!.uid) {
                    val name = user.getValue(User::class.java)!!.name
                    updateName(name)
                    val email = user.getValue(User::class.java)!!.email
                    val flatId = user.getValue(User::class.java)!!.flat
                    instantiateUser(name, email, flatId)
                    updateDisplay()
                }
            }
        }

        override fun onCancelled(databaseError: DatabaseError) {
            //Getting user name failed, show error message
            Toast.makeText(this@HomeActivity, "Something went wrong.", Toast.LENGTH_SHORT).show()
            // ...
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val user = mAuth!!.currentUser
        val email = user!!.email

        //tell database to listen for name
        mDatabaseReference.addValueEventListener(nameListener)

        //check email received correctly
        val testTV : TextView = findViewById(R.id.test)
        testTV.setText("$email")

        //set up logout button
        val logoutButton : Button = findViewById(R.id.logout_button)
        logoutButton.setOnClickListener{ logOut() }

        //set up settings button
        val settingsButton : Button = findViewById(R.id.settings)
        settingsButton.setOnClickListener{openSettings()}

        //set up create and join flat buttons
        createFlatButton = findViewById(R.id.create_flat)
        joinFlatButton = findViewById(R.id.join_flat)
        createFlatButton.setOnClickListener{ createFlat() }
        joinFlatButton.setOnClickListener{ joinFlat() }

        //end activity when user logs out
        mAuth.addAuthStateListener {
            if(mAuth.currentUser == null){
                this.finish()
            }
        }
    }

    //update name textview
    fun updateName(name : String?){
        val nameTV : TextView = findViewById(R.id.nameTv)
        nameTV.setText("hi" + name)
    }

    private fun logOut(){
        mAuth.signOut()
    }

    private fun openSettings(){
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }

    private fun createFlat(){

        if(userInFlat()){
            Toast.makeText(this@HomeActivity, "You're already in a flat!", Toast.LENGTH_SHORT).show()
        }
        else{
            val intent = Intent(this, CreateFlatActivity::class.java)
            intent.putExtra("currentUser", currentUser)
            startActivity(intent)
        }

    }

    private fun joinFlat(){
        val intent = Intent(this, JoinFlatActivity::class.java)
        intent.putExtra("currentUser", currentUser)
        startActivity(intent)
    }

    private fun instantiateUser(name : String?, email : String?, flatId : String?) {
        currentUser = User(name, email, flatId)
    }

    private fun userInFlat() : Boolean{
        return currentUser.flat != ""
    }

    private fun updateDisplay(){
        //remove add/join flat buttons if user already in flat
        if (userInFlat()){
            //user is already in a flat
            createFlatButton.setVisibility(View.GONE)
            joinFlatButton.setVisibility(View.GONE)
        }
    }
}