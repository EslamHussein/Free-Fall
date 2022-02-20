package com.glooko.freefall.domain.usecase

interface UseCase<P, R> {
    fun execute(params: P? = null): R
}