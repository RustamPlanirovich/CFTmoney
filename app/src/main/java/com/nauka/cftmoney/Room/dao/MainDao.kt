package com.nauka.cftmoney.Room.dao

import androidx.room.*
import com.nauka.cftmoney.Main

@Dao
interface MainDao {


    @Query("SELECT * FROM main")
    fun getAll(): Main?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(main: Main?)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(main: Main?)

}