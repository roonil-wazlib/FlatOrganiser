package com.emmahogan.flatorganiser.todo

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.emmahogan.flatorganiser.R
import com.emmahogan.flatorganiser.auth.User
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
        holder.itemCheckBox.setText(TodoAdapter.itemList[position].title)


        //TODO set up recycler view for sublist here
    }

    override fun getItemCount(): Int {
        //get number of items in list
        return itemList.size
    }


    fun addItem(item : TodoItem){
        //add new item to recycler view
        //TODO
        itemList.add(item)
        notifyItemInserted(-1)
    }

    //the 'thing' that the data is stored in and gets recycled during scroll
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //inner class represents individual items within recylcerview
        //TODO add remaining attributes
        var itemCheckBox: CheckBox
        init {
            itemCheckBox = itemView.findViewById(R.id.item_checkbox)
        }
    }
}