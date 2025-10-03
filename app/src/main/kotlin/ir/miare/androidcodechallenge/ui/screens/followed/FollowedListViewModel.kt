package ir.miare.androidcodechallenge.ui.screens.followed

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.miare.androidcodechallenge.domain.models.db.PlayerWithTeamAndFollowed
import ir.miare.androidcodechallenge.domain.repository.Repository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

internal class FollowedListViewModel(
    private val repository: Repository,
) : ViewModel() {

    var selectedPlayerToShow: PlayerWithTeamAndFollowed? by mutableStateOf(null)
        private set

    val players = repository.observeFollowedPlayers()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = listOf()
        )

    fun getPlayerWithId(id: Int) {
        runCatching {
            viewModelScope.launch {
                repository.observePlayerWithTeam(id)
                    .onEach { selectedPlayerToShow = it }
                    .launchIn(viewModelScope)
            }
        }
    }

    fun removeSelectedPlayer() {
        selectedPlayerToShow = null
    }

    fun updateFollowStatus(shouldFollow: Boolean, id: Int) {
        viewModelScope.launch {
            if (shouldFollow) {
                repository.followPlayer(id)
            } else {
                repository.unfollowPlayer(id)
            }
        }
    }
}
