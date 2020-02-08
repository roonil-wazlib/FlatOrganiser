package com.emmahogan.flatorganiser.bins

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.emmahogan.flatorganiser.R
import com.emmahogan.flatorganiser.auth.User
import kotlinx.android.synthetic.main.activity_setup_bins.*

class SetupBinsActivity : AppCompatActivity() {

    lateinit var currentUser : User

    lateinit var redTableRow : TableRow
    lateinit var yellowTableRow : TableRow
    lateinit var greenTableRow : TableRow
    lateinit var blackTableRow : TableRow

    lateinit var redSpinner : Spinner
    lateinit var yellowSpinner : Spinner
    lateinit var greenSpinner : Spinner
    lateinit var blackSpinner : Spinner

    override fun onCreate(savedInstanceState : Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup_bins)

        //get user data
        currentUser = intent.getParcelableExtra("user")

        setUpBinSelect()
        setUpFrequencyMenu()
        setUpSpinners()
        setUpNavigatorBar()
    }


    private fun setUpBinSelect(){
        val redBtn : Button = findViewById(R.id.red)
        redBtn.setOnClickListener{ changeBorder(redBtn) }

        val greenBtn : Button = findViewById(R.id.green)
        greenBtn.setOnClickListener{ changeBorder(greenBtn) }

        val yellowBtn : Button = findViewById(R.id.yellow)
        yellowBtn.setOnClickListener{ changeBorder(yellowBtn) }

        val blackBtn : Button = findViewById(R.id.black)
        blackBtn.setOnClickListener { changeBorder(blackBtn) }
    }


    private fun setUpFrequencyMenu(){
        val frequency_question : TextView = findViewById(R.id.frequency_question)
        frequency_question.setVisibility(View.GONE)

        redTableRow = findViewById(R.id.red_time)
        yellowTableRow = findViewById(R.id.yellow_time)
        greenTableRow = findViewById(R.id.green_time)
        blackTableRow = findViewById(R.id.black_time)

        redTableRow.setVisibility(View.INVISIBLE)
        yellowTableRow.setVisibility(View.INVISIBLE)
        greenTableRow.setVisibility(View.INVISIBLE)
        blackTableRow.setVisibility(View.INVISIBLE)
    }


    private fun setUpNavigatorBar(){
        val cancel : Button = findViewById(R.id.cancel)
        cancel.setOnClickListener{ onBackPressed() }

        val submit : Button = findViewById(R.id.submit)
        submit.setOnClickListener{ onSubmit() }
    }


    private fun changeBorder(btn : Button){
        //TODO fix this monstrosity
        if(btn.tag == "0"){
            btn.tag = "1"
            when(btn.id){
                R.id.red -> {
                    btn.setBackgroundResource(R.drawable.red_bin_selected_bg)
                    redTableRow.setVisibility(View.VISIBLE)
                }
                R.id.yellow -> {
                    btn.setBackgroundResource(R.drawable.yellow_bin_selected_bg)
                    yellowTableRow.setVisibility(View.VISIBLE)
                }
                R.id.green -> {
                    btn.setBackgroundResource(R.drawable.green_bin_selected_bg)
                    greenTableRow.setVisibility(View.VISIBLE)
                }
                R.id.black -> {
                    btn.setBackgroundResource(R.drawable.black_bin_selected_bg)
                    blackTableRow.setVisibility(View.VISIBLE)
                }
            }
        }
        else{
            btn.tag = "0"
            when(btn.id){
                R.id.red -> {
                    btn.setBackgroundResource(R.drawable.red_bin_bg)
                    redTableRow.setVisibility(View.INVISIBLE)
                }
                R.id.yellow -> {
                    btn.setBackgroundResource(R.drawable.yellow_bin_bg)
                    yellowTableRow.setVisibility(View.INVISIBLE)
                }
                R.id.green -> {
                    btn.setBackgroundResource(R.drawable.green_bin_bg)
                    greenTableRow.setVisibility(View.INVISIBLE)
                }
                R.id.black -> {
                    btn.setBackgroundResource(R.drawable.black_bin_bg)
                    blackTableRow.setVisibility(View.INVISIBLE)
                }
            }
        }
    }


    fun setUpSpinners() {
        redSpinner = findViewById(R.id.red_spinner)
        yellowSpinner = findViewById(R.id.yellow_spinner)
        greenSpinner = findViewById(R.id.green_spinner)
        blackSpinner = findViewById(R.id.black_spinner)
    }

    override fun onBackPressed(){
        this.finish()
    }


    private fun onSubmit(){

    }
}