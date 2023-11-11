package movie.rating.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "actor_reference_connections",
    primaryKeys = ["actor", "movie"],
    foreignKeys = [
        ForeignKey(ActorStored::class, ["id"], ["actor"], onDelete = ForeignKey.CASCADE),
        ForeignKey(ActorReferenceStored::class, ["id"], ["movie"], onDelete = ForeignKey.CASCADE),
    ]
)
data class ActorReferenceConnection(
    @ColumnInfo("actor") val actor: Long,
    @ColumnInfo("movie") val movie: Long
)