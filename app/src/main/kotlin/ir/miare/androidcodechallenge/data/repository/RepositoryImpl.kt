package ir.miare.androidcodechallenge.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.insertSeparators
import androidx.paging.map
import ir.miare.androidcodechallenge.data.local.LocalDataSource
import ir.miare.androidcodechallenge.data.model.LeagueItemDataModel
import ir.miare.androidcodechallenge.data.model.ListDataItem
import ir.miare.androidcodechallenge.data.model.PlayerItemDataModel
import ir.miare.androidcodechallenge.data.remote.RemoteDataSource
import ir.miare.androidcodechallenge.data.util.RefreshOnlyRemoteMediator
import ir.miare.androidcodechallenge.domain.models.SortType
import ir.miare.androidcodechallenge.domain.models.db.PlayerWithTeamAndFollowed
import ir.miare.androidcodechallenge.domain.models.ui.PlayerItemUiModel
import ir.miare.androidcodechallenge.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlin.math.round

internal class RepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
) : Repository {
    override suspend fun fetchData() {
        val response = remoteDataSource.getLeaguesWithPlayers()

        val leagues = response.map { it.league.toEntity() }
        val teams = response.map {
            it.players.map { player ->
                player.team.toEntity(it.league.id)
            }
        }.flatten()
        val players = response.map {
            it.players.map { player -> player.toEntity() }
        }.flatten()

        localDataSource.insertLeaguesWithTeamsAndPlayers(
            leagues = leagues,
            teams = teams,
            players = players,
        )
    }

    override fun observePlayerWithTeam(id: Int): Flow<PlayerWithTeamAndFollowed?> {
        return localDataSource.observePlayerWithTeam(id)
    }

    override fun observeLeaguesWithPlayers(
        sortType: SortType
    ): Flow<PagingData<ListDataItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                initialLoadSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { localDataSource.observeLeaguesWithPlayers(sortType) },
            remoteMediator = RefreshOnlyRemoteMediator(
                shouldFetchOnStart = true,
                fetch = { fetchData() }
            )
        ).flow
            .map { pagingData ->
                pagingData.map { row ->
                    when (sortType) {
                        SortType.TeamAndLeagueRank,
                        SortType.MostGoals,
                        SortType.None -> {
                            PlayerItemDataModel(
                                id = row.playerId ?: 0,
                                name = row.playerName.orEmpty(),
                                teamRank = row.teamRank ?: -1, // safer fallback
                                goals = row.totalGoals,
                                teamName = row.teamName.orEmpty(),
                                leagueId = row.leagueId,
                                leagueName = row.leagueName,
                                leagueRank = row.leagueRank
                            )
                        }

                        SortType.AverageGoalPerMatchOfLeague -> {
                            LeagueItemDataModel(
                                id = row.leagueId,
                                rank = row.leagueRank,
                                name = row.leagueName,
                                averageGoal = (row.avgGoals ?: 0.0).let {
                                    round(it * 100) / 100.0
                                }
                            )
                        }
                    }
                }.insertSeparators { before, after ->
                    if (after is PlayerItemDataModel) {
                        val newLeague = before == null ||
                            (before as? PlayerItemDataModel)?.leagueId != after.leagueId

                        if (newLeague && sortType != SortType.MostGoals) {
                            LeagueItemDataModel(
                                id = after.leagueId,
                                name = after.leagueName,
                                rank = after.leagueRank,
                                averageGoal = null
                            )
                        } else {
                            null
                        }
                    } else {
                        null
                    }
                }
            }
            .filterNotNull()
    }

    override suspend fun followPlayer(id: Int) {
        localDataSource.followPlayer(id)
    }

    override suspend fun unfollowPlayer(id: Int) {
        localDataSource.unfollowPlayer(id)
    }

    override fun observeFollowedPlayers(): Flow<List<PlayerItemUiModel>> {
        return localDataSource.observeFollowedPlayers()
            .map { list ->
                list.map { player ->
                    PlayerItemUiModel(
                        id = player.playerEntity.id,
                        name = player.playerEntity.name,
                        teamRank = player.teamEntity.rank,
                        goals = player.playerEntity.totalGoal,
                        teamName = player.teamEntity.name
                    )
                }
            }
    }
}
