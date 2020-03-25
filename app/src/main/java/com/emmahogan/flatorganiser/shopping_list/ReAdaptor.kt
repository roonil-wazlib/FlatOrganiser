package com.emmahogan.flatorganiser.shopping_list

import android.R.attr.data
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.emmahogan.flatorganiser.CloudFirestore
import com.emmahogan.flatorganiser.R
import com.emmahogan.flatorganiser.auth.User
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.mutableListOf


//this class takes data from dataset and uses it to communicate with the layoutmanager
class ReAdapter(private val context : Context, imageModelArrayListMain: ArrayList<ListItem>, currentUser: User) :
    RecyclerView.Adapter<ReAdapter.MyViewHolder>() {


    companion object {
        lateinit var imageModelArrayList: ArrayList<ListItem>
    }


    private val inflater : LayoutInflater
    var holderList = mutableListOf<MyViewHolder>()
    var user = User()


    init {
        inflater = LayoutInflater.from(context)
        imageModelArrayList = imageModelArrayListMain //list of ReModel class instances
        user = currentUser
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

        //listen for clicks on checkboxes and respond
        holder.groceryCheckBox.setOnClickListener {

            if (imageModelArrayList[position].getSelected()) {
                imageModelArrayList[position].setSelecteds(false)
            } else {
                imageModelArrayList[position].setSelecteds(true)
            }
            writeToDb()
        }

        holder.deleteBtn.setOnClickListener {
            imageModelArrayList.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, itemCount)
            writeToDb()
        }
    }


    override fun getItemCount(): Int {
        //get number of items in list
        return imageModelArrayList.size
    }


    fun addItem(item : ListItem){
        //add new item to recycler view
        imageModelArrayList.add(item)
        notifyItemInserted(itemCount-1)
        notifyItemRangeChanged(itemCount-1, itemCount)
    }


    fun writeToDb(){
        val listData = HashMap<String, Any>()
        for (x in imageModelArrayList!!){
            listData.put(x.getItemName(), x.getSelected())
        }
        (CloudFirestore::addShoppingList)(CloudFirestore(), user.flat.toString(), listData)
    }

    fun clearAll(){
        val size: Int = imageModelArrayList.size
        imageModelArrayList.clear()
        notifyItemRangeRemoved(0, size)
        writeToDb()
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