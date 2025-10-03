package ir.miare.androidcodechallenge.ui.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.cachedIn
import ir.miare.androidcodechallenge.domain.models.SortType
import ir.miare.androidcodechallenge.domain.usecase.ObserveLeaguesWithPlayersUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn

internal class HomeViewModel(
    private val observeLeaguesWithPlayersUseCase: ObserveLeaguesWithPlayersUseCase,
) : ViewModel() {

    var currentSortType by mutableStateOf(SortType.None)
        private set

    val items = snapshotFlow { currentSortType }
        .flatMapLatest { observeLeaguesWithPlayersUseCase(it) }
        .cachedIn(viewModelScope)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = PagingData.empty(
                sourceLoadStates = LoadStates(
                    refresh = LoadState.Loading,
                    prepend = LoadState.NotLoading(false),
                    append = LoadState.NotLoading(false),
                )
            )
        )

    fun updateSortType(sortType: SortType) {
        currentSortType = sortType
    }
}
