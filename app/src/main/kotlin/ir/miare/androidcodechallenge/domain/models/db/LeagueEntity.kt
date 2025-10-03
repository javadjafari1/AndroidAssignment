package ir.miare.androidcodechallenge.domain.models.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "leagues")
internal data class LeagueEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val country: String,
    val rank: Int,
    @ColumnInfo("total_matches")
    val totalMatches: Int
)
