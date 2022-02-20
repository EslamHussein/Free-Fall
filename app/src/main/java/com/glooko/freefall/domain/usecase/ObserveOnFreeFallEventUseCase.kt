package com.glooko.freefall.domain.usecase

import com.glooko.freefall.domain.FreeFallRepository
import kotlinx.coroutines.flow.Flow

class ObserveOnFreeFallEventUseCase(private val repository: FreeFallRepository) :
    UseCase<Unit, Flow<String>> {
    override fun execute(params: Unit?): Flow<String> {
        return repository.observeOnFreeFallChanges()
    }
}