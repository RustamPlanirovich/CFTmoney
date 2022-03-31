package com.nauka.cftmoney.model.room.dao

import androidx.room.*
import com.nauka.cftmoney.model.dto.Main

@Dao
interface MainDao {

    //Запрос на получение всех данных из базы данных
    @Query("SELECT * FROM main")
    fun getAll(): Main?

    //Функция для вставки данных в базу данных
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(main: Main?)

    //Функция для обновления имеющихся данных в базе данных
    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(main: Main?)

}