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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        //get user data
        //get intent bundles
        currentUser = intent.getParcelableExtra("user")

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
        updateDisplay()

        //initialise shopping list button
        val shoppingBtn : Button = findViewById(R.id.shopping_list)
        shoppingBtn.setOnClickListener{ openShopping() }

        //go back to MainActivity on LogOut
        mAuth.addAuthStateListener {
            if(mAuth.currentUser == null){
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                this.finish()
            }
        }
    }

    private fun logOut(){
        mAuth.signOut()
    }


    private fun openSettings(){
        val intent = Intent(this, SettingsActivity::class.java)
        intent.putExtra("user", currentUser)
        startActivity(intent)
    }


    private fun createFlat(){

        if(userInFlat()){
            Toast.makeText(this@HomeActivity, "You're already in a flat!", Toast.LENGTH_SHORT).show()
        }
        else{
            val intent = Intent(this, CreateFlatActivity::class.java)
            intent.putExtra("user", currentUser)
            startActivity(intent)
        }
    }


    private fun joinFlat(){
        if(userInFlat()) {
            Toast.makeText(this@HomeActivity, "You're already in a flat!", Toast.LENGTH_SHORT).show()
        }
        else{
            val intent = Intent(this, JoinFlatActivity::class.java)
            intent.putExtra("user", currentUser)
            startActivity(intent)
        }
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
        intent.putExtra("user", currentUser)
        startActivity(intent)
    }
}