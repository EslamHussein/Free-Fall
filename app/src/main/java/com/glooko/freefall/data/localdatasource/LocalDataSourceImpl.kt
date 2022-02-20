package com.glooko.freefall.data.localdatasource

import com.glooko.freefall.data.LocalDataSource
import kotlinx.coroutines.flow.flow

class LocalDataSourceImpl(private val freeFallDao: FreeFallDao) : LocalDataSource {
    override fun addFreeFallEvent(timeStamp: String) =
        flow {
            val recordId = freeFallDao.insertAll(FreeFallEntity(timeStamp = timeStamp))
            if (recordId > 0) emit(recordId) else throw Throwable("Can't insert record")
        }

    override fun getAllFreeFAllEvents() = freeFallDao.getAllFreeFAllEvents()
}