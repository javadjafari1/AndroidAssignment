package ir.miare.androidcodechallenge.domain.repository

import androidx.paging.PagingData
import ir.miare.androidcodechallenge.data.model.ListDataItem
import ir.miare.androidcodechallenge.domain.models.SortType
import ir.miare.androidcodechallenge.domain.models.db.PlayerWithTeamAndFollowed
import ir.miare.androidcodechallenge.domain.models.ui.PlayerItemUiModel
import kotlinx.coroutines.flow.Flow

internal interface Repository {
    suspend fun fetchData()
    fun observePlayerWithTeam(id: Int): Flow<PlayerWithTeamAndFollowed?>
    fun observeLeaguesWithPlayers(sortType: SortType): Flow<PagingData<ListDataItem>>
    suspend fun followPlayer(id: Int)
    suspend fun unfollowPlayer(id: Int)
    fun observeFollowedPlayers(): Flow<List<PlayerItemUiModel>>
}
