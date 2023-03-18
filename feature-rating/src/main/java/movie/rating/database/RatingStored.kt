package movie.rating.database

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "ratings",
    primaryKeys = ["name", "year", "url"]
)
internal data class RatingStored(
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "year")
    val year: Int,
    @ColumnInfo(name = "rating")
    val rating: Byte,
    @ColumnInfo(name = "url")
    val url: String
)