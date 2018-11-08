package com.alimuzaffar.blank.database.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ListConverter {
    var gson = Gson()

    @TypeConverter
    fun fromString(value: String): List<String>? {
        val listType = object : TypeToken<List<String>>() {

        }.type
        return gson.fromJson<List<String>>(value, listType)
    }

    @TypeConverter
    fun fromArrayList(list: List<String>): String {
        return gson.toJson(list)
    }


    @TypeConverter
    fun fromStringToArrayListInt(value: String): List<Int>? {
        val listType = object : TypeToken<List<Int>>() {

        }.type
        return gson.fromJson<List<Int>>(value, listType)
    }

    @TypeConverter
    fun fromArrayListIntToString(list: List<Int>): String {
        return gson.toJson(list)
    }

    @TypeConverter
    fun fromStringToArrayListDouble(value: String): List<Double>? {
        val listType = object : TypeToken<List<Double>>() {

        }.type
        return gson.fromJson<List<Double>>(value, listType)
    }

    @TypeConverter
    fun fromArrayListDoubleToString(list: List<Double>): String {
        return gson.toJson(list)
    }
}
