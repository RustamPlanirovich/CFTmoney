package com.nauka.cftmoney.Room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nauka.cftmoney.CurrencyModel

@Dao
interface MainDao {


    @Query("SELECT * FROM main")
    fun getAll(): ArrayList<CurrencyModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun insertAll(main: ArrayList<CurrencyModel>)
    fun insertAll(main: ArrayList<CurrencyModel>)

}