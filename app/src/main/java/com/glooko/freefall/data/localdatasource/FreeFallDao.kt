package com.glooko.freefall.data.localdatasource

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

/**
 * Dao class for DB CRUD operations
 */
@Dao
interface FreeFallDao {
    @Query("SELECT * FROM FreeFallEntity ORDER BY time_stamp DESC")
    fun getAllFreeFAllEvents(): PagingSource<Int, FreeFallEntity>

    @Insert
    fun insertAll(freeFallEntities: FreeFallEntity): Long
}