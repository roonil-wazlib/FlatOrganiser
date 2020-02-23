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
        holder.chefEditText.setText(imageModelArrayList[position].chef)
        holder.mealEditText.setText(imageModelArrayList[position].meal)
    }


    override fun getItemCount(): Int {
        //get number of items in list
        return imageModelArrayList.size
    }


    fun writeToDb(){
        val listData = HashMap<String, Any>()
        for (x in imageModelArrayList!!){
            listData.put(x.day, x.chef)
        }
        (CloudFirestore::addDinnerInfo)(CloudFirestore(), user.flat.toString(), listData)
    }


    //the 'thing' that the data is stored in and gets recycled during scroll
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //inner class represents individual items within recylcerview
        var mealEditText : EditText
        var chefEditText : EditText
        var day : TextView
        init {
            day = itemView.findViewById(R.id.day)
            chefEditText = itemView.findViewById(R.id.chef)
            mealEditText = itemView.findViewById(R.id.meal)
        }
    }
}