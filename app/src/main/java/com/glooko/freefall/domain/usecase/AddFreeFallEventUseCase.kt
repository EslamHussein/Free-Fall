package com.glooko.freefall.domain.usecase

import com.glooko.freefall.domain.FreeFallRepository
import kotlinx.coroutines.flow.Flow


class AddFreeFallEventUseCase(private val repository: FreeFallRepository) :
    UseCase<AddFreeFallEventUseCase.Params, Flow<Long>> {
    override fun execute(params: Params?): Flow<Long> {
        return if (params == null)
            throw Throwable("Params shouldn't be null")
        else {
            repository.addFreeFallEvent(params)
        }
    }

    data class Params(val timeStamp: String) {
        companion object {
            fun create(timeStamp: String) = Params(timeStamp)
        }
    }
}
