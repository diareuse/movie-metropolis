package movie.core.db.model

import androidx.room.ColumnInfo
import androidx.room.DatabaseView
import java.util.Date
import kotlin.time.Duration.Companion.milliseconds

@DatabaseView(
    value = "select id,name,url,released_at,duration,screening_from,description,directors,`cast`,country_of_origin,upcoming from movies join movie_previews on movies.id=movie_previews.movie",
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
    val countryOfOrigin: String
) {

    val duration get() = durationMillis.milliseconds

}