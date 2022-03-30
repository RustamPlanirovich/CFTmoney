package com.nauka.cftmoney

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


class OtherServicesTypeConverter {

//    @TypeConverter
//    fun stringToList(data: String): HashMap<String, CurrencyModel> {
//        val mapType = object : TypeToken<HashMap<String, CurrencyModel>>() {}.type
//        return Gson().fromJson(data, mapType)
//    }
//
//    @TypeConverter
//    fun listToString(objects: HashMap<String, CurrencyModel>): String {
//        val gson = Gson()
//        return gson.toJson(objects)
//    }

    @TypeConverter
    fun stringToListServer(data: String?): HashMap<String, CurrencyModel>? {
        val listType: Type = object :
            TypeToken<HashMap<String, CurrencyModel>?>() {}.type
        return Gson().fromJson<HashMap<String, CurrencyModel>>(data, listType)
    }

    @TypeConverter
    fun listServerToString(someObjects: HashMap<String, CurrencyModel>?): String? {
        val gson = Gson()
        return gson.toJson(someObjects)
    }

    @TypeConverter
    fun fromString(value: String?): List<String> {
        val listType = object :
            TypeToken<ArrayList<String?>?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromList(list: List<String?>?): String {
        val gson = Gson()
        return gson.toJson(list)
    }

//    @TypeConverter
//    @JvmStatic
//    fun stringToList(data: String): ArrayList<Main> {
//        val mapType = object : TypeToken<ArrayList<Main>>() {}.type
//        return Gson().fromJson(data, mapType)
//    }
//
//    @TypeConverter
//    @JvmStatic
//    fun listToString(objects: ArrayList<Main>): String {
//        val gson = Gson()
//        return gson.toJson(objects)
//    }
}