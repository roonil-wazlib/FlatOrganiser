package com.emmahogan.flatorganiser
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_sign_up.*
import java.util.ArrayList


//TODO comment this file so you don't get confused by your bullshit 1am code again
class ShoppingListActivity : AppCompatActivity() {

    private var recyclerView: RecyclerView? = null
    private var modelArrayList: ArrayList<ListItem>? = null
    private var customAdapter: ReAdapter? = null
    private var groceriesList = mutableListOf<String>()

    lateinit var newItemET : EditText
    lateinit var currentUser : User


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopping_list)

        //get current user
        currentUser = intent.getParcelableExtra("user")

        recyclerView = findViewById(R.id.recycler)
        createList()

        //get reference to new item edit text and add button
        newItemET = findViewById(R.id.new_grocery)
        val addBtn : Button = findViewById(R.id.add)
        addBtn.setOnClickListener{ addItem() }
    }

    //update this to populate from hashmap in db, including correct isSelect value
    private fun getModel(isSelect: Boolean): ArrayList<ListItem> {
        val list = ArrayList<ListItem>()
        for (item in groceriesList) {

            //get class instance for each item in list
            val model = ListItem()
            //set selected checkbuttons
            model.setSelecteds(isSelect)
            //set grocery name
            model.setItemName(item)
            //add class instance to list of grocery class instances
            list.add(model)
        }
        return list
    }


    private fun addItem(){
        val name = newItemET.getText().toString()
        val model = ListItem()
        model.setItemName(name)
        customAdapter!!.addItem(model)
        newItemET.setText("")
        writeToDb()
    }


    private fun createList(){
        modelArrayList = getModel(false)
        customAdapter = ReAdapter(this, modelArrayList!!, currentUser)
        recyclerView!!.adapter = customAdapter
        recyclerView!!.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
    }


    fun writeToDb(){
        val listData = HashMap<String, Any>()
        for (x in modelArrayList!!){
            listData.put(x.getItemName(), x.getSelected())
        }
        (CloudFirestore::addShoppingList)(CloudFirestore(), currentUser.flat.toString(), listData)
    }
}