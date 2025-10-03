package ir.miare.androidcodechallenge.domain.repository

import androidx.paging.PagingData
import ir.miare.androidcodechallenge.data.model.ListDataItem
import ir.miare.androidcodechallenge.domain.models.SortType
import kotlinx.coroutines.flow.Flow

internal interface Repository {
    suspend fun fetchData()
    fun observeLeaguesWithPlayers(sortType: SortType): Flow<PagingData<ListDataItem>>
}
