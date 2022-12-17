package movie.core.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "movie_favorites",
    foreignKeys = [
        ForeignKey(
            entity = MovieStored::class,
            parentColumns = ["id"],
            childColumns = ["movie"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class MovieFavoriteStored(
    @PrimaryKey
    @ColumnInfo(name = "movie")
    val movie: String,
    @ColumnInfo(name = "created_at")
    val createdAt: Date = Date()
)