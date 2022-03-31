package com.nauka.cftmoney.model

import com.nauka.cftmoney.ApiInterface
import com.nauka.cftmoney.model.room.AppDatabase
import javax.inject.Inject

class Repository @Inject constructor(
    private val service: ApiInterface,
    private val db: AppDatabase
) {
    //Функция для получения данных из сети
    fun getAPI() = service.getValuteData()
    //Функция для получения данных из базы данных
    fun getDatabase() = db.mainDao()
}