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
import ir.miare.androidcodechallenge.core.Logger
import ir.miare.androidcodechallenge.domain.models.SortType
import ir.miare.androidcodechallenge.domain.repository.Repository
import ir.miare.androidcodechallenge.domain.usecase.ObserveLeaguesWithPlayersUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

internal class HomeViewModel(
    private val repository: Repository,
    private val observeLeaguesWithPlayersUseCase: ObserveLeaguesWithPlayersUseCase,
    private val logger: Logger,
) : ViewModel() {

    var currentSortType by mutableStateOf(SortType.None)
        private set

    private var selectedPlayerId: Int? by mutableStateOf(null)

    val selectedPlayerToShow = snapshotFlow { selectedPlayerId }
        .flatMapLatest { id ->
            if (id != null) {
                repository.observePlayerWithTeam(id)
            } else {
                flowOf(null)
            }
        }
        .catch { logger.e(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null
        )

    val items = snapshotFlow { currentSortType }
        .flatMapLatest { observeLeaguesWithPlayersUseCase(it) }
        .cachedIn(viewModelScope)
        .catch { logger.e(it) }
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

    fun getPlayerWithId(id: Int) {
        selectedPlayerId = id
    }

    fun removeSelectedPlayer() {
        selectedPlayerId = null
    }

    fun updateFollowStatus(shouldFollow: Boolean, id: Int) {
        viewModelScope.launch {
            runCatching {
                if (shouldFollow) {
                    repository.followPlayer(id)
                } else {
                    repository.unfollowPlayer(id)
                }
            }.onFailure {
                logger.e(it)
            }
        }
    }
}
