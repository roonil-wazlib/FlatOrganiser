package com.emmahogan.flatorganiser.todo

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.emmahogan.flatorganiser.R
import com.emmahogan.flatorganiser.auth.User
import java.nio.file.Files.size
import java.util.*
import kotlin.collections.mutableListOf


class TodoAdapter(private val context : Context, imageModelArrayListMain: ArrayList<TodoItem>, currentUser: User) :
    RecyclerView.Adapter<TodoAdapter.MyViewHolder>() {


    companion object {
        lateinit var itemList: ArrayList<TodoItem>
    }


    private val inflater : LayoutInflater
    var holderList = mutableListOf<MyViewHolder>()
    var user = User()

    init {
        inflater = LayoutInflater.from(context)
        itemList = imageModelArrayListMain //list of ReModel class instances
        user = currentUser
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : MyViewHolder {
        //set layout for each item in recyclerview
        val view = inflater.inflate(R.layout.todo_item, parent, false)
        val item =  MyViewHolder(view)
        holderList.add(item)
        return item
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        //set up each item view as it appears on screen while scrolling
        holder.itemCheckBox.text = TodoAdapter.itemList[position].title
        holder.itemDate.text = itemList[position].date
        holder.itemPriority.text = itemList[position].priority
        holder.itemTimeRemaining.text = itemList[position].timeRemaining
        holder.itemCheckBox.isChecked = itemList[position].checked

        //TODO check item here

        //TODO set up recycler view for sublist here


        //TODO add on click listener to open dialog box
        holder.itemCheckBox.setOnClickListener{

            val actualPosition = holder.adapterPosition
            var activity : TodoActivity = context as TodoActivity
            Log.d("TAG", (!itemList[actualPosition].checked).toString())
            (TodoActivity::clickCheckbox)(activity, itemList[actualPosition], actualPosition)
        }
        holder.itemCardView.setOnClickListener {
            val actualPosition = holder.getAdapterPosition()
            var activity : TodoActivity = context as TodoActivity
            (TodoActivity::openEditDialog)(activity, itemList[actualPosition], actualPosition)
        }
    }

    override fun getItemCount(): Int {
        //get number of items in list
        return itemList.size
    }


    fun addItem(item : TodoItem, originalPosition : Int){
        //add new item to recycler view
        if (originalPosition != -1){
            itemList[originalPosition] = item
            //TODO when priority algo written, will need to move to new position
            //notifyItemInserted(originalPosition)
            notifyItemChanged(originalPosition)
        }
        else {
            itemList.add(item)
            notifyItemRangeChanged(0, -1)
        }
    }

    //the 'thing' that the data is stored in and gets recycled during scroll
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //inner class represents individual items within recylcerview
        //TODO add remaining attributes
        var itemCheckBox : CheckBox
        var itemDate : TextView
        var itemPriority : TextView
        var itemTimeRemaining : TextView
        var itemCardView : CardView
        init {
            itemCheckBox = itemView.findViewById(R.id.item_checkbox)
            itemDate = itemView.findViewById(R.id.item_date)
            itemPriority = itemView.findViewById(R.id.item_priority)
            itemTimeRemaining = itemView.findViewById(R.id.time_remainingTV)
            itemCardView = itemView.findViewById(R.id.card_view)
        }
    }
}