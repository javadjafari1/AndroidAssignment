package ir.miare.androidcodechallenge.domain.usecase

import androidx.paging.PagingData
import androidx.paging.map
import ir.miare.androidcodechallenge.data.model.LeagueItemDataModel
import ir.miare.androidcodechallenge.data.model.PlayerItemDataModel
import ir.miare.androidcodechallenge.domain.models.SortType
import ir.miare.androidcodechallenge.domain.models.ui.LeagueItemUiModel
import ir.miare.androidcodechallenge.domain.models.ui.ListItemUiModel
import ir.miare.androidcodechallenge.domain.models.ui.PlayerItemUiModel
import ir.miare.androidcodechallenge.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class ObserveLeaguesWithPlayersUseCaseImpl(
    private val repository: Repository,
) : ObserveLeaguesWithPlayersUseCase {
    override operator fun invoke(sortType: SortType): Flow<PagingData<ListItemUiModel>> {
        return repository.observeLeaguesWithPlayers(sortType)
            .map { pagingData ->
                pagingData.map { item ->
                    when (item) {
                        is LeagueItemDataModel -> {
                            LeagueItemUiModel(
                                id = item.id,
                                name = item.name,
                                rank = item.rank,
                                averageGoal = item.averageGoal
                            )
                        }

                        is PlayerItemDataModel -> {
                            PlayerItemUiModel(
                                id = item.id,
                                name = item.name,
                                teamRank = item.teamRank,
                                goals = item.goals,
                                teamName = item.teamName
                            )
                        }
                    }
                }
            }
    }
}
