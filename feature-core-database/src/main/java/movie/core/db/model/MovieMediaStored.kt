package movie.core.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "movie_media",
    primaryKeys = ["movie", "type"],
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
data class MovieMediaStored(
    @ColumnInfo("movie")
    val movie: String,
    @ColumnInfo("width")
    val width: Int?,
    @ColumnInfo("height")
    val height: Int?,
    @ColumnInfo("url")
    val url: String,
    @ColumnInfo("type")
    val type: Type
) {

    enum class Type(val type: String) {
        Image("image"), Video("video")
    }

}