package com.emmahogan.flatorganiser

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList


//TODO comment this file so you don't get confused by your bullshit 1am code again
class ReAdapter(private val context : Context, imageModelArrayListMain: ArrayList<ListItem>) :
    RecyclerView.Adapter<ReAdapter.MyViewHolder>() {

    companion object {
        lateinit var imageModelArrayList: ArrayList<ListItem>
    }

    private val inflater : LayoutInflater

    init {
        inflater = LayoutInflater.from(context)
        imageModelArrayList = imageModelArrayListMain //list of ReModel class instances
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : MyViewHolder {
        //set layout for each item in recyclerview
        val view = inflater.inflate(R.layout.recyclerview_item, parent, false)
        return MyViewHolder(view)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        //position is index of grocery in array list of grocery item class instances

        holder.groceryCheckBox.isChecked = imageModelArrayList[position].getSelected()
        holder.groceryCheckBox.setText(imageModelArrayList[position].getItemName())

        holder.groceryCheckBox.tag = position //reset tag on item to current position

        //listen for clicks on checkboxes and respond
        holder.groceryCheckBox.setOnClickListener {
            val pos = holder.groceryCheckBox.tag as Int


            if (imageModelArrayList[pos].getSelected()) {
                imageModelArrayList[pos].setSelecteds(false)
            } else {
                imageModelArrayList[pos].setSelecteds(true)
            }
        }

        holder.deleteBtn.setOnClickListener {
            imageModelArrayList.removeAt(position)
            notifyItemRemoved(position)
            //updateItemTags()
        }
    }


    override fun getItemCount(): Int {
        //get number of items in list
        return imageModelArrayList.size
    }

    fun addItem(item : ListItem){
        //add new item to recycler view
        imageModelArrayList.add(item)
        notifyItemInserted(itemCount)
    }


    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //inner class represents individual items within recylcerview
        var groceryCheckBox: CheckBox
        var deleteBtn: Button
        init {
            groceryCheckBox = itemView.findViewById(R.id.grocery_checkbox)
            deleteBtn = itemView.findViewById(R.id.delete)
        }
    }
}