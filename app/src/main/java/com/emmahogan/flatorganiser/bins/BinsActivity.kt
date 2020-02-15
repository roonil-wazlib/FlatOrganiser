package com.emmahogan.flatorganiser.bins

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.emmahogan.flatorganiser.auth.User
import com.emmahogan.flatorganiser.display.HomeActivity
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap


class BinsActivity : AppCompatActivity() {

    lateinit var currentUser : User
    lateinit var date : String
    var bins = HashMap<String, BinInfo>()
    var nextBinTV = HashMap<String, TextView>()
    var thisBinTV = HashMap<String, TextView>()

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
        val redTV : TextView = findViewById(com.emmahogan.flatorganiser.R.id.red_bin_this)
        thisBinTV["red"] = redTV
        val yellowTV : TextView = findViewById(com.emmahogan.flatorganiser.R.id.yellow_bin_this)
        thisBinTV["yellow"] = yellowTV
        val greenTV : TextView = findViewById(com.emmahogan.flatorganiser.R.id.green_bin_this)
        thisBinTV["green"] = greenTV
        val blackTV : TextView = findViewById(com.emmahogan.flatorganiser.R.id.black_bin_this)
        thisBinTV["black"] = blackTV

        val nextRedTV : TextView = findViewById(com.emmahogan.flatorganiser.R.id.red_bin)
        nextBinTV["red"] = nextRedTV
        val nextYellowTV : TextView = findViewById(com.emmahogan.flatorganiser.R.id.yellow_bin)
        nextBinTV["yellow"] = nextYellowTV
        val nextGreenTV : TextView = findViewById(com.emmahogan.flatorganiser.R.id.green_bin)
        nextBinTV["green"] = nextGreenTV
        val nextBlackTV : TextView = findViewById(com.emmahogan.flatorganiser.R.id.black_bin)
        nextBinTV["black"] = nextBlackTV
    }


    override fun onBackPressed(){
        val intent = Intent(this, HomeActivity::class.java)
        intent.putExtra("user", currentUser)
        startActivity(intent)
        this.finish()
    }


    private fun getCurrentDate() : String {
        val c = Calendar.getInstance().getTime()
        val df = SimpleDateFormat("dd/M/yyyy")
        return df.format(c)
    }


    private fun populateDisplay(){
        for(bin in bins){
            val colour = bin.key
            val day = getDayFromDate(bin.value.startDate)
            val frequency = bin.value.frequency[0].toString().toInt()

            val freqNum = frequency * 7
            var everyWeek = false
            if (freqNum == 7) everyWeek = true

            val currentDate = SimpleDateFormat("dd/M/yyyy").parse(date)
            val date = SimpleDateFormat("dd/M/yyyy").parse(bin.value.startDate)
            val nextDateFull = getNextDate(date, currentDate, freqNum)
            val nextDateFormatted = SimpleDateFormat("dd/M/yyyy").format(nextDateFull)
            val week = whichWeek(nextDateFull, currentDate)

            updateTV(colour, day, nextDateFormatted, week, everyWeek)
        }
    }


    private fun getDayFromDate(dateStr : String) : String{
        val date = SimpleDateFormat("dd/M/yyyy").parse(dateStr)
        val day =  SimpleDateFormat("EEEE").format(date)

        return day.toString()
    }


    private fun updateTV(colour : String, day : String, nextDate : String, week : String, everyWeek : Boolean){
        if(week == "this week") {
            val format = "${colour.capitalize()} bin next goes out on $day $nextDate."
            thisBinTV[colour]!!.text = format
            if(everyWeek){
                val nextWeek = addDaysToDate(SimpleDateFormat("dd/M/yyyy").parse(nextDate), 7)
                val nextWeekFormatted = SimpleDateFormat("dd/M/yyyy").format(nextWeek)
                val format = "${colour.capitalize()} bin goes out on $day $nextWeekFormatted"
                nextBinTV[colour]!!.text = format
            }
        }
        else{
            val format = "${colour.capitalize()} bin next goes out on $day $nextDate."
            nextBinTV[colour]!!.text = format
        }
    }


    private fun getNextDate(date : Date, currentDate : Date, frequency : Int) : Date {
        val diff = date.time - currentDate.time
        val seconds = diff / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24

        if (days <= 14 && days > 0) { //TODO convert to range check
            return date
        }
        else {
            val newDate = addDaysToDate(date, frequency)
            return getNextDate(newDate, currentDate, frequency)
        }
    }


    private fun addDaysToDate(date : Date, daysToAdd : Int) : Date{
        val c = Calendar.getInstance()
        c.time = date
        c.add(Calendar.DAY_OF_YEAR, daysToAdd)
        return c.time
    }


    private fun whichWeek(date : Date, currentDate: Date) : String{
        val diff = date.time - currentDate.time
        val seconds = diff / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24

        if (days <= 7){
            return "this week"
        }
        else{
            return "next week"
        }
    }
}


data class BinInfo(
    var frequency : String,
    var startDate : String
)