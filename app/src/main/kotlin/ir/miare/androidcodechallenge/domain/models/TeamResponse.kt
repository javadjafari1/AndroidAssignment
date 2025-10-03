package ir.miare.androidcodechallenge.domain.models

import ir.miare.androidcodechallenge.domain.models.db.TeamEntity
import kotlinx.serialization.Serializable

@Serializable
internal data class TeamResponse(
    val name: String,
    val rank: Int
) {
    // There is no unique ID provided in the JSON for teams,
    // so we generate a stable primary key by hashing the team name.
    val id = name.hashCode()

    fun toEntity(leagueId: Int): TeamEntity {
        return TeamEntity(
            id = id,
            name = name,
            rank = rank,
            leagueId = leagueId
        )
    }
}
