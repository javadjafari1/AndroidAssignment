package ir.miare.androidcodechallenge.domain.models

import ir.miare.androidcodechallenge.domain.models.db.LeagueEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class LeagueResponse(
    val name: String,
    val country: String,
    val rank: Int,
    @SerialName("total_matches")
    val totalMatches: Int
) {
    // There is no unique ID provided in the JSON for leagues,
    // so we generate a stable primary key by hashing the league name + country.
    // This ensures uniqueness across reloads and keeps IDs consistent
    // (e.g., "La Liga" + "Spain" will always map to the same ID).
    val id = (name + country).hashCode()

    fun toEntity(): LeagueEntity {
        return LeagueEntity(
            id = id,
            name = name,
            country = country,
            rank = rank,
            totalMatches = totalMatches
        )
    }
}
