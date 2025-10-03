package ir.miare.androidcodechallenge.domain.models.db

import androidx.room.Embedded
import androidx.room.Relation

internal data class PlayerWithTeamAndFollowed(
    @Embedded
    val playerEntity: PlayerEntity,
    @Relation(
        parentColumn = "team_id",
        entityColumn = "id"
    )
    val teamEntity: TeamEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "player_id"
    )
    val followedEntity: FollowedEntity?
)
