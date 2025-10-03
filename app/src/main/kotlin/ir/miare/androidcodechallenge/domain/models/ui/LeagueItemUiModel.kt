package ir.miare.androidcodechallenge.domain.models.ui

internal data class LeagueItemUiModel(
    val id: Int,
    val name: String,
    val rank: Int,
    val averageGoal: Double?,
) : ListItemUiModel
