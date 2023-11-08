package com.example.mymate

class BillListValues {
    var deleteList: ArrayList<String> = ArrayList()

    fun clearAll() {
        deleteList.clear()
    }

    fun putid(id: String) {
        deleteList.add(id)
    }

    fun deleteid(id: String) {
        deleteList.remove(id)
    }
}