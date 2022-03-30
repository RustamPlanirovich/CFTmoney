package com.nauka.cftmoney.model

import com.nauka.cftmoney.ApiInterface
import com.nauka.cftmoney.model.room.AppDatabase
import javax.inject.Inject

class Repository @Inject constructor(
    private val service: ApiInterface,
    private val db: AppDatabase
) {
    fun getAPI() = service.getValuteData()
    fun getDatabase() = db.mainDao()
}