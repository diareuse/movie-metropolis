package movie.rating.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "actor_references")
data class ActorReferenceStored(
    @PrimaryKey
    @ColumnInfo("id") val id: Long,
    @ColumnInfo("name") val name: String,
    @ColumnInfo("backdrop") val backdrop: String,
    @ColumnInfo("image") val image: String,
    @ColumnInfo("popularity") val popularity: Int,
    @ColumnInfo("rating") val rating: Byte,
    @ColumnInfo("releasedAt") val releasedAt: Date
)