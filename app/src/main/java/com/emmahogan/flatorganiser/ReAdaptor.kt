package com.emmahogan.flatorganiser

import android.content.Context
import android.nfc.Tag
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList


//this class takes data from dataset and uses it to communicate with the layoutmanager
class ReAdapter(private val context : Context, imageModelArrayListMain: ArrayList<ListItem>) :
    RecyclerView.Adapter<ReAdapter.MyViewHolder>() {

    companion object {
        lateinit var imageModelArrayList: ArrayList<ListItem>
    }

    private val inflater : LayoutInflater
    var holderList = mutableListOf<MyViewHolder>()

    init {
        inflater = LayoutInflater.from(context)
        imageModelArrayList = imageModelArrayListMain //list of ReModel class instances
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : MyViewHolder {
        //set layout for each item in recyclerview
        val view = inflater.inflate(R.layout.recyclerview_item, parent, false)
        val item =  MyViewHolder(view)
        holderList.add(item)
        return item
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        //set up each item view as it appears on screen while scrolling

        holder.groceryCheckBox.isChecked = imageModelArrayList[position].getSelected()
        holder.groceryCheckBox.setText(imageModelArrayList[position].getItemName())

        holder.groceryCheckBox.tag = position //set initial tag to current position
        holder.deleteBtn.tag = position


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
            val pos = holder.deleteBtn.tag as Int
            Log.d("TAG", pos.toString())
            imageModelArrayList.removeAt(pos)
            holderList.removeAt(pos)
            notifyItemRemoved(pos)
            updateTags()
        }
    }

    fun updateTags(){
        imageModelArrayList.forEachIndexed { i, element ->
            holderList[i].groceryCheckBox.tag = i
            holderList[i].deleteBtn.tag = i
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



    //the 'thing' that the data is stored in and gets recycled during scroll
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