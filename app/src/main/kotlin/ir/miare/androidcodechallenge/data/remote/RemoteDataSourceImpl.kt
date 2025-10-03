package ir.miare.androidcodechallenge.data.remote

import ir.miare.androidcodechallenge.data.util.toSuccessResponse
import ir.miare.androidcodechallenge.domain.models.LeagueWithPlayersResponse

internal class RemoteDataSourceImpl : RemoteDataSource {
    override suspend fun getLeaguesWithPlayers(): List<LeagueWithPlayersResponse> {
        return javaClass.classLoader.toSuccessResponse(
            GET_LEAGUES_WITH_PLAYERS_FAKE_DATA_PATH
        )
    }

    private companion object {
        const val GET_LEAGUES_WITH_PLAYERS_FAKE_DATA_PATH = "fakeData.json"
    }
}
