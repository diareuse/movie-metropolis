package movie.core.db.model

import androidx.room.ColumnInfo
import androidx.room.DatabaseView
import java.util.Date
import kotlin.time.Duration.Companion.milliseconds

@DatabaseView(
    value = "select m.id,m.name,m.url,m.released_at,m.screening_from,m.duration,mr.poster,mr.video from movies as m, movie_references as mr where m.id=mr.movie",
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
    @ColumnInfo("screening_from")
    val screeningFrom: Date,
    @ColumnInfo("duration")
    val durationMillis: Long,
    @ColumnInfo("poster")
    val poster: String,
    @ColumnInfo("video")
    val video: String?
) {

    val duration get() = durationMillis.milliseconds

}