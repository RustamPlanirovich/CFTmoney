package com.nauka.cftmoney

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nauka.cftmoney.model.dto.CurrencyModel
import java.lang.reflect.Type


//Конвернет для созранения полученного ответа в базу данных
/*2 конвертера
1 - конвертер
listServerToString - конвертирование HashMap<String, CurrencyModel> в String
stringToListServer - конвертирование обратно в Object типа HashMap<String, CurrencyModel>
2 - конвертер
fromString - конвертируем String в List<String>
fromList - конвертируем List<String> в String
*/
class OtherServicesTypeConverter {

    //1 конвертер
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

    //2 конвертер
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

}