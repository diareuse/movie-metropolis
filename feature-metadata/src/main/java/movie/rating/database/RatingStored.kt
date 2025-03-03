package movie.rating.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "ratings")
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
    val overlay: String,
    @ColumnInfo(name = "releaseDate")
    val releaseDate: Date?,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "trailer")
    val trailer: String?,
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Long
)