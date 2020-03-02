package com.emmahogan.flatorganiser.dinner_plan

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emmahogan.flatorganiser.R
import com.emmahogan.flatorganiser.auth.User
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import kotlin.collections.HashMap


class DinnerPlanActivity : AppCompatActivity() {

    private var recyclerView: RecyclerView? = null
    private var modelArrayList: ArrayList<DayItem>? = null
    private lateinit var customAdapter: DinnerAdapter
    lateinit var currentUser : User

    private lateinit var mealsList : HashMap<String, Any>
    private var days  = listOf<String>()

    private var db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState : Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dinner_plan)

        //get user data
        currentUser = intent.getParcelableExtra("user")
        recyclerView = findViewById(R.id.recycler)

        days = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")

        val saveBtn : Button = findViewById(R.id.save)
        saveBtn.setOnClickListener{save()}

        val day = getDayIndex()

        mealsList = HashMap()

        val docRef = db.collection("flats/${currentUser.flat.toString()}/data").document("dinner_plans")
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d("TAG", "DocumentSnapshot data: ${document.data}")
                    try{
                        mealsList = document.data as HashMap<String, Any>
                    }
                    catch(e: TypeCastException){
                        for (day in days){
                            mealsList.put(day, mapOf("meal" to "", "chef" to ""))
                        }
                    }
                    createList()
                    scrollToCurrentDay(day)
                } else {
                    Log.d("TAG", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("TAG", "get failed with ", exception)
            }
    }


    private fun createModel(): ArrayList<DayItem> {
        val list = ArrayList<DayItem>()
        for(day in days) {
            //get list in correct day order
            val model = DayItem()
            model.day = day
            Log.d("TAG", mealsList.toString())
            for ((key, value) in mealsList) {
                //get class instance for each item in list
                value as Map<String, String>
                if(key==day) {
                    model.meal = value["meal"].toString()
                    model.chef = value["chef"].toString()
                    break
                }
            }
            list.add(model)
        }
        return list
   }


    private fun createList(){
        modelArrayList = createModel()
        customAdapter = DinnerAdapter(this, modelArrayList!!, currentUser)
        recyclerView!!.adapter = customAdapter
        recyclerView!!.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
    }


    private fun save(){
        (DinnerAdapter::updateDayItemInfo)(customAdapter)
        (DinnerAdapter::writeToDb)(customAdapter)
        Toast.makeText(this, "Saving...", Toast.LENGTH_SHORT).show()
    }


    private fun getDayIndex() : Int {
        val day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
        Log.d("TAG", day.toString())
        return day - 2
    }

    private fun scrollToCurrentDay(x : Int){
        recyclerView?.scrollToPosition(x)
    }
}