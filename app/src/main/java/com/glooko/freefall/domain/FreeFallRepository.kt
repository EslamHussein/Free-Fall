package com.glooko.freefall.domain

import androidx.paging.PagingSource
import com.glooko.freefall.data.localdatasource.FreeFallEntity
import com.glooko.freefall.domain.usecase.AddFreeFallEventUseCase
import kotlinx.coroutines.flow.Flow

/**
 * Class responsible for multiple data sources interactions
 */
interface FreeFallRepository {
    /**
     * method to save free fall event
     */
    fun addFreeFallEvent(
        params: AddFreeFallEventUseCase.Params
    ): Flow<Long>

    /**
     * method to get all free fall events
     */
    fun getAllFreeFAllEvents(): PagingSource<Int, FreeFallEntity>

    /**
     * method to observe on free fall event
     */
    fun observeOnFreeFallChanges(): Flow<String>
}