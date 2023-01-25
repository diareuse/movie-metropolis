package movie.core.db.model

import androidx.room.ColumnInfo
import androidx.room.DatabaseView
import java.util.Date
import kotlin.time.Duration.Companion.milliseconds

@DatabaseView(
    value = "select movies.id,movies.name,movies.url,movies.released_at,movies.duration,movie_previews.screening_from,movie_previews.description,movie_previews.directors,movie_previews.`cast`,movie_previews.country_of_origin,movie_previews.upcoming,movie_previews.genres from movies, movie_previews where movies.id=movie_previews.movie",
    viewName = "movie_preview_views"
)
data class MoviePreviewView(
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
    @ColumnInfo("screening_from")
    val screeningFrom: Date,
    @ColumnInfo("description")
    val description: String,
    @ColumnInfo("directors")
    val directors: Iterable<String>,
    @ColumnInfo("cast")
    val cast: Iterable<String>,
    @ColumnInfo("country_of_origin")
    val countryOfOrigin: String,
    @ColumnInfo("genres")
    val genres: Iterable<String>
) {

    val duration get() = durationMillis.milliseconds

}