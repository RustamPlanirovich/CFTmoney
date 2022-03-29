package com.nauka.cftmoney

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


object OtherServicesTypeConverter {

//    @TypeConverter
//    @JvmStatic
//    fun stringToList(data: String): HashMap<String, CurrencyModel> {
//        val mapType = object : TypeToken<HashMap<String, CurrencyModel>>() {}.type
//        return Gson().fromJson(data, mapType)
//    }
//
//    @TypeConverter
//    @JvmStatic
//    fun listToString(objects: HashMap<String, CurrencyModel>): String {
//        val gson = Gson()
//        return gson.toJson(objects)
//    }

    @TypeConverter
    @JvmStatic
    fun stringToList(data: String): ArrayList<Main> {
        val mapType = object : TypeToken<ArrayList<Main>>() {}.type
        return Gson().fromJson(data, mapType)
    }

    @TypeConverter
    @JvmStatic
    fun listToString(objects: ArrayList<Main>): String {
        val gson = Gson()
        return gson.toJson(objects)
    }
}