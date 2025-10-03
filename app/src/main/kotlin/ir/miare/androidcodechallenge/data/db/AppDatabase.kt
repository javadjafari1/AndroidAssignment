package ir.miare.androidcodechallenge.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ir.miare.androidcodechallenge.domain.models.db.FollowedEntity
import ir.miare.androidcodechallenge.domain.models.db.LeagueEntity
import ir.miare.androidcodechallenge.domain.models.db.PlayerEntity
import ir.miare.androidcodechallenge.domain.models.db.TeamEntity

@Database(
    entities = [
        LeagueEntity::class,
        PlayerEntity::class,
        TeamEntity::class,
        FollowedEntity::class,
    ],
    version = 1
)
internal abstract class AppDatabase : RoomDatabase() {
    abstract fun getDao(): Dao
}
