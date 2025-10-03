package ir.miare.androidcodechallenge.data.model

data class PlayerItemDataModel(
    val id: Int,
    val name: String,
    val teamRank: Int,
    val goals: Int?,
    val teamName: String,
    val leagueId: Int,
    val leagueName: String,
    val leagueRank: Int,
) : ListDataItem
