package com.emmahogan.flatorganiser
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList


//TODO comment this file so you don't get confused by your bullshit 1am code again
class ShoppingListActivity : AppCompatActivity() {

    private var recyclerView: RecyclerView? = null
    private var modelArrayList: ArrayList<ReModel>? = null
    private var customAdapter: ReAdapter? = null
    private var groceriesList = mutableListOf("Milk", "Eggs", "Cheese", "Fruit", "Flour", "Beans", "Carrots", "Bread")

    lateinit var newItemET : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopping_list)

        recyclerView = findViewById(R.id.recycler)
        updateList()


        //get reference to new item edit text and add button
        newItemET = findViewById(R.id.new_grocery)
        val addBtn : Button = findViewById(R.id.add)
        addBtn.setOnClickListener{ addItem() }
    }

    private fun getModel(isSelect: Boolean): ArrayList<ReModel> {
        val list = ArrayList<ReModel>()
        for (item in groceriesList) {

            val model = ReModel()
            model.setSelecteds(isSelect)
            model.setGroceries(item)
            list.add(model)
        }
        return list
    }

    private fun addItem(){
        val newItem = newItemET.getText().toString()
        groceriesList.add(newItem)
        Log.d("TAG", groceriesList.toString())
        updateList()
    }

    private fun updateList(){
        modelArrayList = getModel(false)
        customAdapter = ReAdapter(this, modelArrayList!!)
        recyclerView!!.adapter = customAdapter
        recyclerView!!.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
    }
}