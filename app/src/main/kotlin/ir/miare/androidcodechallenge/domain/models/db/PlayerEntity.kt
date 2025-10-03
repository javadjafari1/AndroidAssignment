package ir.miare.androidcodechallenge.domain.models.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "players",
    foreignKeys = [
        ForeignKey(
            entity = TeamEntity::class,
            parentColumns = ["id"],
            childColumns = ["team_id"],
            onDelete = CASCADE,
            onUpdate = CASCADE,
        )
    ],
    indices = [Index("team_id")]
)
internal data class PlayerEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    @ColumnInfo("total_goal")
    val totalGoal: Int,
    @ColumnInfo("team_id")
    val teamId: Int,
)
