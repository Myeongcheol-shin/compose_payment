package com.shino72.wallet.db


import androidx.room.Database
import androidx.room.RoomDatabase
import com.shino72.wallet.db.entity.OttDB
import com.shino72.wallet.db.dao.OttDao

@Database(entities = [OttDB::class], version = 1, exportSchema = false)
abstract class OttDatabase : RoomDatabase() {
    abstract fun ottDao(): OttDao
}