package ir.miare.androidcodechallenge.data.remote

import ir.miare.androidcodechallenge.domain.models.LeagueWithPlayersResponse

internal interface RemoteDataSource {
    suspend fun getLeaguesWithPlayers(): List<LeagueWithPlayersResponse>
}
