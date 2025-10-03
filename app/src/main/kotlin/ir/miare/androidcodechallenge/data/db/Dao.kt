package ir.miare.androidcodechallenge.data.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.RoomRawQuery
import androidx.room.Transaction
import ir.miare.androidcodechallenge.domain.models.db.FollowedEntity
import ir.miare.androidcodechallenge.domain.models.db.LeagueEntity
import ir.miare.androidcodechallenge.domain.models.db.PlayerEntity
import ir.miare.androidcodechallenge.domain.models.db.PlayerWithTeamAndFollowed
import ir.miare.androidcodechallenge.domain.models.db.TeamEntity
import kotlinx.coroutines.flow.Flow

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

    @Transaction
    @Query("select * from players where id = :id")
    fun observePlayerWithTeam(id: Int): Flow<PlayerWithTeamAndFollowed?>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFollowEntity(followedEntity: FollowedEntity)

    @Query("DELETE FROM followed where player_id = :id")
    suspend fun removeFollowEntity(id: Int)

    @Transaction
    @Query(
        """
         SELECT p.*
        FROM players p
        INNER JOIN followed f ON f.player_id = p.id
    """
    )
    fun observeFollowedPlayers(): Flow<List<PlayerWithTeamAndFollowed>>
}
