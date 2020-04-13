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
    private lateinit var myTodoList : HashMap<String, Any>
    private lateinit var flatTodoList : HashMap<String, Any>
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
                    flatTodoList = try{
                        document.data as HashMap<String, Any>
                    } catch(e: TypeCastException){
                        hashMapOf()
                    }
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
                    myTodoList = try{
                        document.data as HashMap<String, Any>
                    } catch(e: TypeCastException){
                        hashMapOf()
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
            value["timeRemaining"]?.let { model.setItemTimeRemaining(it) }
            value["checked"]?.let { model.checked = it.toBoolean() }
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
        //open custom to-do TodoActivity()dialog box
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
                addToDb(alertDialog.item_name.text.toString(), alertDialog.calendar.text.toString(), alertDialog.priority.selectedItem.toString(), alertDialog.time_remaining.text.toString(), myTodoList)
            }
            else {
                addToDb(alertDialog.item_name.text.toString(), alertDialog.calendar.text.toString(), alertDialog.priority.selectedItem.toString(), alertDialog.time_remaining.text.toString(), flatTodoList)
            }

            alertDialog.dismiss()
            Toast.makeText(this, "Added", Toast.LENGTH_SHORT).show()
        }
        cancelBtn.setOnClickListener{
            alertDialog.dismiss()
        }
        alertDialog.show()
    }


    fun openEditDialog(item : TodoItem, position : Int){
        //where position is index in list

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

        //prefill current details
        val calendarBtn = alertDialog.calendar
        calendarBtn.setOnClickListener{ openCalendar(calendarBtn) }
        calendarBtn.text = item.date

        alertDialog.item_name.setText(item.title)
        //alertDialog.priority.text = item.priority you can't do this you dumb fuck
        alertDialog.time_remaining.setText(item.timeRemaining)

        var priorityPosition = 0
        //loop through priority list and find index of priority
        for ( (index, string) in resources.getStringArray(R.array.priority_array).withIndex() ) {
            if (string == item.priority) {
                priorityPosition = index
            }
        }
        alertDialog.priority.setSelection(priorityPosition)

        saveBtn.setOnClickListener{
            if (myTodo){
                //update database
                myTodoList.remove(item.title)
                addToDb(alertDialog.item_name.text.toString(), alertDialog.calendar.text.toString(), alertDialog.priority.selectedItem.toString(), alertDialog.time_remaining.text.toString(), myTodoList, position, item.checked.toString())
            }
            else {
                //update database
                flatTodoList.remove(item.title)
                addToDb(alertDialog.item_name.text.toString(), alertDialog.calendar.text.toString(), alertDialog.priority.selectedItem.toString(), alertDialog.time_remaining.text.toString(), flatTodoList, position, item.checked.toString())
            }

            alertDialog.dismiss()
            Toast.makeText(this, "Added", Toast.LENGTH_SHORT).show()
        }
        cancelBtn.setOnClickListener{
            alertDialog.dismiss()
        }
        alertDialog.show()
    }


    private fun addItemToView(name : String, date : String, priority : String, timeRemaining : String, originalPosition : Int, checked : Boolean = false) {
        val model = TodoItem()
        model.setItemTitle(name)
        model.setItemPriority(priority)
        model.setItemDueDate(date)
        model.setItemTimeRemaining(timeRemaining)
        model.checkItem(checked)
        customAdapter.addItem(model, originalPosition)
    }


    private fun addToDb(name : String, date : String, priority : String, timeRemaining : String, data : HashMap<String, Any>, originalPosition : Int = -1, checked : String = "false"){
        //if info correctly filled out:
        val flat = currentUser.flat
        data[name] = mapOf("date" to date, "priority" to priority, "timeRemaining" to timeRemaining, "checked" to checked)

        if (myTodo) {
            (CloudFirestore::addPersonalTodo)(CloudFirestore(), currentUser, flat.toString(), data)
        }
        else {
            (CloudFirestore::addFlatTodo)(CloudFirestore(), currentUser, flat.toString(), data)
        }
        Toast.makeText(this, "Added to database", Toast.LENGTH_SHORT).show()

        //update view
        addItemToView(name, date, priority, timeRemaining, originalPosition, checked.toBoolean())
    }


    fun clickCheckbox(item : TodoItem, position : Int){
        addToDb(item.title, item.date, item.priority, item.timeRemaining, myTodoList, position, (!item.checked).toString())
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