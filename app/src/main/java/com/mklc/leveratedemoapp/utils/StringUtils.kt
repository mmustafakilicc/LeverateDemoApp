package com.mklc.leveratedemoapp.utils

fun String.compareDouble(value: String): Int?{
    val doubleThis = this.toDoubleOrNull()
    val doubleValue = value.toDoubleOrNull()
    if(doubleValue != null && doubleThis != null){
        return if(doubleThis > doubleValue){
            1
        }else if (doubleThis < doubleValue){
            -1
        }else{
            0
        }
    }

    return null
}