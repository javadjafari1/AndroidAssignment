package ir.miare.androidcodechallenge.domain.models

import ir.miare.androidcodechallenge.domain.models.db.PlayerEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class PlayerResponse(
    val name: String,
    val team: TeamResponse,
    @SerialName("total_goal")
    val totalGoal: Int
) {
    // There is no unique ID provided in the JSON for leagues,
    // so we generate a stable primary key by hashing the league name.
    val id = name.hashCode()

    fun toEntity(): PlayerEntity {
        return PlayerEntity(
            id = id,
            name = name,
            totalGoal = totalGoal,
            teamId = team.id
        )
    }
}
