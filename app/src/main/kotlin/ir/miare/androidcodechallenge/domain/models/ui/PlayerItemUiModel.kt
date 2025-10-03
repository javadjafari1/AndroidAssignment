package ir.miare.androidcodechallenge.domain.models.ui

internal data class PlayerItemUiModel(
    val id: Int,
    val name: String,
    val teamRank: Int,
    val goals: Int?,
    val teamName: String,
) : ListItemUiModel
