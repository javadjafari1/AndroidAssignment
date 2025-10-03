package ir.miare.androidcodechallenge.data.local

import androidx.paging.PagingSource
import androidx.room.RoomRawQuery
import ir.miare.androidcodechallenge.data.db.Dao
import ir.miare.androidcodechallenge.data.db.LeaguePlayerRow
import ir.miare.androidcodechallenge.data.util.QueryBuilder
import ir.miare.androidcodechallenge.domain.models.SortType
import ir.miare.androidcodechallenge.domain.models.db.FollowedEntity
import ir.miare.androidcodechallenge.domain.models.db.LeagueEntity
import ir.miare.androidcodechallenge.domain.models.db.PlayerEntity
import ir.miare.androidcodechallenge.domain.models.db.PlayerWithTeamAndFollowed
import ir.miare.androidcodechallenge.domain.models.db.TeamEntity
import kotlinx.coroutines.flow.Flow

internal class LocalDataSourceImpl(
    private val dao: Dao,
) : LocalDataSource {
    override suspend fun insertLeaguesWithTeamsAndPlayers(
        leagues: List<LeagueEntity>,
        teams: List<TeamEntity>,
        players: List<PlayerEntity>
    ) {
        dao.insertLeaguesWithTeamsAndPlayers(
            leagues = leagues,
            teams = teams,
            players = players,
        )
    }

    override fun observeLeaguesWithPlayers(sortType: SortType): PagingSource<Int, LeaguePlayerRow> {
        val query = QueryBuilder(sortType).build()
        return dao.observeLeaguesWithPlayersSortedByRank(RoomRawQuery(query))
    }

    override fun observePlayerWithTeam(id: Int): Flow<PlayerWithTeamAndFollowed?> {
        return dao.observePlayerWithTeam(id)
    }

    override suspend fun followPlayer(id: Int) {
        dao.insertFollowEntity(FollowedEntity(id))
    }

    override suspend fun unfollowPlayer(id: Int) {
        dao.removeFollowEntity(id)
    }

    override fun observeFollowedPlayers(): Flow<List<PlayerWithTeamAndFollowed>> {
        return dao.observeFollowedPlayers()
    }
}
