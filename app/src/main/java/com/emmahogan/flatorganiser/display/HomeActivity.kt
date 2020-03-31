package com.emmahogan.flatorganiser.display

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import com.emmahogan.flatorganiser.R
import com.emmahogan.flatorganiser.shopping_list.ShoppingListActivity
import com.emmahogan.flatorganiser.auth.*
import com.emmahogan.flatorganiser.bins.BinsActivity
import com.emmahogan.flatorganiser.bins.SetupBinsActivity
import com.emmahogan.flatorganiser.dinner_plan.DinnerPlanActivity
import com.emmahogan.flatorganiser.flat.CreateFlatActivity
import com.emmahogan.flatorganiser.flat.JoinFlatActivity
import com.emmahogan.flatorganiser.todo.TodoActivity
import com.google.firebase.database.FirebaseDatabase



class HomeActivity : AppCompatActivity() {

    private var mAuth = FirebaseAuth.getInstance()

    lateinit var currentUser : User
    private lateinit var createJoinDisplay : LinearLayout
    private lateinit var homeTableDisplay : LinearLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        //get user data
        currentUser = intent.getParcelableExtra("user")

        setUpDashboard()

        //go back to MainActivity on LogOut
        mAuth.addAuthStateListener {
            if(mAuth.currentUser == null){
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                this.finish()
            }
        }
    }


    private fun setUpDashboard(){
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

        //initialise bins button
        val binsBtn : Button = findViewById(R.id.bins)
        binsBtn.setOnClickListener{ openBinsActivity() }

        val dinnerBtn : Button = findViewById(R.id.dinner)
        dinnerBtn.setOnClickListener{openDinnerPlanner()}

        val todoBtn : Button = findViewById(R.id.todo)
        todoBtn.setOnClickListener{openTodoActivity()}
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
        } else{
            Log.d("TAG", "WORKING")
            val intent = Intent(this, CreateFlatActivity::class.java)
            intent.putExtra("user", currentUser)
            startActivity(intent)
            this.finish()
        }
    }


    private fun joinFlat(){
        if(userInFlat()) {
            Toast.makeText(this@HomeActivity, "You're already in a flat!", Toast.LENGTH_SHORT).show()
        } else{
            val intent = Intent(this, JoinFlatActivity::class.java)
            intent.putExtra("user", currentUser)
            startActivity(intent)
            this.finish()
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
        } else{
            homeTableDisplay.setVisibility(View.GONE)
            createJoinDisplay.setVisibility(View.VISIBLE)
        }
    }


    private fun openShopping(){
        val intent = Intent(this, ShoppingListActivity::class.java)
        intent.putExtra("user", currentUser)
        startActivity(intent)
    }


    private fun openBinsActivity(){
        val intent : Intent
        when(currentUser.binsAdded){
            true -> intent = Intent(this, BinsActivity::class.java)
            false -> intent = Intent(this, SetupBinsActivity::class.java)
        }

        intent.putExtra("user", currentUser)
        startActivity(intent)
    }


    private fun openDinnerPlanner(){
        val intent = Intent(this, DinnerPlanActivity::class.java)
        intent.putExtra("user", currentUser)
        startActivity(intent)
        Toast.makeText(this, "Loading", Toast.LENGTH_SHORT).show()
    }


    private fun openTodoActivity(){
        val intent = Intent(this, TodoActivity::class.java)
        intent.putExtra("user", currentUser)
        startActivity(intent)
    }
}