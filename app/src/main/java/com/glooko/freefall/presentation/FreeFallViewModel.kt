package com.glooko.freefall.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.glooko.freefall.data.localdatasource.FreeFallEntity
import com.glooko.freefall.domain.usecase.GetAllFreeFallEventsUseCase
import com.glooko.freefall.domain.usecase.ObserveOnFreeFallEventUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class FreeFallStatus(
    val showAlert: Boolean = false
)

class FreeFallViewModel(
    private val getAllFreeFallEventsUseCase: GetAllFreeFallEventsUseCase,
    private val observeOnFreeFallEventUseCase: ObserveOnFreeFallEventUseCase,
    private val dispatcher: CoroutineDispatcher) : ViewModel() {
    private val _state = mutableStateOf(FreeFallStatus())
    val state: State<FreeFallStatus> = _state

    init {
        observeOnFreeFall()
    }

    private fun observeOnFreeFall() {
        viewModelScope.launch {
            observeOnFreeFallEventUseCase.execute()
                .flowOn(dispatcher)
                .collect {
                    _state.value = _state.value.copy(showAlert = true)
                }
        }
    }

    fun dismissAlert() {
        _state.value = _state.value.copy(showAlert = false)
    }

    val allEvents: Flow<PagingData<FreeFallEntity>> =
        Pager(config = PagingConfig(3, enablePlaceholders = true, maxSize = 200)) {
            getAllFreeFallEventsUseCase.execute()
        }.flow.flowOn(dispatcher)
}