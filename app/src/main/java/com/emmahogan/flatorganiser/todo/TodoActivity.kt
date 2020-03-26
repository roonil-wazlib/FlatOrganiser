package com.emmahogan.flatorganiser.todo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emmahogan.flatorganiser.R
import com.emmahogan.flatorganiser.auth.User
import com.google.firebase.firestore.FirebaseFirestore
import java.util.ArrayList

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

        //TODO add new item button - floating action?

        //TODO figure out how to populate cards via a pop up
    }


    private fun createModel(): ArrayList<TodoItem> {
        val list = ArrayList<TodoItem>()

        val model = TodoItem()
        model.title = ""
        list.add(model) //testing code

        return list
    }


    fun createList(){
        modelArrayList = createModel()
        customAdapter = TodoAdapter(this, modelArrayList!!, currentUser)
        recyclerView!!.adapter = customAdapter
        recyclerView!!.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
    }
}