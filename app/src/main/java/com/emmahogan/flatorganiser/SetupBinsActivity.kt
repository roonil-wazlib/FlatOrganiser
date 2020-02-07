package com.emmahogan.flatorganiser

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class SetupBinsActivity : AppCompatActivity() {

    lateinit var currentUser : User

    override fun onCreate(savedInstanceState : Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup_bins)

        //get user data
        currentUser = intent.getParcelableExtra("user")

        setUpWidgets()
    }

    private fun setUpWidgets(){
        val redBtn : Button = findViewById(R.id.red)
        redBtn.setOnClickListener{ changeBorder(redBtn) }

        val greenBtn : Button = findViewById(R.id.green)
        greenBtn.setOnClickListener{ changeBorder(greenBtn) }

        val yellowBtn : Button = findViewById(R.id.yellow)
        yellowBtn.setOnClickListener{ changeBorder(yellowBtn) }

        val blackBtn : Button = findViewById(R.id.black)
        blackBtn.setOnClickListener { changeBorder(blackBtn) }
    }


    private fun changeBorder(btn : Button){
        //TODO fix this monstrosity
        if(btn.tag == "0"){
            btn.tag = "1"
            when(btn.id){
                R.id.red -> btn.setBackgroundResource(R.drawable.red_bin_selected_bg)
                R.id.yellow -> btn.setBackgroundResource(R.drawable.yellow_bin_selected_bg)
                R.id.green -> btn.setBackgroundResource(R.drawable.green_bin_selected_bg)
                R.id.black -> btn.setBackgroundResource(R.drawable.black_bin_selected_bg)
            }
        }
        else{
            btn.tag = "0"
            when(btn.id){
                R.id.red -> btn.setBackgroundResource(R.drawable.red_bin_bg)
                R.id.yellow -> btn.setBackgroundResource(R.drawable.yellow_bin_bg)
                R.id.green -> btn.setBackgroundResource(R.drawable.green_bin_bg)
                R.id.black -> btn.setBackgroundResource(R.drawable.black_bin_bg)
            }
        }
    }
}