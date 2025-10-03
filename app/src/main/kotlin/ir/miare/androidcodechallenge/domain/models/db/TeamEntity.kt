package ir.miare.androidcodechallenge.domain.models.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    "teams",
    foreignKeys = [
        ForeignKey(
            entity = LeagueEntity::class,
            parentColumns = ["id"],
            childColumns = ["league_id"],
            onDelete = CASCADE,
            onUpdate = CASCADE,
        )
    ],
    indices = [Index("league_id")]
)
internal data class TeamEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val rank: Int,
    @ColumnInfo("league_id")
    val leagueId: Int
)
