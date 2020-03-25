package com.emmahogan.flatorganiser.shopping_list

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emmahogan.flatorganiser.CloudFirestore
import com.emmahogan.flatorganiser.R
import com.emmahogan.flatorganiser.auth.User
import com.emmahogan.flatorganiser.dinner_plan.DinnerAdapter
import com.google.firebase.firestore.FirebaseFirestore
import java.util.ArrayList



class ShoppingListActivity : AppCompatActivity() {

    private var recyclerView: RecyclerView? = null
    private var modelArrayList: ArrayList<ListItem>? = null
    private lateinit var customAdapter: ReAdapter

    private lateinit var groceriesList : HashMap<String, Any>
    private lateinit var newItemET : EditText
    lateinit var currentUser : User

    private var db = FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {

        //get current user
        currentUser = intent.getParcelableExtra("user")

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopping_list)

        recyclerView = findViewById(R.id.recycler)

        val docRef = db.collection("flats/${currentUser.flat.toString()}/data").document("shopping_list")
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d("TAG", "DocumentSnapshot data: ${document.data}")
                    try{groceriesList = document.data as HashMap<String, Any>}
                    catch(e: TypeCastException){
                        groceriesList = hashMapOf()
                    }
                    createList()
                } else {
                    Log.d("TAG", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("TAG", "get failed with ", exception)
            }


        //get reference to new item edit text and add button
        newItemET = findViewById(R.id.new_grocery)
        val addBtn : Button = findViewById(R.id.add)
        addBtn.setOnClickListener{ addItem() }

        val clearBtn : Button = findViewById(R.id.clear)
        clearBtn.setOnClickListener{ clear() }
    }


    private fun createModel(): ArrayList<ListItem> {
        val list = ArrayList<ListItem>()
        for ((key, value) in groceriesList) {

            //get class instance for each item in list
            val model = ListItem()
            //set selected checkbuttons
            model.setSelecteds(value as Boolean)
            //set grocery name
            model.setItemName(key)
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
        modelArrayList = createModel()
        customAdapter =
            ReAdapter(this, modelArrayList!!, currentUser)
        recyclerView!!.adapter = customAdapter
        recyclerView!!.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
    }


    private fun writeToDb(){
        val listData = HashMap<String, Any>()
        for (x in modelArrayList!!){
            listData.put(x.getItemName(), x.getSelected())
        }
        (CloudFirestore::addShoppingList)(CloudFirestore(), currentUser.flat.toString(), listData)
    }

    private fun clear(){
        //wipe database, reset view
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Clear whole week?")
        builder.setMessage("This action is not reversable")


        builder.setPositiveButton("Yes"){_,_ ->
            (ReAdapter::clearAll)(customAdapter)
        }

        builder.setNegativeButton("No"){_,_ ->
            //close alert dialog
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}