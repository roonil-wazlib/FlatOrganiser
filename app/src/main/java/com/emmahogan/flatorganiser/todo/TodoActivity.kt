package com.emmahogan.flatorganiser.todo

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emmahogan.flatorganiser.R
import com.emmahogan.flatorganiser.auth.User
import com.google.android.material.floatingactionbutton.FloatingActionButton
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
    lateinit var todoList : HashMap<String, String>

    override fun onCreate(savedInstanceState: Bundle?) {

        //get current user
        currentUser = intent.getParcelableExtra("user")

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo)

        recyclerView = findViewById(R.id.todo)

        //TODO(actually populate with infomation)
        createList()

        val newItemBtn : FloatingActionButton = findViewById(R.id.newItemBtn)
        newItemBtn.setOnClickListener{ addItem() }

        val myBtn : Button = findViewById(R.id.myBtn)
        val flatBtn : Button = findViewById(R.id.flatBtn)
    }


    private fun createModel(): ArrayList<TodoItem> {
        val list = ArrayList<TodoItem>()

        for (i in 0..8){
            val model = TodoItem()
            model.title = ""
            list.add(model) //testing code
        }
        return list
    }


    private fun createList(){
        modelArrayList = createModel()
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
        //todo set up calendar and spinner

        val calendarBtn = alertDialog.calendar
        calendarBtn.setOnClickListener{ openCalendar(calendarBtn) }
        saveBtn.setOnClickListener{ addToDb(alertDialog.item_name.text.toString()) }
        cancelBtn.setOnClickListener{
            alertDialog.dismiss()
        }
        alertDialog.show()
    }


    private fun addToDb(name : String){
        return
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