package ir.miare.androidcodechallenge.domain.usecase

import androidx.paging.PagingData
import ir.miare.androidcodechallenge.domain.models.SortType
import ir.miare.androidcodechallenge.domain.models.ui.ListItemUiModel
import kotlinx.coroutines.flow.Flow

internal interface ObserveLeaguesWithPlayersUseCase {
    operator fun invoke(sortType: SortType): Flow<PagingData<ListItemUiModel>>
}
