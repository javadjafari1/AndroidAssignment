package ir.miare.androidcodechallenge.domain.models

import kotlinx.serialization.Serializable

@Serializable
internal data class LeagueWithPlayersResponse(
    val league: LeagueResponse,
    val players: List<PlayerResponse>
)
