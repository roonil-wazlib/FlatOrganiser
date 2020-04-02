package com.emmahogan.flatorganiser.todo

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emmahogan.flatorganiser.CloudFirestore
import com.emmahogan.flatorganiser.R
import com.emmahogan.flatorganiser.auth.User
import com.emmahogan.flatorganiser.shopping_list.ListItem
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.todo_dialog.*
import java.util.*
import kotlin.collections.HashMap

class TodoActivity : AppCompatActivity() {

    private var recyclerView: RecyclerView? = null
    private var modelArrayList: ArrayList<TodoItem>? = null
    private lateinit var customAdapter: TodoAdapter

    lateinit var currentUser : User
    private var db = FirebaseFirestore.getInstance()
    lateinit var myTodoList : HashMap<String, Any>
    lateinit var flatTodoList : HashMap<String, Any>
    private var myTodo = true

    private var mAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {

        //get current user
        currentUser = intent.getParcelableExtra("user")

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo)

        recyclerView = findViewById(R.id.todo)

        val newItemBtn : FloatingActionButton = findViewById(R.id.newItemBtn)
        newItemBtn.setOnClickListener{ addItem() }

        //listen for flat to do list
        val flatDocRef = db.collection("flats/${currentUser.flat.toString()}/data").document("todo")
        flatDocRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d("TAG", "DocumentSnapshot data: ${document.data}")
                    try{flatTodoList = document.data as HashMap<String, Any>}
                    catch(e: TypeCastException){
                        flatTodoList = hashMapOf()
                    }
                    createList(flatTodoList)
                } else {
                    Log.d("TAG", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("TAG", "get failed with ", exception)
            }


        //listen for user personal to do list
        val docRef = db.collection("flats/${currentUser.flat.toString()}/members/${mAuth.currentUser!!.uid}/data").document("todo")
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d("TAG", "DocumentSnapshot data: ${document.data}")
                    try{myTodoList = document.data as HashMap<String, Any>}
                    catch(e: TypeCastException){
                        myTodoList = hashMapOf()
                    }
                    createList(myTodoList)
                } else {
                    Log.d("TAG", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("TAG", "get failed with ", exception)
            }

        val myBtn : Button = findViewById(R.id.myBtn)
        myBtn.setOnClickListener{
            myTodo = true
            createList(myTodoList)
        }

        val flatBtn : Button = findViewById(R.id.flatBtn)
        flatBtn.setOnClickListener{
            myTodo = false
            createList(flatTodoList)
        }

    }

    private fun createModel(data : HashMap<String, Any>): ArrayList<TodoItem> {
        val list = ArrayList<TodoItem>()
        for ((key, value) in data){ value as HashMap<String, String>

            val model = TodoItem()
            model.setItemTitle(key)
            value["priority"]?.let { model.setItemPriority(it) }
            value["date"]?.let { model.setItemDueDate(it) }
            list.add(model)
        }
        return list
    }


    private fun createList(data : HashMap<String, Any>){
        modelArrayList = createModel(data)
        customAdapter = TodoAdapter(this, modelArrayList!!, currentUser)
        recyclerView!!.adapter = customAdapter
        recyclerView!!.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
    }


    private fun addItem(){
        //open custom to-do dialog box
        val dialogView = LayoutInflater.from(this).inflate(R.layout.todo_dialog, null)
        //AlertDialogBuilder
        val builder = AlertDialog.Builder(this)
            .setView(dialogView)
            .setTitle("Enter Details")
        //show dialog
        val alertDialog = builder.show()
        val saveBtn = alertDialog.save
        val cancelBtn = alertDialog.cancel

        val calendarBtn = alertDialog.calendar
        calendarBtn.setOnClickListener{ openCalendar(calendarBtn) }

        saveBtn.setOnClickListener{
            if (myTodo){
                addToDb(alertDialog.item_name.text.toString(), alertDialog.calendar.text.toString(), alertDialog.priority.selectedItem.toString(), myTodoList)
            }
            else {
                addToDb(alertDialog.item_name.text.toString(), alertDialog.calendar.text.toString(), alertDialog.priority.selectedItem.toString(), flatTodoList)
            }

            alertDialog.dismiss()
            Toast.makeText(this, "Added", Toast.LENGTH_SHORT).show()
        }
        cancelBtn.setOnClickListener{
            alertDialog.dismiss()
        }
        alertDialog.show()
    }

    private fun addItemToView(name : String, date : String, priority : String) {
        val model = TodoItem()
        model.setItemTitle(name)
        model.setItemPriority(priority)
        model.setItemDueDate(date)
        customAdapter!!.addItem(model)
    }


    private fun addToDb(name : String, date : String, priority : String, data : HashMap<String, Any>){
        //if info correctly filled out:
        val flat = currentUser.flat
        data[name] = mapOf("date" to date, "priority" to priority)

        if (myTodo) {
            (CloudFirestore::addPersonalTodo)(CloudFirestore(), currentUser, flat.toString(), data)
        }
        else {
            (CloudFirestore::addFlatTodo)(CloudFirestore(), currentUser, flat.toString(), data)
        }
        Toast.makeText(this, "Added to database", Toast.LENGTH_SHORT).show()

        //update view
        addItemToView(name, date, priority)
    }


    private fun openCalendar(calendarBtn : Button){
        val calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH)
        val year = calendar.get(Calendar.YEAR)
        // date picker dialog
        val picker = DatePickerDialog(this,
            DatePickerDialog.OnDateSetListener { _, selectedYear, monthOfYear, dayOfMonth ->
                val date = "${dayOfMonth}/${monthOfYear + 1}/${selectedYear}"
                calendarBtn.text = date
            }, year, month, day
        )
        picker.show()
    }
}