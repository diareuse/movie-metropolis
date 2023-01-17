package movie.core.db.model

import androidx.room.ColumnInfo
import androidx.room.DatabaseView
import java.util.Date
import kotlin.time.Duration.Companion.milliseconds

@DatabaseView(
    value = "select movies.id,movies.name,movies.url,movies.released_at,movies.duration,movie_references.poster,movie_references.video from movies, movie_references where movies.id=movie_references.movie",
    viewName = "movie_reference_views"
)
data class MovieReferenceView(
    @ColumnInfo("id")
    val id: String,
    @ColumnInfo("name")
    val name: String,
    @ColumnInfo("url")
    val url: String,
    @ColumnInfo("released_at")
    val releasedAt: Date,
    @ColumnInfo("duration")
    val durationMillis: Long,
    @ColumnInfo("poster")
    val poster: String,
    @ColumnInfo("video")
    val video: String?
) {

    val duration get() = durationMillis.milliseconds

}