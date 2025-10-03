package ir.miare.androidcodechallenge

import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.testing.asSnapshot
import io.kotest.matchers.shouldBe
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import ir.miare.androidcodechallenge.data.db.LeaguePlayerRow
import ir.miare.androidcodechallenge.data.local.LocalDataSource
import ir.miare.androidcodechallenge.data.model.LeagueItemDataModel
import ir.miare.androidcodechallenge.data.model.PlayerItemDataModel
import ir.miare.androidcodechallenge.data.remote.RemoteDataSource
import ir.miare.androidcodechallenge.data.repository.RepositoryImpl
import ir.miare.androidcodechallenge.domain.models.LeagueResponse
import ir.miare.androidcodechallenge.domain.models.LeagueWithPlayersResponse
import ir.miare.androidcodechallenge.domain.models.PlayerResponse
import ir.miare.androidcodechallenge.domain.models.SortType
import ir.miare.androidcodechallenge.domain.models.TeamResponse
import ir.miare.androidcodechallenge.domain.repository.Repository
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

internal class RepositoryTest {
    @Rule
    @JvmField
    val coroutineRule = TestCoroutineRule()

    @RelaxedMockK
    private lateinit var localDataSource: LocalDataSource

    @RelaxedMockK
    private lateinit var remoteDataSource: RemoteDataSource

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `fetchData with empty remote response inserts empty lists`() = runTest {
        val repository = createSystem()

        coEvery { remoteDataSource.getLeaguesWithPlayers() } returns listOf()
        repository.fetchData()

        coVerify(exactly = 1) { remoteDataSource.getLeaguesWithPlayers() }
        coVerify {
            localDataSource.insertLeaguesWithTeamsAndPlayers(
                any(), any(), any()
            )
        }
    }

    @Test
    fun `fetchData inserts leagues teams and players correctly`() = runTest {
        val items = listOf(
            LeagueWithPlayersResponse(
                league = LeagueResponse(
                    name = "Persian league",
                    country = "iran",
                    rank = 1,
                    totalMatches = 23
                ),
                players = listOf(
                    PlayerResponse(
                        name = "javad jafari",
                        totalGoal = 650,
                        team = TeamResponse(
                            name = "Barcelona",
                            rank = 1
                        )
                    )
                )
            )
        )
        val repository = createSystem()
        coEvery { remoteDataSource.getLeaguesWithPlayers() } returns items

        repository.fetchData()

        coVerify {
            localDataSource.insertLeaguesWithTeamsAndPlayers(
                leagues = items.map { it.league.toEntity() },
                teams = items.map { res ->
                    res.players.map { it.team.toEntity(res.league.id) }
                }.flatten(),
                players = items.map { res ->
                    res.players.map { it.toEntity() }
                }.flatten()
            )
        }
    }

    @Test
    fun `TeamAndLeagueRank returns players with separators`() = runTest {
        val repository = createSystem()
        val rows = listOf(
            LeaguePlayerRow(
                leagueId = 1,
                leagueName = "La Liga",
                leagueRank = 1,
                playerId = 100,
                playerName = "Messi",
                teamId = 10,
                teamName = "Barcelona",
                teamRank = 1,
                totalGoals = 30,
                avgGoals = null
            ),
            LeaguePlayerRow(
                leagueId = 1,
                leagueName = "La Liga",
                leagueRank = 1,
                playerId = 101,
                playerName = "Xavi",
                teamId = 10,
                teamName = "Barcelona",
                teamRank = 1,
                totalGoals = 10,
                avgGoals = null
            )
        )

        coEvery { localDataSource.observeLeaguesWithPlayers(SortType.TeamAndLeagueRank) } returns
            createFakePagingSource(rows)

        val snapshot = repository
            .observeLeaguesWithPlayers(SortType.TeamAndLeagueRank)
            .asSnapshot()

        snapshot.filterIsInstance<PlayerItemDataModel>().size shouldBe 2
        snapshot.filterIsInstance<LeagueItemDataModel>().size shouldBe 1
    }

    @Test
    fun `MostGoals returns only players`() = runTest {
        val repository = createSystem()
        val rows = listOf(
            LeaguePlayerRow(
                leagueId = 1,
                leagueName = "Ligue 1",
                leagueRank = 5,
                playerId = 200,
                playerName = "Mbappe",
                teamId = 20,
                teamName = "PSG",
                teamRank = 1,
                totalGoals = 40,
                avgGoals = null,
            )
        )
        coEvery { localDataSource.observeLeaguesWithPlayers(SortType.MostGoals) } returns
            createFakePagingSource(rows)

        val snapshot = repository.observeLeaguesWithPlayers(SortType.MostGoals)
            .asSnapshot()

        snapshot.single() shouldBe PlayerItemDataModel(
            id = 200,
            name = "Mbappe",
            teamRank = 1,
            goals = 40,
            teamName = "PSG",
            leagueId = 1,
            leagueName = "Ligue 1",
            leagueRank = 5
        )
    }

    @Test
    fun `AverageGoalPerMatchOfLeague returns only leagues`() = runTest {
        val repository = createSystem()
        val rows = listOf(
            LeaguePlayerRow(
                leagueId = 2,
                leagueName = "Serie A",
                leagueRank = 3,
                avgGoals = 3.95463,
                playerId = null,
                playerName = null,
                teamId = null,
                teamRank = null,
                teamName = null,
                totalGoals = null,
            )
        )
        coEvery { localDataSource.observeLeaguesWithPlayers(SortType.AverageGoalPerMatchOfLeague) } returns
            createFakePagingSource(rows)

        val snapshot = repository.observeLeaguesWithPlayers(SortType.AverageGoalPerMatchOfLeague)
            .asSnapshot()

        val league = snapshot.single() as LeagueItemDataModel
        league.name shouldBe "Serie A"
        league.averageGoal shouldBe 3.95
    }

    @Test
    fun `None behaves like players with headers`() = runTest {
        val repository = createSystem()
        val rows = listOf(
            LeaguePlayerRow(
                leagueId = 1,
                leagueName = "EPL",
                leagueRank = 1,
                playerId = 300,
                playerName = "Kane",
                teamId = 30,
                teamName = "Spurs",
                teamRank = 2,
                totalGoals = 25,
                avgGoals = null,
            ),
            LeaguePlayerRow(
                leagueId = 2,
                leagueName = "EPL2",
                leagueRank = 2,
                playerId = 301,
                playerName = "Salah",
                teamId = 31,
                teamName = "Liverpool",
                teamRank = 1,
                totalGoals = 27,
                avgGoals = null,
            )
        )
        coEvery { localDataSource.observeLeaguesWithPlayers(SortType.None) } returns
            createFakePagingSource(rows)

        val snapshot = repository.observeLeaguesWithPlayers(SortType.None)
            .asSnapshot()

        snapshot.filterIsInstance<PlayerItemDataModel>().size shouldBe 2
        snapshot.filterIsInstance<LeagueItemDataModel>().size shouldBe 2
    }

    private fun createSystem(): Repository = RepositoryImpl(
        remoteDataSource = remoteDataSource,
        localDataSource = localDataSource
    )

    private fun createFakePagingSource(rows: List<LeaguePlayerRow>): PagingSource<Int, LeaguePlayerRow> {
        return object : PagingSource<Int, LeaguePlayerRow>() {
            override fun getRefreshKey(state: PagingState<Int, LeaguePlayerRow>): Int? {
                return null
            }

            override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LeaguePlayerRow> {
                return LoadResult.Page(
                    data = rows,
                    prevKey = null,
                    nextKey = null
                )
            }
        }
    }
}
