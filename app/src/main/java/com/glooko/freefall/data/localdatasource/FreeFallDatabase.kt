package com.glooko.freefall.data.localdatasource

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * Free fall Database class to define DAO's , entities and db version
 */
@Database(entities = [FreeFallEntity::class], version = 1)
abstract class FreeFallDatabase : RoomDatabase() {
    abstract fun freeFallDao(): FreeFallDao
}