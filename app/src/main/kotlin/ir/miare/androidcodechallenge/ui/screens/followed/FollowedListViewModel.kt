package ir.miare.androidcodechallenge.ui.screens.followed

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.miare.androidcodechallenge.core.Logger
import ir.miare.androidcodechallenge.domain.repository.Repository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

internal class FollowedListViewModel(
    private val repository: Repository,
    private val logger: Logger,
) : ViewModel() {
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

    val players = repository.observeFollowedPlayers()
        .catch { logger.e(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = listOf()
        )

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
