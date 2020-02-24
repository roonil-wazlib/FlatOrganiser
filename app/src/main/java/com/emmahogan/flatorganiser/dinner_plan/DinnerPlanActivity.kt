package com.emmahogan.flatorganiser.dinner_plan

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emmahogan.flatorganiser.CloudFirestore
import com.emmahogan.flatorganiser.R
import com.emmahogan.flatorganiser.auth.User
import kotlinx.android.synthetic.main.activity_dinner_plan.*
import kotlinx.android.synthetic.main.single_day_dinner.*
import java.util.ArrayList

class DinnerPlanActivity : AppCompatActivity() {

    private var recyclerView: RecyclerView? = null
    private var modelArrayList: ArrayList<DayItem>? = null
    private lateinit var customAdapter: DinnerAdapter
    lateinit var currentUser : User

    private lateinit var mealsList : HashMap<String, Any>
    private var days  = listOf<String>()

    override fun onCreate(savedInstanceState : Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dinner_plan)

        //get user data
        currentUser = intent.getParcelableExtra("user")
        recyclerView = findViewById(R.id.recycler)


        val saveBtn : Button = findViewById(R.id.save)
        saveBtn.setOnClickListener{save()}

        mealsList = HashMap()

        days = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday")
        for (day in days){
            mealsList.put(day, "")
        }
        createList()
    }

    private fun createModel(): ArrayList<DayItem> {
        val list = ArrayList<DayItem>()
        for(day in days) {
            //get list in correct day order
            for ((key, value) in mealsList) {
                //get class instance for each item in list
                if(key==day) {
                    val model = DayItem()
                    model.day = key
                    model.meal = value.toString()
                    model.chef = value.toString()
                    list.add(model)
                }
            }
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
    }

}