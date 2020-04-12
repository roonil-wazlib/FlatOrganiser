package com.emmahogan.flatorganiser.todo


class TodoItem {
    var title : String = ""
    var priority : String = ""
    var date : String = ""
    var timeRemaining : String = ""
    var checked : Boolean = false

    //TODO add item checked var

    fun setItemTitle(title : String){
        this.title = title
    }

    fun setItemPriority(priority : String){
        this.priority = priority
    }

    fun setItemDueDate(date : String){
        this.date = date
    }

    fun setItemTimeRemaining(time : String){
        this.timeRemaining = time
    }

    fun checkItem(checked : Boolean){
        this.checked = checked
    }

}