package com.emmahogan.flatorganiser

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val billsButton: Button = findViewById(R.id.bills_button)
        billsButton.setOnClickListener { onClick() }
    }

    fun onClick(){
        val intent = Intent(this, BillsActivity::class.java)
        startActivity(intent)
    }
}
