package ir.miare.androidcodechallenge.data.util

import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator

internal class RefreshOnlyRemoteMediator<T : Any>(
    private val shouldFetchOnStart: Boolean,
    private val fetch: suspend () -> Unit
) : RemoteMediator<Int, T>() {

    override suspend fun initialize(): InitializeAction {
        return if (shouldFetchOnStart) {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        } else {
            InitializeAction.SKIP_INITIAL_REFRESH
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, T>
    ): MediatorResult {
        if (loadType == LoadType.PREPEND || loadType == LoadType.APPEND) {
            return MediatorResult.Success(endOfPaginationReached = true)
        }
        return try {
            fetch()
            MediatorResult.Success(endOfPaginationReached = true)
        } catch (t: Throwable) {
            MediatorResult.Error(t)
        }
    }
}
