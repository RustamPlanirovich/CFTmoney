package com.nauka.cftmoney.model.room.dao

import androidx.room.*
import com.nauka.cftmoney.model.dto.Main

@Dao
interface MainDao {

    @Query("SELECT * FROM main")
    fun getAll(): Main?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(main: Main?)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(main: Main?)

}