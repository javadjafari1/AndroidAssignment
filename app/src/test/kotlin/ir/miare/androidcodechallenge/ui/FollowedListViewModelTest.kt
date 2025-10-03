package ir.miare.androidcodechallenge.ui

import io.kotest.matchers.shouldBe
import io.mockk.MockKAnnotations
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import ir.miare.androidcodechallenge.data.TestCoroutineRule
import ir.miare.androidcodechallenge.domain.models.db.PlayerEntity
import ir.miare.androidcodechallenge.domain.models.db.PlayerWithTeamAndFollowed
import ir.miare.androidcodechallenge.domain.models.db.TeamEntity
import ir.miare.androidcodechallenge.domain.models.ui.PlayerItemUiModel
import ir.miare.androidcodechallenge.domain.repository.Repository
import ir.miare.androidcodechallenge.ui.screens.followed.FollowedListViewModel
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

internal class FollowedListViewModelTest {

    @Rule
    @JvmField
    val coroutineRule = TestCoroutineRule()

    @RelaxedMockK
    private lateinit var repository: Repository

    private val fakeList = listOf(
        PlayerItemUiModel(
            id = 6401,
            name = "Messi",
            teamRank = 9619,
            goals = 9986,
            teamName = "Manuel Wooten"
        )
    )

    private val fakePlayer = PlayerWithTeamAndFollowed(
        playerEntity = PlayerEntity(
            id = 1,
            name = "Messi",
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
        every { repository.observeFollowedPlayers() } returns flowOf(fakeList)
    }

    @Test
    fun `getPlayerWithId updates selectedPlayerToShow`() = runTest {
        val viewModel = createSystem()
        every { repository.observePlayerWithTeam(1) } returns flowOf(fakePlayer)

        viewModel.getPlayerWithId(1)
        advanceUntilIdle()

        viewModel.selectedPlayerToShow shouldBe fakePlayer

        viewModel.removeSelectedPlayer()
        viewModel.selectedPlayerToShow shouldBe null
    }

    @Test
    fun `updateFollowStatus true calls followPlayer`() = runTest {
        val viewModel = createSystem()
        coJustRun { repository.followPlayer(1) }

        viewModel.updateFollowStatus(true, 1)
        advanceUntilIdle()

        coVerify { repository.followPlayer(1) }
    }

    @Test
    fun `updateFollowStatus false calls unfollowPlayer`() = runTest {
        val viewModel = createSystem()
        coJustRun { repository.unfollowPlayer(1) }

        viewModel.updateFollowStatus(false, 1)
        advanceUntilIdle()

        coVerify { repository.unfollowPlayer(1) }
    }

    private fun createSystem(): FollowedListViewModel {
        return FollowedListViewModel(
            repository = repository
        )
    }
}
