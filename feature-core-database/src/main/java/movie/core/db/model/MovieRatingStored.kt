package movie.core.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "movie_ratings",
    foreignKeys = [
        ForeignKey(
            entity = MovieStored::class,
            parentColumns = ["id"],
            childColumns = ["movie"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("movie")
    ]
)
data class MovieRatingStored(
    @PrimaryKey
    @ColumnInfo("movie")
    val movie: String,
    @ColumnInfo("rating")
    val rating: Byte,
    @ColumnInfo("link_imdb")
    val linkImdb: String?,
    @ColumnInfo("link_rt")
    val linkRottenTomatoes: String?,
    @ColumnInfo("link_csfd")
    val linkCsfd: String?,
    @ColumnInfo("created_at")
    val createdAt: Date = Date()
)