package ir.miare.androidcodechallenge.data.local

import androidx.paging.PagingSource
import androidx.room.RoomRawQuery
import ir.miare.androidcodechallenge.data.db.Dao
import ir.miare.androidcodechallenge.data.db.LeaguePlayerRow
import ir.miare.androidcodechallenge.data.util.QueryBuilder
import ir.miare.androidcodechallenge.domain.models.SortType
import ir.miare.androidcodechallenge.domain.models.db.LeagueEntity
import ir.miare.androidcodechallenge.domain.models.db.PlayerEntity
import ir.miare.androidcodechallenge.domain.models.db.TeamEntity

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
}
