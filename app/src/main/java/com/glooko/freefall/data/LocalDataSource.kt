package com.glooko.freefall.data

import androidx.paging.PagingSource
import com.glooko.freefall.data.localdatasource.FreeFallEntity
import kotlinx.coroutines.flow.Flow

/**
 * Local data source class to interact with room db
 */
interface LocalDataSource {
    /**
     * method to store free fall event into database
     */
    fun addFreeFallEvent(timeStamp: String): Flow<Long>

    /**
     * method to get all free fall events from db with pagination
     */
    fun getAllFreeFAllEvents(): PagingSource<Int, FreeFallEntity>
}