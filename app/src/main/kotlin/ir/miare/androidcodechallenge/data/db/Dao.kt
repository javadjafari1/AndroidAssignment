package ir.miare.androidcodechallenge.data.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.RawQuery
import androidx.room.RoomRawQuery
import androidx.room.Transaction
import ir.miare.androidcodechallenge.domain.models.db.LeagueEntity
import ir.miare.androidcodechallenge.domain.models.db.PlayerEntity
import ir.miare.androidcodechallenge.domain.models.db.TeamEntity

@Dao
internal interface Dao {
    @Transaction
    suspend fun insertLeaguesWithTeamsAndPlayers(
        leagues: List<LeagueEntity>,
        teams: List<TeamEntity>,
        players: List<PlayerEntity>
    ) {
        insertLeagues(leagues)
        insertTeams(teams)
        insertPlayers(players)
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPlayers(players: List<PlayerEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTeams(teams: List<TeamEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertLeagues(leagues: List<LeagueEntity>)

    @RawQuery(
        observedEntities = [
            TeamEntity::class,
            LeagueEntity::class,
            PlayerEntity::class
        ]
    )
    fun observeLeaguesWithPlayersSortedByRank(rawQuery: RoomRawQuery): PagingSource<Int, LeaguePlayerRow>
}
