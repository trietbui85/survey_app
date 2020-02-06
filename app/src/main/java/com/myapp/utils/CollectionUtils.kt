package com.myapp.utils

object CollectionUtils {
    // Merge 2 lists into a new MutableList
    fun <T> merge2List(list1: List<T>?, list2: List<T>?): MutableList<T> {
        if (list1.isNullOrEmpty()) {
            return list2?.toMutableList() ?: mutableListOf()
        }
        return list1.toMutableList().also {
            if (!list2.isNullOrEmpty()) {
                it.addAll(list2)
            }
        }
    }
}