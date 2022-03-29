package com.nauka.cftmoney.Room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.nauka.cftmoney.Main
import com.nauka.cftmoney.OtherServicesTypeConverter
import com.nauka.cftmoney.Room.dao.MainDao

@Database(entities = [Main::class], version = 1, exportSchema = false)
@TypeConverters(OtherServicesTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun mainDao(): MainDao
}