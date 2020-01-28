package com.emmahogan.flatorganiser


//TODO comment this file so you don't get confused by your bullshit 1am code again
class ListItem {

    var isSelected : Boolean = false
    var name : String? = null

    fun getItemName(): String {
        return name.toString()
    }

    fun setItemName(grocery: String) {
        this.name = grocery
    }

    fun getSelected(): Boolean {
        return isSelected
    }

    fun setSelecteds(selected: Boolean) {
        isSelected = selected
    }

}