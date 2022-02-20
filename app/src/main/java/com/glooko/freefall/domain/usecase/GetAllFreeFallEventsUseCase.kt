package com.glooko.freefall.domain.usecase

import androidx.paging.PagingSource
import com.glooko.freefall.domain.FreeFallRepository
import com.glooko.freefall.data.localdatasource.FreeFallEntity


class GetAllFreeFallEventsUseCase(private val repository: FreeFallRepository) :
    UseCase<Unit, PagingSource<Int, FreeFallEntity>> {
    override fun execute(params: Unit?): PagingSource<Int, FreeFallEntity> {
        return repository.getAllFreeFAllEvents()
    }
}
