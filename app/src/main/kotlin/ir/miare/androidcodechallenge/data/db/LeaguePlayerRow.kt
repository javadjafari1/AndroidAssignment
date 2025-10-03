package ir.miare.androidcodechallenge.data.db

internal data class LeaguePlayerRow(
    val leagueId: Int,
    val leagueName: String,
    val leagueRank: Int,
    val avgGoals: Double?,
    val playerId: Int?,
    val playerName: String?,
    val teamId: Int?,
    val teamRank: Int?,
    val teamName: String?,
    val totalGoals: Int?
)
