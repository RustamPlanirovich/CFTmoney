package com.nauka.cftmoney

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "main")
data class Main(
    @PrimaryKey(autoGenerate = true) val uid: Int,

    @SerializedName("Date")
    @Expose
    @ColumnInfo(name = "Date")
    val date: String? = null,

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
    @TypeConverters(OtherServicesTypeConverter::class)
    val valute: HashMap<String, CurrencyModel>
)

data class CurrencyModel(
    @SerializedName("ID")
    @PrimaryKey
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