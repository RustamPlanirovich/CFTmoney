package com.nauka.cftmoney.model.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

//Общий дата класс и для retrofit и для room
@Entity(tableName = "main")
data class Main(

    @SerializedName("Date")
    @Expose
    @ColumnInfo(name = "Date")
    @PrimaryKey
    val date: String,

    @SerializedName("PreviousDate")
    @Expose
    @ColumnInfo(name = "PreviousDate")
    val previousDate: String? = null,

    @SerializedName("PreviousURL")
    @Expose
    @ColumnInfo(name = "PreviousURL")
    val previousURL: String? = null,

    @SerializedName("Timestamp")
    @Expose
    @ColumnInfo(name = "Timestamp")
    val timestamp: String? = null,

    @SerializedName("Valute")
    @Expose
    @ColumnInfo(name = "Valute")
    val valute: HashMap<String, CurrencyModel>
)

data class CurrencyModel(
    @SerializedName("ID")
    @ColumnInfo(name = "ID")
    val id: String,

    @SerializedName("NumCode")
    @ColumnInfo(name = "NumCode")
    val numCode: String,

    @SerializedName("CharCode")
    @ColumnInfo(name = "CharCode")
    val charCode: String,

    @SerializedName("Nominal")
    @ColumnInfo(name = "Nominal")
    val nominal: Float,

    @SerializedName("Name")
    @ColumnInfo(name = "Name")
    val name: String,

    @SerializedName("Value")
    @ColumnInfo(name = "Value")
    val value: Float,

    @SerializedName("Previous")
    @ColumnInfo(name = "Previous")
    val previous: Float
)