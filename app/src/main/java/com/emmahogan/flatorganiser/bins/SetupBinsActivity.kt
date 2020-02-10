package com.emmahogan.flatorganiser.bins

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.emmahogan.flatorganiser.auth.User
import android.app.DatePickerDialog
import java.util.Calendar
import com.emmahogan.flatorganiser.CloudFirestore
import com.emmahogan.flatorganiser.R



class SetupBinsActivity : AppCompatActivity() {

    private lateinit var currentUser : User

    private lateinit var redTableRow : TableRow
    private lateinit var yellowTableRow : TableRow
    private lateinit var greenTableRow : TableRow
    private lateinit var blackTableRow : TableRow

    private lateinit var redSpinner : Spinner
    private lateinit var yellowSpinner : Spinner
    private lateinit var greenSpinner : Spinner
    private lateinit var blackSpinner : Spinner

    private lateinit var redCalendar : Button
    private lateinit var yellowCalendar : Button
    private lateinit var greenCalendar : Button
    private lateinit var blackCalendar : Button

    private var bins = mutableListOf<Bin>()

    private lateinit var picker: DatePickerDialog
    //private var eText: EditText? = null //TODO check when to do this and when to use lateinit...unclear to me


    override fun onCreate(savedInstanceState : Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup_bins)

        //get user data
        currentUser = intent.getParcelableExtra("user")

        setUpFrequencyMenu()
        setUpSpinners()
        setUpCalendars()
        setUpBinObjects()
        setUpBinSelect()
        setUpNavigatorBar()
    }


    private fun setUpBinSelect(){
        val redBtn : Button = findViewById(R.id.red)
        redBtn.setOnClickListener{ changeBorder(bins[0], redBtn) }

        val yellowBtn : Button = findViewById(R.id.yellow)
        yellowBtn.setOnClickListener{ changeBorder(bins[1], yellowBtn)}

        val greenBtn : Button = findViewById(R.id.green)
        greenBtn.setOnClickListener{ changeBorder(bins[2], greenBtn) }

        val blackBtn : Button = findViewById(R.id.black)
        blackBtn.setOnClickListener { changeBorder(bins[3], blackBtn) }
    }


    private fun setUpFrequencyMenu(){
        val frequencyQuestion : TextView = findViewById(R.id.frequency_question)
        frequencyQuestion.visibility = View.GONE

        redTableRow = findViewById(R.id.red_time)
        yellowTableRow = findViewById(R.id.yellow_time)
        greenTableRow = findViewById(R.id.green_time)
        blackTableRow = findViewById(R.id.black_time)

        redTableRow.visibility = View.INVISIBLE
        yellowTableRow.visibility = View.INVISIBLE
        greenTableRow.visibility = View.INVISIBLE
        blackTableRow.visibility = View.INVISIBLE
    }


    private fun setUpCalendars(){
        redCalendar = findViewById(R.id.red_calendar)
        yellowCalendar = findViewById(R.id.yellow_calendar)
        greenCalendar = findViewById(R.id.green_calendar)
        blackCalendar = findViewById(R.id.black_calendar)

        redCalendar.setOnClickListener{ openCalendar(redCalendar) }
        yellowCalendar.setOnClickListener{ openCalendar(yellowCalendar) }
        greenCalendar.setOnClickListener{ openCalendar(greenCalendar) }
        blackCalendar.setOnClickListener{ openCalendar(blackCalendar) }
    }


    private fun setUpNavigatorBar(){
        val cancel : Button = findViewById(R.id.cancel)
        cancel.setOnClickListener{ onBackPressed() }

        val submit : Button = findViewById(R.id.submit)
        submit.setOnClickListener{ onSubmit() }
    }


    private fun changeBorder(bin : Bin, btn : Button){
        if (bin.selected){
            bin.deselectBin()
            bin.section.visibility = View.INVISIBLE
            btn.setBackgroundResource(bin.noBorderBg)
        }
        else{
            bin.selectBin()
            bin.section.visibility = View.VISIBLE
            btn.setBackgroundResource(bin.borderBg)
        }
    }


    private fun setUpSpinners() {
        redSpinner = findViewById(R.id.red_spinner)
        yellowSpinner = findViewById(R.id.yellow_spinner)
        greenSpinner = findViewById(R.id.green_spinner)
        blackSpinner = findViewById(R.id.black_spinner)
    }


    private fun setUpBinObjects(){
        val redBin = Bin("red", redSpinner, redCalendar, redTableRow, R.drawable.red_bin_bg, R.drawable.red_bin_selected_bg)
        val yellowBin = Bin("yellow", yellowSpinner, yellowCalendar, yellowTableRow, R.drawable.yellow_bin_bg, R.drawable.yellow_bin_selected_bg)
        val greenBin = Bin("green", greenSpinner, greenCalendar, greenTableRow, R.drawable.green_bin_bg, R.drawable.green_bin_selected_bg)
        val blackBin = Bin("black", blackSpinner, blackCalendar, blackTableRow, R.drawable.black_bin_bg, R.drawable.black_bin_selected_bg)

        bins.add(redBin)
        bins.add(yellowBin)
        bins.add(greenBin)
        bins.add(blackBin)
    }


    override fun onBackPressed(){
        this.finish()
    }


    private fun onSubmit(){
        //check all info filled out....if not just display toast
        if(!atLeastOneBin()){
            Toast.makeText(this, "You must select at least one bin.", Toast.LENGTH_SHORT).show()
        }
        else if (infoFilledOut()){
            //if info correctly filled out:
            val flat = currentUser.flat
            val listData = HashMap<String, Any>()

            for (bin in bins){
                if (bin.selected) listData.put(bin.colour, mapOf("start_data" to bin.date.text.toString(), "frequency" to bin.frequency.selectedItem.toString()))
            }

            (CloudFirestore::addBinDates)(CloudFirestore(), flat.toString(), listData)
            Toast.makeText(this, "Added to database", Toast.LENGTH_SHORT).show()
        }
        else{
            Toast.makeText(this, "Please fill out all dates and times", Toast.LENGTH_SHORT).show()
        }
    }


    private fun openCalendar(calendarBtn : Button){
        val calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH)
        val year = calendar.get(Calendar.YEAR)
        // date picker dialog
        picker = DatePickerDialog(this,
            DatePickerDialog.OnDateSetListener {_, selectedYear, monthOfYear, dayOfMonth ->
                val date = "${dayOfMonth}/${monthOfYear + 1}/${selectedYear}"
                calendarBtn.text = date
            }, year, month, day
        )
        picker.show()
    }


    private fun infoFilledOut() : Boolean{
        var isSelected = true
        for (bin in bins){
            if (bin.selected){
                if (bin.frequency.selectedItem.toString() == "Select:" || bin.date.text.toString() == "Date"){
                    isSelected = false
                }
            }
        }
        return isSelected
    }


    private fun atLeastOneBin() : Boolean{
        var somethingSelected = false
        for (bin in bins){
            if (bin.selected) somethingSelected = true
        }
        return somethingSelected
    }
}


data class Bin(
    var colour: String,
    var frequency : Spinner,
    var date : Button,
    var section : TableRow,
    var noBorderBg : Int,
    var borderBg : Int,
    var selected : Boolean = false
) {
    fun selectBin(){
        this.selected = true
    }
    fun deselectBin(){
        this.selected = false
    }
}