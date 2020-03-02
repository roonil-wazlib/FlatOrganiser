package com.emmahogan.flatorganiser.flat

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.emmahogan.flatorganiser.CloudFirestore
import com.emmahogan.flatorganiser.display.HomeActivity
import com.emmahogan.flatorganiser.R
import com.emmahogan.flatorganiser.RealtimeDatabase
import com.emmahogan.flatorganiser.auth.User


class JoinFlatActivity : AppCompatActivity() {

    lateinit var currentUser : User
    lateinit var flatIdET : EditText

    override fun onCreate(savedInstanceState : Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join_flat)

        //get intent bundles
        currentUser = intent.getParcelableExtra("user")

        val previousFlat = currentUser.flat.toString()

        //set up widgets
        flatIdET = findViewById(R.id.flat_id)
        val joinBtn : Button = findViewById(R.id.join_flat)
        joinBtn.setOnClickListener{ joinFlat(previousFlat) }

    }

    private fun joinFlat(previousFlat : String){
        (CloudFirestore::joinFlat)(CloudFirestore(), flatIdET.getText().toString(), currentUser)
        if(previousFlat != flatIdET.text.toString()){
            deleteFromFlat(previousFlat) //fixes removal from current flat bug
        }
        Toast.makeText(this, "Adding to flat", Toast.LENGTH_LONG).show()
        onBackPressed()

    }

    private fun deleteFromFlat(previousFlat : String){
        //delete user from collection of flat members
        (CloudFirestore::deleteFromFlat)(CloudFirestore(), previousFlat, currentUser)
        //update user bin info
        currentUser.unAddBins()
        (RealtimeDatabase::updateUser)(RealtimeDatabase(), currentUser)
    }

    override fun onBackPressed(){
        val intent = Intent(this, HomeActivity::class.java)
        intent.putExtra("user", currentUser)
        startActivity(intent)
        this.finish()
    }
}