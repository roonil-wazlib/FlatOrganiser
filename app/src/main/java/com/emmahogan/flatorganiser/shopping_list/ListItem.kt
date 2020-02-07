package com.emmahogan.flatorganiser.shopping_list


//POJO for setting up list items
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