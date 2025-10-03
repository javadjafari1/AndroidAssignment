package ir.miare.androidcodechallenge.ui

import androidx.paging.PagingData
import io.kotest.matchers.shouldBe
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import ir.miare.androidcodechallenge.data.TestCoroutineRule
import ir.miare.androidcodechallenge.domain.models.SortType
import ir.miare.androidcodechallenge.domain.models.db.PlayerEntity
import ir.miare.androidcodechallenge.domain.models.db.PlayerWithTeamAndFollowed
import ir.miare.androidcodechallenge.domain.models.db.TeamEntity
import ir.miare.androidcodechallenge.domain.repository.Repository
import ir.miare.androidcodechallenge.domain.usecase.ObserveLeaguesWithPlayersUseCase
import ir.miare.androidcodechallenge.ui.screens.home.HomeViewModel
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

internal class HomeViewModelTest {

    @Rule
    @JvmField
    val coroutineRule = TestCoroutineRule()

    @RelaxedMockK
    private lateinit var repository: Repository

    @RelaxedMockK
    private lateinit var observeUseCase: ObserveLeaguesWithPlayersUseCase

    private val fakePlayer = PlayerWithTeamAndFollowed(
        playerEntity = PlayerEntity(
            id = 1,
            name = "Wilbert Cooper",
            totalGoal = 5023,
            teamId = 4575
        ),
        teamEntity = TeamEntity(
            id = 4575,
            name = "Noel Lara",
            rank = 6985,
            leagueId = 6457
        ),
        followedEntity = null
    )

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        coEvery { observeUseCase(any()) } returns flowOf(PagingData.empty())
    }

    @Test
    fun `default state is correct`() {
        val viewModel = createSystem()
        viewModel.currentSortType shouldBe SortType.None
        viewModel.selectedPlayerToShow shouldBe null
    }

    @Test
    fun `updateSortType changes currentSortType`() {
        val viewModel = createSystem()
        viewModel.updateSortType(SortType.MostGoals)
        viewModel.currentSortType shouldBe SortType.MostGoals
    }

    @Test
    fun `getPlayerWithId updates selectedPlayerToShow`() = runTest {
        val viewModel = createSystem()
        coEvery { repository.observePlayerWithTeam(1) } returns flowOf(fakePlayer)

        viewModel.getPlayerWithId(1)

        advanceUntilIdle()

        viewModel.selectedPlayerToShow shouldBe fakePlayer
    }

    @Test
    fun `removeSelectedPlayer clears selectedPlayerToShow`() {
        val viewModel = createSystem()
        coEvery { repository.observePlayerWithTeam(1) } returns flowOf(fakePlayer)

        viewModel.getPlayerWithId(1)

        viewModel.selectedPlayerToShow shouldBe fakePlayer

        viewModel.removeSelectedPlayer()

        viewModel.selectedPlayerToShow shouldBe null
    }

    @Test
    fun `updateFollowStatus true calls followPlayer`() = runTest {
        val viewModel = createSystem()
        coJustRun { repository.followPlayer(1) }
        viewModel.updateFollowStatus(true, 1)
        coVerify { repository.followPlayer(1) }
    }

    @Test
    fun `updateFollowStatus false calls unfollowPlayer`() = runTest {
        val viewModel = createSystem()
        coJustRun { repository.unfollowPlayer(1) }
        viewModel.updateFollowStatus(false, 1)
        coVerify { repository.unfollowPlayer(1) }
    }

    private fun createSystem(): HomeViewModel = HomeViewModel(
        repository = repository,
        observeLeaguesWithPlayersUseCase = observeUseCase
    )
}
