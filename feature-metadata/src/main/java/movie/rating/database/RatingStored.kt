package movie.rating.database

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "ratings",
    primaryKeys = ["name", "year"]
)
internal data class RatingStored(
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "year")
    val year: Int,
    @ColumnInfo(name = "rating")
    val rating: Byte,
    @ColumnInfo(name = "poster")
    val poster: String,
    @ColumnInfo(name = "overlay")
    val overlay: String
)