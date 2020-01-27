package com.emmahogan.flatorganiser


//TODO comment this file so you don't get confused by your bullshit 1am code again
class ReModel {

    var isSelected: Boolean = false
    var grocery: String? = null

    fun getGroceries(): String {
        return grocery.toString()
    }

    fun setGroceries(grocery: String) {
        this.grocery = grocery
    }

    fun getSelected(): Boolean {
        return isSelected
    }

    fun setSelecteds(selected: Boolean) {
        isSelected = selected
    }

}