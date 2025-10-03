package ir.miare.androidcodechallenge.data.model

data class LeagueItemDataModel(
    val id: Int,
    val name: String,
    val rank: Int,
    val averageGoal: Double?,
) : ListDataItem
