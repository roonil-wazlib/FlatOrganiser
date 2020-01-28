package com.emmahogan.flatorganiser

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import android.widget.Button
import android.widget.LinearLayout
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
    lateinit var createJoinDisplay : LinearLayout
    lateinit var homeTableDisplay : LinearLayout


    private val nameListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot : DataSnapshot) {
            // Get User objects as iterable
            val users = dataSnapshot.children

            //find current user and update name
            for (user in users) {
                if (user.key == mAuth.currentUser!!.uid) {
                    val name = user.getValue(User::class.java)!!.name
                    val email = user.getValue(User::class.java)!!.email
                    val flatId = user.getValue(User::class.java)!!.flat
                    instantiateUser(name, email, flatId)
                    updateDisplay()
                    break
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

        //tell database to listen for name
        mDatabaseReference.addValueEventListener(nameListener)

        //set up logout button
        val logoutButton : Button = findViewById(R.id.logout_button)
        logoutButton.setOnClickListener{ logOut() }

        //set up settings button
        val settingsButton : Button = findViewById(R.id.settings)
        settingsButton.setOnClickListener{openSettings()}

        //set up create and join flat buttons
        val createFlatButton : Button = findViewById(R.id.create_flat)
        val joinFlatButton : Button = findViewById(R.id.join_flat)
        createFlatButton.setOnClickListener{ createFlat() }
        joinFlatButton.setOnClickListener{ joinFlat() }

        //initalise layouts
        createJoinDisplay = findViewById(R.id.create_join_layout)
        homeTableDisplay = findViewById(R.id.home_table_layout)
        homeTableDisplay.setVisibility(View.GONE) //changes when user data retrieved and user found to be in flat

        //initialise shopping list button
        val shoppingBtn : Button = findViewById(R.id.shopping_list)
        shoppingBtn.setOnClickListener{ openShopping() }

        //end activity when user logs out
        mAuth.addAuthStateListener {
            if(mAuth.currentUser == null){
                this.finish()
            }
        }
    }

    private fun logOut(){
        mAuth.signOut()
    }


    private fun openSettings(){
        val intent = Intent(this, SettingsActivity::class.java)
        intent.putExtra("currentUser", currentUser)
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
        if(userInFlat()) {
            Toast.makeText(this@HomeActivity, "You're already in a flat!", Toast.LENGTH_SHORT).show()
        }
        else{
            val intent = Intent(this, JoinFlatActivity::class.java)
            intent.putExtra("currentUser", currentUser)
            startActivity(intent)
        }
    }


    private fun instantiateUser(name : String?, email : String?, flatId : String?) {
        currentUser = User(name, email, flatId)
        Toast.makeText(this, "DONE", Toast.LENGTH_SHORT).show()
    }


    private fun userInFlat() : Boolean{
        return currentUser.flat != ""
    }


    private fun updateDisplay(){
        //remove add/join flat buttons if user already in flat
        if (userInFlat()){
            //user is already in a flat
            createJoinDisplay.setVisibility(View.GONE)
            homeTableDisplay.setVisibility(View.VISIBLE)
        }else{
            homeTableDisplay.setVisibility(View.GONE)
            createJoinDisplay.setVisibility(View.VISIBLE)
        }
    }

    private fun openShopping(){
        val intent = Intent(this, ShoppingListActivity::class.java)
        startActivity(intent)
    }
}