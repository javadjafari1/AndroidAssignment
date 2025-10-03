package ir.miare.androidcodechallenge.data.local

import androidx.paging.PagingSource
import ir.miare.androidcodechallenge.data.db.LeaguePlayerRow
import ir.miare.androidcodechallenge.domain.models.SortType
import ir.miare.androidcodechallenge.domain.models.db.LeagueEntity
import ir.miare.androidcodechallenge.domain.models.db.PlayerEntity
import ir.miare.androidcodechallenge.domain.models.db.TeamEntity

internal interface LocalDataSource {
    suspend fun insertLeaguesWithTeamsAndPlayers(
        leagues: List<LeagueEntity>,
        teams: List<TeamEntity>,
        players: List<PlayerEntity>
    )

    fun observeLeaguesWithPlayers(sortType: SortType): PagingSource<Int, LeaguePlayerRow>
}
