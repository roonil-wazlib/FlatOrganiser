package com.emmahogan.flatorganiser.bins

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.emmahogan.flatorganiser.R
import com.emmahogan.flatorganiser.auth.User
import com.emmahogan.flatorganiser.display.HomeActivity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_bins.view.*
import java.text.SimpleDateFormat
import java.util.*




class BinsActivity : AppCompatActivity() {

    lateinit var currentUser : User
    lateinit var date : String
    var bins = HashMap<String, BinInfo>()
    var binTV = HashMap<String, TextView>()

    private var db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.emmahogan.flatorganiser.R.layout.activity_bins)

        //get user data
        currentUser = intent.getParcelableExtra("user")

        date = getCurrentDate()

        //get textview objects
        setUpTextViews()


        val docRef = db.collection("flats/${currentUser.flat.toString()}/data").document("bin_dates")
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d("TAG", "DocumentSnapshot data: ${document.data}")
                    for ((bin, info) in document.data!!){
                        info as Map<String, String>

                        val binColour = bin.toString()
                        val frequency = info["frequency"].toString()
                        val startDate = info["start_data"].toString()

                        bins.put(binColour, BinInfo(frequency, startDate))
                    }
                    populateDisplay()

                } else {
                    Log.d("TAG", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("TAG", "get failed with ", exception)
            }
    }


    private fun setUpTextViews(){
        val redTV : TextView = findViewById(R.id.red_bin)
        binTV.put("red", redTV)
        val yellowTV : TextView = findViewById(R.id.yellow_bin)
        binTV.put("yellow", yellowTV)
        val greenTV : TextView = findViewById(R.id.green_bin)
        binTV.put("green", greenTV)
        val blackTV : TextView = findViewById(R.id.black_bin)
        binTV.put("black", blackTV)
    }


    override fun onBackPressed(){
        val intent = Intent(this, HomeActivity::class.java)
        intent.putExtra("user", currentUser)
        startActivity(intent)
        this.finish()
    }


    private fun getCurrentDate() : String {
        val c = Calendar.getInstance().getTime()
        println("Current time => $c")

        val df = SimpleDateFormat("dd/MMM/yyyy")
        val formattedDate = df.format(c)

        return formattedDate
    }


    private fun populateDisplay(){
        for(bin in bins){
            val colour = bin.key
            val day = getDayFromDate(bin.value.startDate)
            val frequency = bin.value.frequency
            updateTV(colour, day, frequency)
        }
    }


    private fun getDayFromDate(date : String) : String{
        val date = SimpleDateFormat("dd/M/yyyy").parse(date)
        val day =  SimpleDateFormat("EEEE").format(date)

        return day.toString()
    }


    private fun updateTV(colour : String, day : String, frequency : String){
        val format = "${colour.capitalize()} bin goes out every ${frequency} on ${day}s."
        binTV[colour]!!.text = format
    }
}

data class BinInfo(
    var frequency : String,
    var startDate : String
)