package ir.miare.androidcodechallenge.data.util

import ir.miare.androidcodechallenge.domain.models.SortType

internal class QueryBuilder(private val sortType: SortType) {

    fun build(): String {
        val baseSelect = """
            SELECT 
                l.id AS leagueId,
                l.name AS leagueName,
                l.country AS leagueCountry,
                l.rank AS leagueRank,
                t.id AS teamId,
                t.name AS teamName,
                t.rank AS teamRank,
                p.id AS playerId,
                p.name AS playerName,
                p.total_goal AS totalGoals
            FROM leagues l
            LEFT JOIN teams t ON t.league_id = l.id
            LEFT JOIN players p ON p.team_id = t.id
        """.trimIndent()

        return when (sortType) {
            SortType.TeamAndLeagueRank -> "$baseSelect ORDER BY l.rank ASC, t.rank ASC"
            SortType.MostGoals -> "$baseSelect ORDER BY p.total_goal DESC"
            SortType.AverageGoalPerMatchOfLeague -> """
                SELECT 
                    l.id AS leagueId,
                    l.name AS leagueName,
                    l.country AS leagueCountry,
                    l.rank AS leagueRank,
                    (CAST(SUM(p.total_goal) AS REAL) / l.total_matches) AS avgGoals,
                    NULL AS playerId,
                    NULL AS playerName,
                    NULL AS teamId,
                    NULL AS teamRank,
                    NULL AS totalGoals
                FROM leagues l
                LEFT JOIN teams t ON t.league_id = l.id
                LEFT JOIN players p ON p.team_id = t.id
                GROUP BY l.id
                ORDER BY avgGoals DESC
            """.trimIndent()

            SortType.None -> baseSelect
        }
    }
}
