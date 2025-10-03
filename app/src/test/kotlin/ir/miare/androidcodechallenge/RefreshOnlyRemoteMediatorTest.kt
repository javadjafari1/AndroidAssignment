package ir.miare.androidcodechallenge

import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.mockk
import ir.miare.androidcodechallenge.data.util.RefreshOnlyRemoteMediator
import kotlinx.coroutines.test.runTest
import org.junit.Test
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import org.junit.Rule
import java.io.IOException

internal class RefreshOnlyRemoteMediatorTest {

    @Rule
    @JvmField
    val coroutineRule = TestCoroutineRule()

    private val mockedFetch = mockk<suspend () -> Unit>()

    val pagingState = PagingState<Int, String>(
        pages = listOf(),
        anchorPosition = null,
        config = PagingConfig(10),
        leadingPlaceholderCount = 10
    )

    @Test
    fun `initialize returns correct action based on shouldFetchOnStart`() = runTest {
        val fetchOnStartMediator = RefreshOnlyRemoteMediator<String>(true, mockedFetch)
        fetchOnStartMediator.initialize() shouldBe RemoteMediator.InitializeAction.LAUNCH_INITIAL_REFRESH

        val cachedMediator = RefreshOnlyRemoteMediator<String>(false, mockedFetch)
        cachedMediator.initialize() shouldBe RemoteMediator.InitializeAction.SKIP_INITIAL_REFRESH
    }

    @Test
    fun `prepend load returns Success with endOfPaginationReached true`() = runTest {
        val remoteMediator = RefreshOnlyRemoteMediator<String>(false, mockedFetch)

        val result = remoteMediator.load(LoadType.PREPEND, pagingState)
        result.shouldBeInstanceOf<RemoteMediator.MediatorResult.Success>()
        result.endOfPaginationReached shouldBe true
    }

    @Test
    fun `append load returns Success with endOfPaginationReached true`() = runTest {
        val remoteMediator = RefreshOnlyRemoteMediator<String>(false, mockedFetch)

        val result = remoteMediator.load(LoadType.PREPEND, pagingState)
        result.shouldBeInstanceOf<RemoteMediator.MediatorResult.Success>()
        result.endOfPaginationReached shouldBe true
    }

    @Test
    fun `refresh load returns Success and endOfPaginationReached true`() = runTest {
        coJustRun { mockedFetch() }

        val remoteMediator = RefreshOnlyRemoteMediator<String>(false, mockedFetch)

        val result = remoteMediator.load(LoadType.REFRESH, pagingState)
        result.shouldBeInstanceOf<RemoteMediator.MediatorResult.Success>()
        result.endOfPaginationReached shouldBe true
    }

    @Test
    fun `refresh load returns Error when fetch throws exception`() = runTest {
        coEvery { mockedFetch() } throws IOException("")

        val remoteMediator = RefreshOnlyRemoteMediator<String>(false, mockedFetch)

        val result = remoteMediator.load(LoadType.REFRESH, pagingState)
        result.shouldBeInstanceOf<RemoteMediator.MediatorResult.Error>()
    }
}
