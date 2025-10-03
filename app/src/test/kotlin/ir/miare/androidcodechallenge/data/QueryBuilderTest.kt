package ir.miare.androidcodechallenge.data

import io.kotest.matchers.shouldBe
import ir.miare.androidcodechallenge.data.util.QueryBuilder
import ir.miare.androidcodechallenge.domain.models.SortType
import kotlinx.coroutines.test.runTest
import org.junit.Test

internal class QueryBuilderTest {

    @Test
    fun `build returns correct SQL for SortType TeamAndLeagueRank`() = runTest {
        val queryBuilder = QueryBuilder(SortType.TeamAndLeagueRank)
        val sql = queryBuilder.build()
        sql shouldBe """SELECT 
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
            LEFT JOIN players p ON p.team_id = t.id ORDER BY l.rank ASC, t.rank ASC
        """.trimIndent()
    }

    @Test
    fun `build returns correct SQL for SortType MostGoals`() = runTest {
        val queryBuilder = QueryBuilder(SortType.MostGoals)
        val sql = queryBuilder.build()
        sql shouldBe """SELECT 
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
            LEFT JOIN players p ON p.team_id = t.id ORDER BY p.total_goal DESC
        """.trimIndent()
    }

    @Test
    fun `build returns correct SQL for SortType AverageGoalPerMatchOfLeague`() = runTest {
        val queryBuilder = QueryBuilder(SortType.AverageGoalPerMatchOfLeague)
        val sql = queryBuilder.build()
        sql shouldBe """
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
    }

    @Test
    fun `build returns correct SQL for SortType None`() = runTest {
        val queryBuilder = QueryBuilder(SortType.None)
        val sql = queryBuilder.build()
        sql shouldBe """SELECT 
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
            LEFT JOIN players p ON p.team_id = t.id ORDER BY l.id ASC, p.total_goal DESC
        """.trimIndent()
    }
}
