package com.emmahogan.flatorganiser
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_sign_up.*
import java.util.ArrayList


//TODO comment this file so you don't get confused by your bullshit 1am code again
class ShoppingListActivity : AppCompatActivity() {

    private var recyclerView: RecyclerView? = null
    private var modelArrayList: ArrayList<ListItem>? = null
    private var customAdapter: ReAdapter? = null

    private lateinit var groceriesList : HashMap<String, Any>
    lateinit var newItemET : EditText
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
    }

    //update this to populate from hashmap in db, including correct isSelect value
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
        Log.d("TAG", modelArrayList.toString())
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