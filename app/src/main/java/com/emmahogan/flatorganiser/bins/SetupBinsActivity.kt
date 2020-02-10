package com.emmahogan.flatorganiser.bins

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.emmahogan.flatorganiser.auth.User
import android.app.DatePickerDialog
import java.util.Calendar;
import android.widget.EditText
import com.emmahogan.flatorganiser.CloudFirestore


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

    lateinit var redCalendar : Button
    lateinit var yellowCalendar : Button
    lateinit var greenCalendar : Button
    lateinit var blackCalendar : Button


    lateinit var picker: DatePickerDialog
    //private var eText: EditText? = null //TODO check when to do this and when to use lateinit...unclear to me


    override fun onCreate(savedInstanceState : Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(com.emmahogan.flatorganiser.R.layout.activity_setup_bins)

        //get user data
        currentUser = intent.getParcelableExtra("user")

        setUpBinSelect()
        setUpFrequencyMenu()
        setUpSpinners()
        setUpCalendars()
        setUpNavigatorBar()
    }


    private fun setUpBinSelect(){
        val redBtn : Button = findViewById(com.emmahogan.flatorganiser.R.id.red)
        redBtn.setOnClickListener{ changeBorder(redBtn) }

        val greenBtn : Button = findViewById(com.emmahogan.flatorganiser.R.id.green)
        greenBtn.setOnClickListener{ changeBorder(greenBtn) }

        val yellowBtn : Button = findViewById(com.emmahogan.flatorganiser.R.id.yellow)
        yellowBtn.setOnClickListener{ changeBorder(yellowBtn) }

        val blackBtn : Button = findViewById(com.emmahogan.flatorganiser.R.id.black)
        blackBtn.setOnClickListener { changeBorder(blackBtn) }
    }


    private fun setUpFrequencyMenu(){
        val frequency_question : TextView = findViewById(com.emmahogan.flatorganiser.R.id.frequency_question)
        frequency_question.setVisibility(View.GONE)

        redTableRow = findViewById(com.emmahogan.flatorganiser.R.id.red_time)
        yellowTableRow = findViewById(com.emmahogan.flatorganiser.R.id.yellow_time)
        greenTableRow = findViewById(com.emmahogan.flatorganiser.R.id.green_time)
        blackTableRow = findViewById(com.emmahogan.flatorganiser.R.id.black_time)

        redTableRow.setVisibility(View.INVISIBLE)
        yellowTableRow.setVisibility(View.INVISIBLE)
        greenTableRow.setVisibility(View.INVISIBLE)
        blackTableRow.setVisibility(View.INVISIBLE)
    }


    private fun setUpCalendars(){
        redCalendar = findViewById(com.emmahogan.flatorganiser.R.id.red_calendar)
        yellowCalendar = findViewById(com.emmahogan.flatorganiser.R.id.yellow_calendar)
        greenCalendar = findViewById(com.emmahogan.flatorganiser.R.id.green_calendar)
        blackCalendar = findViewById(com.emmahogan.flatorganiser.R.id.black_calendar)

        redCalendar.setOnClickListener{ openCalendar(redCalendar) }
        yellowCalendar.setOnClickListener{ openCalendar(yellowCalendar) }
        greenCalendar.setOnClickListener{ openCalendar(greenCalendar) }
        blackCalendar.setOnClickListener{ openCalendar(blackCalendar) }
    }


    private fun setUpNavigatorBar(){
        val cancel : Button = findViewById(com.emmahogan.flatorganiser.R.id.cancel)
        cancel.setOnClickListener{ onBackPressed() }

        val submit : Button = findViewById(com.emmahogan.flatorganiser.R.id.submit)
        submit.setOnClickListener{ onSubmit() }
    }


    private fun changeBorder(btn : Button){
        //TODO fix this monstrosity
        if(btn.tag == "0"){
            btn.tag = "1"
            when(btn.id){
                com.emmahogan.flatorganiser.R.id.red -> {
                    btn.setBackgroundResource(com.emmahogan.flatorganiser.R.drawable.red_bin_selected_bg)
                    redTableRow.setVisibility(View.VISIBLE)
                }
                com.emmahogan.flatorganiser.R.id.yellow -> {
                    btn.setBackgroundResource(com.emmahogan.flatorganiser.R.drawable.yellow_bin_selected_bg)
                    yellowTableRow.setVisibility(View.VISIBLE)
                }
                com.emmahogan.flatorganiser.R.id.green -> {
                    btn.setBackgroundResource(com.emmahogan.flatorganiser.R.drawable.green_bin_selected_bg)
                    greenTableRow.setVisibility(View.VISIBLE)
                }
                com.emmahogan.flatorganiser.R.id.black -> {
                    btn.setBackgroundResource(com.emmahogan.flatorganiser.R.drawable.black_bin_selected_bg)
                    blackTableRow.setVisibility(View.VISIBLE)
                }
            }
        }
        else{
            btn.tag = "0"
            when(btn.id){
                com.emmahogan.flatorganiser.R.id.red -> {
                    btn.setBackgroundResource(com.emmahogan.flatorganiser.R.drawable.red_bin_bg)
                    redTableRow.setVisibility(View.INVISIBLE)
                }
                com.emmahogan.flatorganiser.R.id.yellow -> {
                    btn.setBackgroundResource(com.emmahogan.flatorganiser.R.drawable.yellow_bin_bg)
                    yellowTableRow.setVisibility(View.INVISIBLE)
                }
                com.emmahogan.flatorganiser.R.id.green -> {
                    btn.setBackgroundResource(com.emmahogan.flatorganiser.R.drawable.green_bin_bg)
                    greenTableRow.setVisibility(View.INVISIBLE)
                }
                com.emmahogan.flatorganiser.R.id.black -> {
                    btn.setBackgroundResource(com.emmahogan.flatorganiser.R.drawable.black_bin_bg)
                    blackTableRow.setVisibility(View.INVISIBLE)
                }
            }
        }
    }


    fun setUpSpinners() {
        redSpinner = findViewById(com.emmahogan.flatorganiser.R.id.red_spinner)
        yellowSpinner = findViewById(com.emmahogan.flatorganiser.R.id.yellow_spinner)
        greenSpinner = findViewById(com.emmahogan.flatorganiser.R.id.green_spinner)
        blackSpinner = findViewById(com.emmahogan.flatorganiser.R.id.black_spinner)
    }


    override fun onBackPressed(){
        this.finish()
    }


    private fun onSubmit(){
        //check all info filled out....if not just display toast

        //if info correctly filled out:
        val flat = currentUser.flat

        val listData = HashMap<String, Any>()

        listData.put("red", mapOf("start_data" to redCalendar.text.toString(), "frequency" to redSpinner.selectedItem.toString() ))
        listData.put("yellow", mapOf("start_data" to yellowCalendar.text.toString(), "frequency" to yellowSpinner.selectedItem.toString() ))
        listData.put("green", mapOf("start_data" to greenCalendar.text.toString(), "frequency" to greenSpinner.selectedItem.toString() ))
        listData.put("black", mapOf("start_data" to blackCalendar.text.toString(), "frequency" to blackSpinner.selectedItem.toString() ))

        (CloudFirestore::addBinDates)(CloudFirestore(), flat.toString(), listData)
    }


    private fun openCalendar(calendar : Button){
        val cldr = Calendar.getInstance()
        val day = cldr.get(Calendar.DAY_OF_MONTH)
        val month = cldr.get(Calendar.MONTH)
        val year = cldr.get(Calendar.YEAR)
        // date picker dialog
        picker = DatePickerDialog(this,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                calendar.setText(dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year)
            }, year, month, day
        )
        picker.show()
    }
}