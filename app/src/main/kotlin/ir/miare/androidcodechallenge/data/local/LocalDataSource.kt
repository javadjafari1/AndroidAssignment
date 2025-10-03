package ir.miare.androidcodechallenge.data.local

import androidx.paging.PagingSource
import ir.miare.androidcodechallenge.data.db.LeaguePlayerRow
import ir.miare.androidcodechallenge.domain.models.SortType
import ir.miare.androidcodechallenge.domain.models.db.LeagueEntity
import ir.miare.androidcodechallenge.domain.models.db.PlayerEntity
import ir.miare.androidcodechallenge.domain.models.db.PlayerWithTeamAndFollowed
import ir.miare.androidcodechallenge.domain.models.db.TeamEntity
import kotlinx.coroutines.flow.Flow

internal interface LocalDataSource {
    suspend fun insertLeaguesWithTeamsAndPlayers(
        leagues: List<LeagueEntity>,
        teams: List<TeamEntity>,
        players: List<PlayerEntity>
    )

    fun observeLeaguesWithPlayers(sortType: SortType): PagingSource<Int, LeaguePlayerRow>

    fun observePlayerWithTeam(id: Int): Flow<PlayerWithTeamAndFollowed?>
    suspend fun followPlayer(id: Int)
    suspend fun unfollowPlayer(id: Int)
    fun observeFollowedPlayers(): Flow<List<PlayerWithTeamAndFollowed>>
}
