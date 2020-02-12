package com.emmahogan.flatorganiser.bins

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.emmahogan.flatorganiser.auth.User
import com.emmahogan.flatorganiser.display.HomeActivity
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*
import kotlin.reflect.typeOf


class BinsActivity : AppCompatActivity() {

    lateinit var currentUser : User
    lateinit var date : String
    var bins = HashMap<String, BinInfo>()

    private var db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.emmahogan.flatorganiser.R.layout.activity_bins)

        //get user data
        currentUser = intent.getParcelableExtra("user")

        date = getCurrentDate()


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
//                    for (bin in bins){
//                        Log.d("TAG", bin.toString())
//                    }

                    //TODO set up activity here

                } else {
                    Log.d("TAG", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("TAG", "get failed with ", exception)
            }
    }


    override fun onBackPressed(){
        val intent = Intent(this, HomeActivity::class.java)
        intent.putExtra("user", currentUser)
        startActivity(intent)
        this.finish()
    }


    fun getCurrentDate() : String {
        val c = Calendar.getInstance().getTime()
        println("Current time => $c")

        val df = SimpleDateFormat("dd/MMM/yyyy")
        val formattedDate = df.format(c)

        return formattedDate
    }
}

data class BinInfo(
    var frequency : String,
    var startDate : String
)