package com.emmahogan.flatorganiser.dinner_plan

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.emmahogan.flatorganiser.CloudFirestore
import com.emmahogan.flatorganiser.R
import com.emmahogan.flatorganiser.auth.User
import java.util.ArrayList


class DinnerAdapter(private val context : Context?, imageModelArrayListMain: ArrayList<DayItem>, currentUser: User) :
    RecyclerView.Adapter<DinnerAdapter.MyViewHolder>() {


    companion object {
        lateinit var imageModelArrayList: ArrayList<DayItem>
    }

    private val inflater : LayoutInflater
    var holderList = mutableListOf<MyViewHolder>()
    var user = User()


    init {
        inflater = LayoutInflater.from(context)
        imageModelArrayList = imageModelArrayListMain
        user = currentUser
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : MyViewHolder {
        //set layout for each item in recyclerview
        val view = inflater.inflate(R.layout.single_day_dinner, parent, false)
        val item =  MyViewHolder(view)
        holderList.add(item)
        return item
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        //set up each item view as it appears on screen while scrolling
        holder.day.setText(imageModelArrayList[position].day)
        holder.chefEditText.setText(imageModelArrayList[position].chef)
        holder.mealEditText.setText(imageModelArrayList[position].meal)
        holder.ingredientsEditText.setText(imageModelArrayList[position].ingredients)
    }


    override fun getItemCount(): Int {
        //get number of items in list
        return imageModelArrayList.size
    }


    fun updateDayItemInfo(){
        //pretty gross but there's only ever going to be 7 things in each list so not a huge issue
        for (x in imageModelArrayList){
            for (y in holderList){
                if (x.day == y.day.text){
                    x.chef = y.chefEditText.text.toString()
                    x.meal = y.mealEditText.text.toString()
                    x.ingredients = y.ingredientsEditText.toString()
                }
            }
        }
    }


    fun writeToDb(){
        val listData = HashMap<String, Any>()
        for (x in holderList){
            val info = mapOf("meal" to x.mealEditText.text.toString(), "chef" to x.chefEditText.text.toString(), "ingredients" to x.ingredientsEditText.text.toString())
            listData.put(x.day.text.toString(),info)
        }

        (CloudFirestore::addDinnerInfo)(CloudFirestore(), user.flat.toString(), listData)
    }


    fun clearAll(){
        for (x in imageModelArrayList){
            x.chef = ""
            x.meal = ""
            x.ingredients = ""
        }

        for (x in holderList) {
            x.ingredientsEditText.setText("")
            x.chefEditText.setText("")
            x.mealEditText.setText("")
        }
    }


    //the 'thing' that the data is stored in and gets recycled during scroll
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //inner class represents individual items within recylcerview
        var mealEditText : EditText
        var chefEditText : EditText
        var day : TextView
        var ingredientsEditText : EditText
        init {
            day = itemView.findViewById(R.id.day)
            chefEditText = itemView.findViewById(R.id.chef)
            mealEditText = itemView.findViewById(R.id.meal)
            ingredientsEditText = itemView.findViewById(R.id.ingredients)
        }
    }
}