package movie.rating.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "actors")
data class ActorStored(
    @PrimaryKey
    @ColumnInfo("id") val id: Long,
    @ColumnInfo("name") val name: String,
    @ColumnInfo("popularity") val popularity: Int,
    @ColumnInfo("image") val image: String,
    @ColumnInfo("query") val query: String
)