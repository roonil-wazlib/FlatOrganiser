package com.emmahogan.flatorganiser

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList


//TODO comment this file so you don't get confused by your bullshit 1am code again
class ReAdapter(private val ctx: Context, imageModelArrayListMain: ArrayList<ReModel>) :
    RecyclerView.Adapter<ReAdapter.MyViewHolder>() {

    companion object {
        lateinit var imageModelArrayList: ArrayList<ReModel>
    }

    private val inflater : LayoutInflater

    init {
        inflater = LayoutInflater.from(ctx)
        imageModelArrayList = imageModelArrayListMain
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReAdapter.MyViewHolder {

        val view = inflater.inflate(R.layout.recyclerview_item, parent, false)
        return MyViewHolder(view)
    }


    override fun onBindViewHolder(holder: ReAdapter.MyViewHolder, position: Int) {
        holder.groceryCheckBox.isChecked = imageModelArrayList[position].getSelected()
        holder.groceryCheckBox.setText(imageModelArrayList[position].getGroceries())

        holder.groceryCheckBox.tag = position
        holder.groceryCheckBox.setOnClickListener {
            val pos = holder.groceryCheckBox.tag as Int
            Toast.makeText(ctx, imageModelArrayList[pos].getGroceries() + " clicked!", Toast.LENGTH_SHORT).show() //check click recorded for correct item


            if (imageModelArrayList[pos].getSelected()) {
                imageModelArrayList[pos].setSelecteds(false)
            } else {
                imageModelArrayList[pos].setSelecteds(true)
            }
        }
    }


    override fun getItemCount(): Int {
        return imageModelArrayList.size
    }


    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var groceryCheckBox: CheckBox

        init {
            groceryCheckBox = itemView.findViewById(R.id.grocery_checkbox)
        }
    }
}