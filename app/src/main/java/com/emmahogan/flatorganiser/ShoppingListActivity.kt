package com.emmahogan.flatorganiser
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList


//TODO comment this file so you don't get confused by your bullshit 1am code again
class ShoppingListActivity : AppCompatActivity() {

    private var recyclerView: RecyclerView? = null
    private var modelArrayList: ArrayList<ReModel>? = null
    private var customAdapter: ReAdapter? = null
    private val groceriesList = arrayOf("Milk", "Eggs", "Cheese", "Fruit", "Flour", "Beans", "Carrots", "Bread")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopping_list)

        recyclerView = findViewById(R.id.recycler)

        modelArrayList = getModel(false)
        customAdapter = ReAdapter(this, modelArrayList!!)
        recyclerView!!.adapter = customAdapter
        recyclerView!!.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
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
}