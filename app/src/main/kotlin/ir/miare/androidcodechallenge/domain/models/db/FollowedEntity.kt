package ir.miare.androidcodechallenge.domain.models.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "followed")
internal data class FollowedEntity(
    @PrimaryKey
    @ColumnInfo("player_id")
    val playerId: Int,
)
