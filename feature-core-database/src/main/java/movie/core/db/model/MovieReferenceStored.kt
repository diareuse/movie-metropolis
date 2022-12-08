package movie.core.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "movie_references",
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
data class MovieReferenceStored(
    @PrimaryKey
    @ColumnInfo("movie")
    val movie: String,
    @ColumnInfo("poster")
    val poster: String,
    @ColumnInfo("video")
    val video: String?
)